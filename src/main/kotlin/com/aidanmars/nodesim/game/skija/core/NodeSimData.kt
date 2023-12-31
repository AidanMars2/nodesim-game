package com.aidanmars.nodesim.game.skija.core

import com.aidanmars.nodesim.core.Circuit
import com.aidanmars.nodesim.core.Node
import com.aidanmars.nodesim.core.extensions.getNodesInRegion
import com.aidanmars.nodesim.core.extensions.tick
import com.aidanmars.nodesim.game.skija.Analytics
import com.aidanmars.nodesim.game.skija.core.registers.NodeSimDataListenerHandler
import com.aidanmars.nodesim.game.skija.distanceToLine
import com.aidanmars.nodesim.game.skija.location
import com.aidanmars.nodesim.game.skija.types.ToolType
import com.aidanmars.nodesim.game.skija.types.VirtualScreenLocation
import com.aidanmars.nodesim.game.skija.types.WorldLocation
import io.github.humbleui.types.Point
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.math.floor
import kotlin.time.DurationUnit
import kotlin.time.measureTime
import kotlin.time.toDuration

class NodeSimData(
    val closeGraceFully: () -> Unit
) {
    // analytics
    val analytics = Analytics()

    // listeners
    val dataListenerHandler = NodeSimDataListenerHandler()

    // action verification values
    var isAskingVerification = false
        private set

    // window info
    var windowWidth = 0
    var windowHeight = 0
    var mouseWorldX = 0
    var mouseWorldY = 0

    // player location info
    var playerLocation = WorldLocation(0, 0)
        set(value) {
            field = value
            dataListenerHandler.onPlayerMove()
        }
    val playerX: Int
        get() = playerLocation.x
    val playerY: Int
        get() = playerLocation.y
    var scale = 1f
        set(value) {
            field = value
            dataListenerHandler.onPlayerMove()
        }

    // circuit
    var circuit = Circuit()
    val circuitMutex = Mutex() // must be used to connect, disconnect, create, or delete nodes in the circuit
    private var isSimulating = false

    // quick rendering values
    val nodesOnScreen: Set<Node>
        get() {
            val (topLeftX, topLeftY) = topLeftScreenLocation()
            val (bottomRightX, bottomRightY) = bottomRightScreenLocation()
            return analytics.measureTime("data.nodesOnScreen") {
                circuit.getNodesInRegion(topLeftX..bottomRightX, topLeftY..bottomRightY)
            }
        }

    var placeSnapDistance = 1
    var currentTool = ToolType.Interact
        set(value) {
            field = value
            dataListenerHandler.onToolSwitch()
        }

    //<editor-fold desc="utility functions" defaultstate="collapsed">
    //<editor-fold desc="conversion functions" defaultstate="collapsed">
    fun screenPointAt(worldLocation: WorldLocation): Point =
        screenPointAt(virtualScreenPoint(worldLocation))

    fun screenPointAt(virtualScreenLocation: VirtualScreenLocation): Point =
        Point(
            virtualScreenLocation.x + (windowWidth / 2),
            virtualScreenLocation.y + (windowHeight / 2)
        )

    private fun virtualScreenPoint(screenPoint: Point): VirtualScreenLocation =
        VirtualScreenLocation(
            screenPoint.x - (windowWidth / 2),
            screenPoint.y - (windowHeight / 2)
        )

    private fun virtualScreenPoint(worldLocation: WorldLocation): VirtualScreenLocation =
        VirtualScreenLocation(
            (worldLocation.x - playerX) * scale,
            (worldLocation.y - playerY) * scale
        )

    fun worldLocationAt(virtualScreenLocation: VirtualScreenLocation): WorldLocation =
        WorldLocation(
            ((virtualScreenLocation.x / scale) + playerX).toInt(),
            ((virtualScreenLocation.y / scale) + playerY).toInt()
        )

    fun worldLocationAt(screenPoint: Point): WorldLocation =
        worldLocationAt(virtualScreenPoint(screenPoint))
    //</editor-fold>

    //<editor-fold desc="node getting functions" defaultstate="collapsed">
    fun getTopObjectAt(location: WorldLocation): Pair<Node, Node?>? {
        val topNode = getTopNodeAt(location)
        if (topNode !== null) return topNode to null
        return getTopWireAt(location)
    }

    fun getTopNodeAt(location: WorldLocation): Node? {
        return nodesOnScreen.lastOrNull {
            WorldLocation(it.x, it.y).distanceTo(location) < 20.0
        }
    }

    fun getTopWireAt(location: WorldLocation): Pair<Node, Node>? {
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
    }
    //</editor-fold>

    //<editor-fold desc="screen location functions" defaultstate="collapsed">
    fun topLeftScreenLocation(): WorldLocation = worldLocationAt(Point.ZERO)

    fun bottomRightScreenLocation(): WorldLocation =
        worldLocationAt(Point(windowWidth.toFloat(), windowHeight.toFloat()))
    //</editor-fold>

    //<editor-fold desc="node placement functions" defaultstate="collapsed">
    fun getNodePlaceLocation(location: WorldLocation): WorldLocation {
        if (placeSnapDistance == 1) return location
        return WorldLocation(
            (floor((location.x.toDouble() / placeSnapDistance) + 0.5) * placeSnapDistance).toInt(),
            (floor((location.y.toDouble() / placeSnapDistance) + 0.5) * placeSnapDistance).toInt()
        )
    }

    fun placeNodeAtLocation(node: Node, location: WorldLocation) {
        val (x, y) = getNodePlaceLocation(location)
        circuit.moveNode(node, x, y)
    }
    //</editor-fold>

    //<editor-fold desc="verification functions" defaultstate="collapsed">
    fun startVerification() {
        if (isAskingVerification) return
        isAskingVerification = true
    }

    fun answerVerification(answer: Boolean) {
        if (!isAskingVerification) return
        isAskingVerification = false
        dataListenerHandler.onYesNoAnswer(answer)
    }
    //</editor-fold>
    inline fun withCircuitMutexLocked(crossinline block: () -> Unit) {
        runBlocking {
            circuitMutex.withLock {
                block()
            }
        }
    }
    //</editor-fold>

    //<editor-fold desc="simulation functions" defaultstate="collapsed">
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
    //</editor-fold>

    fun init() {
        startSimulation()
    }

    fun terminate() {
        stopSimulation()
    }
}