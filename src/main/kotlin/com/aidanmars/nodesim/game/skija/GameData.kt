package com.aidanmars.nodesim.game.skija

import com.aidanmars.nodesim.core.Circuit
import com.aidanmars.nodesim.core.Node
import com.aidanmars.nodesim.core.NodeType
import com.aidanmars.nodesim.core.extensions.tick
import com.aidanmars.nodesim.core.getChunk
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.math.abs
import kotlin.math.floor
import kotlin.time.DurationUnit
import kotlin.time.measureTime
import kotlin.time.toDuration
import kotlin.time.toJavaDuration

class GameData(
    val setNodeElementsHidden: (hidden: Boolean) -> Unit,
    val nodesOnScreen: Set<Node>
) {
    var playerX = 0
    var playerY = 0
    var scale = 1f

    /**
     * chunks can be safely accessed without locking the mutex,
     * although the data in the nodes concerning power can change.
     */
    var circuit = Circuit()
    var selectionLocation1 = WorldLocation(0, 0)
    var selectionLocation2 = WorldLocation(0, 0)
    var selectedNode: Pair<Node, Node?>? = null
    private var isHolding = false
    var sl2ShouldChaseMouse = false
    var showSelection = false
    var currentPlaceType = NodeType.Switch
    var wasdKeysPressed = BooleanArray(4)
    var currentTool = ToolType.Interact
        set(value) {
            field = value
            setNodeElementsHidden(value != ToolType.Place)
        }
    private val circuitMutex = Mutex()
    private var isSimulating = false
    var placeSnapDistance = 1
    private var interactionDrag = false

    fun startSimulation() {
        if (isSimulating) return
        Thread {
            isSimulating = true
            while (isSimulating) {
                val simulationTime = measureTime {
                    runBlocking {
                        circuitMutex.withLock {
                            circuit.tick(1u)
                        }
                    }
                }
                val sleepTime = 23L.toDuration(DurationUnit.MILLISECONDS) - simulationTime
                try {
                    if (sleepTime.isPositive()) {
                        Thread.sleep(sleepTime.inWholeMilliseconds, (sleepTime.inWholeNanoseconds % 1_000_000).toInt())
                    }
                    Thread.sleep(2)
                } catch (_: InterruptedException) {}
            }
        }.start()
    }

    fun stopSimulation() {
        isSimulating = false
    }

    fun getVirtualScreenLocation(worldLocation: WorldLocation): VirtualScreenLocation =
        VirtualScreenLocation((worldLocation.x - playerX) * scale, (worldLocation.y - playerY) * scale)

    fun getWorldLocation(virtualScreenLocation: VirtualScreenLocation): WorldLocation =
        WorldLocation(
            ((virtualScreenLocation.x / scale) + playerX).toInt(),
            ((virtualScreenLocation.y / scale) + playerY).toInt()
        )

    fun moveSl2InChase(newLocation: WorldLocation) {
        selectionLocation2 = newLocation
        when (currentTool) {
            ToolType.Interact -> {
                if (newLocation.distanceTo(selectionLocation1) >= 20.0 || interactionDrag) {
                    placeNodeAtLocation(selectedNode?.first ?: return, newLocation)
                    interactionDrag = true
                }
            }
            ToolType.Select -> {
                selectionLocation2 = getNodePlaceLocation(newLocation)
            }
            else -> {}
        }
    }

    fun placeNodeAtLocation(node: Node, location: WorldLocation) {
        val (x, y) = getNodePlaceLocation(location)
        circuit.moveNode(node, x, y)
    }

    fun getNodePlaceLocation(location: WorldLocation): WorldLocation {
        if (placeSnapDistance == 1) return location
        return WorldLocation(
            (floor((location.x.toDouble() / placeSnapDistance) + 0.5) * placeSnapDistance).toInt(),
            (floor((location.y.toDouble() / placeSnapDistance) + 0.5) * placeSnapDistance).toInt()
        )
    }

    fun handleMousePress(mouseLocation: WorldLocation) {
        sl2ShouldChaseMouse = false
        showSelection = false
        selectedNode = null
        isHolding = true
        selectionLocation1 = mouseLocation
        when (currentTool) {
            ToolType.Place -> handlePlacePress(mouseLocation)
            ToolType.Delete -> handleDeletePress(mouseLocation)
            ToolType.Interact -> handleInteractPress(mouseLocation)
            ToolType.Connect -> handleConnectPress(mouseLocation)
            ToolType.Select -> handleSelectPress(mouseLocation)
        }
    }

    fun handleMouseRelease(mouseLocation: WorldLocation) {
        if (!isHolding) return
        isHolding = false

        sl2ShouldChaseMouse = false
        when (currentTool) {
            ToolType.Place -> handlePlaceRelease(mouseLocation)
            ToolType.Delete -> handleDeleteRelease(mouseLocation)
            ToolType.Interact -> handleInteractRelease(mouseLocation)
            ToolType.Connect -> handleConnectRelease(mouseLocation)
            ToolType.Select -> handleSelectRelease(mouseLocation)
        }
    }

    private fun handleInteractPress(mouseLocation: WorldLocation) {
        sl2ShouldChaseMouse = true

        val topNode = getTopNodeAt(mouseLocation)
        if (topNode === null) {
            selectedNode = null
            return
        }
        selectedNode = topNode to null
    }

    private fun handleInteractRelease(mouseLocation: WorldLocation) {
        if (mouseLocation.distanceTo(selectionLocation1) >= 20.0 || interactionDrag) {
            placeNodeAtLocation(selectedNode?.first ?: return, mouseLocation)
            return
        }
        interactionDrag = false
        val topNode = getTopNodeAt(mouseLocation)
        if (topNode === null) return
        runBlocking {
            circuitMutex.withLock {
                circuit.triggerNode(topNode)
            }
        }
    }

    private fun handleConnectPress(mouseLocation: WorldLocation) {
        val topNode = getTopNodeAt(mouseLocation)
        if (topNode !== null) {
            sl2ShouldChaseMouse = true
            showSelection = true
            selectionLocation1 = topNode.location()
            selectedNode = topNode to null
        } else {selectedNode = null}
    }

    private fun handleConnectRelease(mouseLocation: WorldLocation) {
        val secondNode = getTopNodeAt(mouseLocation)
        if (secondNode === null) return
        if (selectedNode === null) return
        val firstNode = selectedNode!!.first
        runBlocking {
            circuitMutex.withLock {
                circuit.connectNodes(firstNode, secondNode)
            }
        }
    }

    private fun handleDeletePress(mouseLocation: WorldLocation) {
        sl2ShouldChaseMouse = true
        showSelection = true
    }

    private fun handleDeleteRelease(mouseLocation: WorldLocation) {
        val topObjectAtMouse = getTopObjectAt(mouseLocation)
        when {
            topObjectAtMouse === null -> {}
            topObjectAtMouse.second === null -> runBlocking {
                circuitMutex.withLock {
                    circuit.deleteNode(topObjectAtMouse.first)
                }
            }
            else -> runBlocking {
                circuitMutex.withLock {
                    circuit.disconnectNodes(topObjectAtMouse.first, topObjectAtMouse.second!!)
                }
            }
        }
    }

    private fun handlePlacePress(mouseLocation: WorldLocation) {}

    private fun handlePlaceRelease(mouseLocation: WorldLocation) {
        val nodeLocation = getNodePlaceLocation(mouseLocation)
        runBlocking {
            circuitMutex.withLock {
                circuit.createNode(nodeLocation.x, nodeLocation.y, currentPlaceType)
            }
        }
    }

    private fun handleSelectPress(mouseLocation: WorldLocation) {
        selectionLocation1 = getNodePlaceLocation(mouseLocation)
        sl2ShouldChaseMouse = true
        showSelection = true
    }

    private fun handleSelectRelease(mouseLocation: WorldLocation) {
        selectionLocation2 = getNodePlaceLocation(mouseLocation)
    }

    private fun getTopObjectAt(location: WorldLocation): Pair<Node, Node?>? {
        val topNode = getTopNodeAt(location)
        if (topNode !== null) return topNode to null
        return getTopWireAt(location)
    }

    private fun getTopNodeAt(location: WorldLocation): Node? {
        return nodesOnScreen.lastOrNull {
            WorldLocation(it.x, it.y).distanceTo(location) < 20.0
        }
//        val (centerChunkX, centerChunkY) = getChunk(location.x, location.y)
//        for (chunkX in ((centerChunkX - 1)..(centerChunkX + 1)).reversed()) {
//            for (chunkY in ((centerChunkY - 1)..(centerChunkY + 1)).reversed()) {
//                val chunk = circuit.chunks[chunkX, chunkY]
//                if (chunk === null) continue
//
//                val node = chunk.lastOrNull { WorldLocation(it.x, it.y).distanceTo(location) < 20.0 }
//                if (node === null) continue
//                return node
//            }
//        }
//        return null
    }

    private fun getTopWireAt(location: WorldLocation): Pair<Node, Node>? {
        var topSelectedWire: Pair<Node, Node>? = null
        nodesOnScreen.forEach { node ->
            node.inputNodes.forEach { otherNode ->
                if (distanceToLine(location, otherNode.location(), node.location()) < 5f) {
                    topSelectedWire = otherNode to node
                }
            }
            node.outputNodes.forEach { otherNode ->
                if (distanceToLine(location, otherNode.location(), node.location()) < 5f) {
                    topSelectedWire = node to otherNode
                }
            }
        }
        return topSelectedWire
//        val topLeftLocation = topLeftScreenLocation()
//        val bottomRightLocation = bottomRightScreenLocation()
//        val (chunkXMin, chunkYMin) = getChunk(topLeftLocation.x, topLeftLocation.y)
//        val (chunkXMax, chunkYMax) = getChunk(bottomRightLocation.x, bottomRightLocation.y)
//        for (chunkX in (chunkXMin..chunkXMax).reversed()) {
//            for (chunkY in (chunkYMin..chunkYMax).reversed()) {
//                val chunk = circuit.chunks[chunkX, chunkY]
//                if (chunk === null) continue
//
//                var topSelectedWire: Pair<Node, Node>? = null
//                chunk.forEach {node ->
//                    node.inputNodes.forEach { otherNode ->
//                        if (distanceToLine(location, otherNode.location(), node.location()) < 5f) {
//                            topSelectedWire = otherNode to node
//                        }
//                    }
//                    node.outputNodes.forEach { otherNode ->
//                        if (distanceToLine(location, otherNode.location(), node.location()) < 5f) {
//                            topSelectedWire = node to otherNode
//                        }
//                    }
//                }
//                if (topSelectedWire !== null) return topSelectedWire
//            }
//        }
//        return null
    }
}