package com.aidanmars.nodesim.game.skija

import com.aidanmars.nodesim.core.Node
import com.aidanmars.nodesim.core.NodeType
import com.aidanmars.nodesim.core.getChunk
import com.aidanmars.nodesim.game.skija.Constants.SIZE_CHUNK
import com.aidanmars.nodesim.game.skija.Constants.SIZE_TILE
import com.aidanmars.nodesim.game.skija.hud.HudElement
import com.aidanmars.nodesim.game.skija.hud.HudElementGroup
import com.aidanmars.nodesim.game.skija.world.drawNode
import com.aidanmars.nodesim.game.skija.world.drawWire
import io.github.humbleui.skija.Canvas
import io.github.humbleui.skija.Paint
import io.github.humbleui.types.IRect
import io.github.humbleui.types.Point
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWErrorCallback
import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.max
import kotlin.math.pow

class NodeSimWindow : Window("NodeSim") {
    val data = GameData(
        ::topLeftScreenLocation,
        ::bottomRightScreenLocation,
        ::setBuildTypesHidden
    )
    val HUDElements = mutableListOf<HudElement>() // elements later in the list have priority
    val hudElementGroup = HudElementGroup()
    private var lastDrawCallDate = 0L

    fun runGame() {
        GLFWErrorCallback.createPrint(System.err).set()
        check(glfwInit()) { "Unable to initialize GLFW" }

        val vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor())
        val width = (vidmode!!.width() * 0.75).toInt()
        val height = (vidmode.height() * 0.75).toInt()
        val bounds = IRect.makeXYWH(
            max(0, (vidmode.width() - width) / 2),
            max(0, (vidmode.height() - height) / 2),
            width,
            height
        )
        run(bounds)
    }

    fun getVirtualScreenLocation(trueScreenX: Float, trueScreenY: Float): VirtualScreenLocation {
        return VirtualScreenLocation(trueScreenX - (width / 2), trueScreenY - (height / 2))
    }

    fun getTrueScreenLocation(virtualScreenLocation: VirtualScreenLocation): Point {
        return Point(virtualScreenLocation.x + (width / 2), virtualScreenLocation.y + (height / 2))
    }

    fun getTrueScreenLocation(worldLocation: WorldLocation): Point {
        return getTrueScreenLocation(data.getVirtualScreenLocation(worldLocation))
    }

    private fun topLeftScreenLocation(): WorldLocation = data.getWorldLocation(
        getVirtualScreenLocation(0f, 0f)
    )

    private fun bottomRightScreenLocation(): WorldLocation = data.getWorldLocation(
        getVirtualScreenLocation(width.toFloat(), height.toFloat())
    )

    override fun onKeyPress(window: Long, key: Int, scanCode: Int, action: Int, mods: Int) {
        if (window != this.window) return
        var didBreak = false
        if (action == GLFW_PRESS) {
            for (index in HUDElements.indices.reversed()) {
                if (!HUDElements[index].isFocused) continue
                if (HUDElements[index].onKeyEvent(this, key, mods)) {
                    didBreak = true
                    break
                }
            }
        }
        if (!didBreak) {
            onKeyEvent(key, action)
        }
    }

    private fun onKeyEvent(key: Int, action: Int) {
        if (action == GLFW_PRESS) {
            when (key) {
                GLFW_KEY_1, GLFW_KEY_2, GLFW_KEY_3, GLFW_KEY_4, GLFW_KEY_5 -> onNodeKeyEvent(key)
                GLFW_KEY_E -> data.currentTool = ToolType.Place
                GLFW_KEY_R -> data.currentTool = ToolType.Delete
                GLFW_KEY_T -> data.currentTool = ToolType.Interact
                GLFW_KEY_G -> data.currentTool = ToolType.Connect
                GLFW_KEY_W -> data.wasdKeysPressed[0] = true
                GLFW_KEY_A -> data.wasdKeysPressed[1] = true
                GLFW_KEY_S -> data.wasdKeysPressed[2] = true
                GLFW_KEY_D -> data.wasdKeysPressed[3] = true
            }
        } else if (action == GLFW_RELEASE) {
            when (key) {
                GLFW_KEY_W -> data.wasdKeysPressed[0] = false
                GLFW_KEY_A -> data.wasdKeysPressed[1] = false
                GLFW_KEY_S -> data.wasdKeysPressed[2] = false
                GLFW_KEY_D -> data.wasdKeysPressed[3] = false
            }
        }
    }

    private fun setBuildTypesHidden(hidden: Boolean) {
        hudElementGroup.switchElement.isHidden = hidden
        hudElementGroup.lightElement.isHidden = hidden
        hudElementGroup.norGateElement.isHidden = hidden
        hudElementGroup.andGateElement.isHidden = hidden
        hudElementGroup.xorGateElement.isHidden = hidden
    }

    private fun onNodeKeyEvent(key: Int) {
        data.currentTool = ToolType.Place
        data.currentPlaceType = when (key) {
            GLFW_KEY_1 -> NodeType.Switch
            GLFW_KEY_2 -> NodeType.Light
            GLFW_KEY_3 -> NodeType.NorGate
            GLFW_KEY_4 -> NodeType.AndGate
            GLFW_KEY_5 -> NodeType.XorGate
            else -> throw IllegalArgumentException("no node with that key bind")
        }
    }

    override fun onScroll(window: Long, xOffset: Double, yOffset: Double) {
        val scaleDifference = 1.1.pow(yOffset)
        data.scale *= scaleDifference.toFloat()
        data.scale = data.scale.coerceAtMost(10f)
        data.scale = data.scale.coerceAtLeast(0.1f)
    }

    override fun onMouseButtonEvent(window: Long, button: Int, action: Int, mods: Int) {
        if (window != this.window || button != GLFW_MOUSE_BUTTON_1) return
        if (action == GLFW_PRESS) {
            var didBreak = false
            for (index in HUDElements.indices.reversed()) {
                if (HUDElements[index].isHidden) continue
                if (HUDElements[index].onClick(this, Point(xPos.toFloat(), yPos.toFloat()))) {
                    didBreak = true
                    break
                }
            }
            if (!didBreak) {
                data.handleMousePress(data.getWorldLocation(
                    getVirtualScreenLocation(xPos.toFloat(), yPos.toFloat())
                ))
            }
        } else if (action == GLFW_RELEASE) {
            data.handleMouseRelease(data.getWorldLocation(
                getVirtualScreenLocation(xPos.toFloat(), yPos.toFloat())
            ))
        }
    }

    fun initHudElements() {
        setBuildTypesHidden(data.currentTool != ToolType.Place)
        hudElementGroup.addAllToList(HUDElements)
    }

    override fun init() {
        lastDrawCallDate = System.currentTimeMillis()
        initHudElements()
        data.startSimulation()
    }

    override fun terminate() {
        data.stopSimulation()
        //TODO: do whatever needs to be done upon closing
    }

    fun closeGraceFully() {
        glfwSetWindowShouldClose(window, true)
    }

    override fun draw(canvas: Canvas) {
        val currentTime = System.currentTimeMillis()
        val multiplier = (currentTime - lastDrawCallDate).toFloat()
        lastDrawCallDate = currentTime
        if (data.wasdKeysPressed[0]) data.playerY -= (0.625f / data.scale * multiplier).toInt()
        if (data.wasdKeysPressed[1]) data.playerX -= (0.625f / data.scale * multiplier).toInt()
        if (data.wasdKeysPressed[2]) data.playerY += (0.625f / data.scale * multiplier).toInt()
        if (data.wasdKeysPressed[3]) data.playerX += (0.625f / data.scale * multiplier).toInt()
        if (data.sl2ShouldChaseMouse) data.moveSl2InChase(
            data.getWorldLocation(getVirtualScreenLocation(xPos.toFloat(), yPos.toFloat()))
        )
        canvas.clear(Colors.background)
        drawBackground(canvas)

        drawNodesAndWires(canvas)

        drawHUD(canvas)
    }

    private fun drawNodesAndWires(canvas: Canvas) {
        val topLeftWorldLocation = topLeftScreenLocation()
        val bottomRightWorldLocation = bottomRightScreenLocation()
        val topLeftChunk = getChunk(topLeftWorldLocation.x, topLeftWorldLocation.y)
        val bottomRightChunk = getChunk(bottomRightWorldLocation.x, bottomRightWorldLocation.y)
        val chunks = ArrayList<Set<Node>>(
            abs(bottomRightChunk.first - topLeftChunk.first) *
            abs(bottomRightChunk.second - topLeftChunk.second)
        )
        for (chunkX in topLeftChunk.first..bottomRightChunk.first) {
            for (chunkY in topLeftChunk.second..bottomRightChunk.second) {
                val chunk = data.circuit.chunks[chunkX, chunkY]
                if (chunk === null) continue
                chunks.add(chunk)

                chunk.forEach {
                    drawNode(it, this, canvas)
                }
            }
        }
        drawWires(chunks, canvas)
    }

    private fun drawWires(chunks: List<Set<Node>>, canvas: Canvas) {
        chunks.forEach { chunk ->
            chunk.forEach { node ->
                node.inputNodes.forEach {
                    drawWire1(it, node, canvas)
                }
                node.outputNodes.forEach {
                    drawWire1(node, it, canvas)
                }
            }
        }
    }

    private fun drawWire1(from: Node, to: Node, canvas: Canvas) {
        drawWire(
            getTrueScreenLocation(WorldLocation(from.x, from.y)),
            getTrueScreenLocation(WorldLocation(to.x, to.y)),
            from.outputPower,
            data.scale,
            canvas
        )
    }

    private fun drawBackground(canvas: Canvas) {
        val tileSize = data.scale * SIZE_TILE
        val (worldTopLeftX, worldTopLeftY) = topLeftScreenLocation()

        val worldOriginX = worldTopLeftX - (worldTopLeftX % SIZE_TILE)
        val worldOriginY = worldTopLeftY - (worldTopLeftY % SIZE_TILE)
        val chunkX = (worldOriginX / SIZE_TILE) % SIZE_CHUNK
        val chunkY = (worldOriginY / SIZE_TILE) % SIZE_CHUNK

        val (screenOriginX, screenOriginY) = getTrueScreenLocation(WorldLocation(
            worldOriginX,
            worldOriginY
        ))

        val numTilesX = (ceil(width / tileSize).toInt() + 2)
        val numTilesY = (ceil(height / tileSize).toInt() + 2)
        val xMax = width.toFloat()
        val yMax = height.toFloat()
        // draw tile lines
        if (data.scale >= 0.125f) {
            Paint().use {
                it.color = Colors.tileLine
                it.strokeWidth = 3f * data.scale

                var x = screenOriginX
                for (index in 0..numTilesX) {
                    canvas.drawLine(x, 0f, x, yMax, it)
                    x += tileSize
                }
                var y = screenOriginY
                for (index in 0..numTilesY) {
                    canvas.drawLine(0f, y, xMax, y, it)
                    y += tileSize
                }
            }
        }
        Paint().use {
            it.color = Colors.chunkLine
            it.strokeWidth = 4f * data.scale

            val chunkSize = tileSize * SIZE_CHUNK
            var x = screenOriginX - (chunkX * tileSize)
            for (index in 0..(numTilesX / SIZE_CHUNK)) {
                canvas.drawLine(x, 0f, x, yMax, it)
                x += chunkSize
            }
            var y = screenOriginY - (chunkY * tileSize)
            for (index in 0..(numTilesX / SIZE_CHUNK)) {
                canvas.drawLine(0f, y, xMax, y, it)
                y += chunkSize
            }
        }
        Paint().use {
            it.color = Colors.zeroZeroLine
            it.strokeWidth = 7f
            if (data.scale > 1f) it.strokeWidth *= data.scale

            val (screenX, screenY) = getTrueScreenLocation(WorldLocation(0, 0))
            if (screenX.toInt() in -3..width) {
                canvas.drawLine(screenX, 0f, screenX, yMax, it)
            }
            if (screenY.toInt() in -3..height) {
                canvas.drawLine(0f, screenY, xMax, screenY, it)
            }
        }
    }

    private fun drawHUD(canvas: Canvas) {
        HUDElements.forEach {
            if (!it.isHidden) {
                it.draw(this, canvas)
            }
        }
        drawSelection(canvas)
    }

    private fun drawSelection(canvas: Canvas) {
        if (!data.showSelection) return
        when (data.currentTool) {
            ToolType.Place -> {}
            ToolType.Delete -> {}
            ToolType.Interact -> {}
            ToolType.Connect -> {
                if (data.selectedNode === null) return
                drawWire(
                    getTrueScreenLocation(data.selectionLocation1),
                    getTrueScreenLocation(data.selectionLocation2),
                    data.selectedNode?.first?.outputPower ?: false,
                    data.scale,
                    canvas
                )
            }
        }
    }
}