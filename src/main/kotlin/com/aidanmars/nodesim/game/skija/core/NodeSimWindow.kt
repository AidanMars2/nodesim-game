package com.aidanmars.nodesim.game.skija.core

import com.aidanmars.nodesim.game.skija.register.types.RegisterAble
import com.aidanmars.nodesim.game.skija.Window
import com.aidanmars.nodesim.game.skija.core.registers.NodeSimActorHandler
import com.aidanmars.nodesim.game.skija.core.registers.NodeSimInputHandler
import com.aidanmars.nodesim.game.skija.core.registers.NodeSimRenderer
import com.aidanmars.nodesim.game.skija.register.types.actors.ConstantActor
import com.aidanmars.nodesim.game.skija.register.types.actors.DrawAble
import com.aidanmars.nodesim.game.skija.register.types.data.DataListener
import com.aidanmars.nodesim.game.skija.register.types.input.InputListener
import io.github.humbleui.skija.Canvas
import io.github.humbleui.types.IRect
import io.github.humbleui.types.Point
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWErrorCallback
import kotlin.math.*

//TODO: circuit saving/blueprints
//TODO: advanced circuit manipulation(copying, pasting, cutting, mass deletion, packaging)
//TODO: undo/redo
class NodeSimWindow : Window("NodeSim") {
    //<editor-fold desc="old code" defaultstate="collapsed">
//    private val hudElements = mutableListOf<HudElement>() // elements later in the list have priority
//    val hudElementGroup = HudElementGroup()
//    private var lastDrawCallDate = 0L
//    private val nodesOnScreen = mutableSetOf<Node>()
//    val data = GameData(
//        ::setBuildTypesHidden,
//        nodesOnScreen,
//        hudElementGroup
//    )
//    private var oldTopLeftChunk = topLeftScreenLocation().chunk()
//    private var oldBottomRightChunk = bottomRightScreenLocation().chunk()
//
//    fun runGame() {
//        GLFWErrorCallback.createPrint(System.err).set()
//        check(glfwInit()) { "Unable to initialize GLFW" }
//
//        val videoMode = glfwGetVideoMode(glfwGetPrimaryMonitor())
//        val width = (videoMode!!.width() * 0.75).toInt()
//        val height = (videoMode.height() * 0.75).toInt()
//        val bounds = IRect.makeXYWH(
//            max(0, (videoMode.width() - width) / 2),
//            max(0, (videoMode.height() - height) / 2),
//            width,
//            height
//        )
//        run(bounds)
//    }
//
//    //<editor-fold desc="utility functions" defaultstate="collapsed">
//    fun getVirtualScreenLocation(trueScreenX: Float, trueScreenY: Float): VirtualScreenLocation {
//        return VirtualScreenLocation(trueScreenX - (width / 2), trueScreenY - (height / 2))
//    }
//
//    fun getTrueScreenLocation(virtualScreenLocation: VirtualScreenLocation): Point {
//        return Point(virtualScreenLocation.x + (width / 2), virtualScreenLocation.y + (height / 2))
//    }
//
//    fun getTrueScreenLocation(worldLocation: WorldLocation): Point {
//        return getTrueScreenLocation(data.getVirtualScreenLocation(worldLocation))
//    }
//
//    private fun topLeftScreenLocation(): WorldLocation = data.getWorldLocation(
//        getVirtualScreenLocation(0f, 0f)
//    )
//
//    private fun bottomRightScreenLocation(): WorldLocation = data.getWorldLocation(
//        getVirtualScreenLocation(width.toFloat(), height.toFloat())
//    )
//
//    private fun setNodesOnScreen() {
//        val topLeftChunk = topLeftScreenLocation().chunk()
//        val bottomRightChunk = bottomRightScreenLocation().chunk()
//        if (topLeftChunk == oldTopLeftChunk ||
//            bottomRightChunk == oldBottomRightChunk) return
//        nodesOnScreen.clear()
//
//        for (chunkX in topLeftChunk.x..bottomRightChunk.x) {
//            for (chunkY in topLeftChunk.y..bottomRightChunk.y) {
//                val chunk = data.circuit.chunks[chunkX, chunkY]
//                if (chunk === null) continue
//
//                nodesOnScreen.addAll(chunk)
//            }
//        }
//        oldTopLeftChunk = topLeftChunk
//        oldBottomRightChunk = bottomRightChunk
//    }
//    //</editor-fold>
//
//    //<editor-fold desc="event handlers" defaultstate="collapsed">
//    override fun onKeyPress(window: Long, key: Int, scanCode: Int, action: Int, mods: Int) {
//        if (window != this.window) return
//        var didBreak = false
//        if (action == GLFW_PRESS) {
//            for (index in hudElements.indices.reversed()) {
//                if (!hudElements[index].isFocused) continue
//                if (hudElements[index].onKeyEvent(this, key, mods)) {
//                    didBreak = true
//                    break
//                }
//            }
//        }
//        if (!didBreak) {
//            onKeyEvent(key, action)
//        }
//    }
//
//    private fun onKeyEvent(key: Int, action: Int) {
//        if (action == GLFW_PRESS) {
//            when (key) {
//                GLFW_KEY_1, GLFW_KEY_2, GLFW_KEY_3, GLFW_KEY_4, GLFW_KEY_5 -> onNodeKeyEvent(key)
//                GLFW_KEY_E -> data.currentTool = ToolType.Place
//                GLFW_KEY_R -> data.currentTool = ToolType.Delete
//                GLFW_KEY_T -> data.currentTool = ToolType.Interact
//                GLFW_KEY_G -> data.currentTool = ToolType.Connect
//                GLFW_KEY_Q -> data.currentTool = ToolType.Select
//                GLFW_KEY_W -> data.wasdKeysPressed[0] = true
//                GLFW_KEY_A -> data.wasdKeysPressed[1] = true
//                GLFW_KEY_S -> data.wasdKeysPressed[2] = true
//                GLFW_KEY_D -> data.wasdKeysPressed[3] = true
//            }
//        } else if (action == GLFW_RELEASE) {
//            when (key) {
//                GLFW_KEY_W -> data.wasdKeysPressed[0] = false
//                GLFW_KEY_A -> data.wasdKeysPressed[1] = false
//                GLFW_KEY_S -> data.wasdKeysPressed[2] = false
//                GLFW_KEY_D -> data.wasdKeysPressed[3] = false
//            }
//        }
//    }
//
//    private fun onNodeKeyEvent(key: Int) {
//        data.currentTool = ToolType.Place
//        data.currentPlaceType = when (key) {
//            GLFW_KEY_1 -> NodeType.Switch
//            GLFW_KEY_2 -> NodeType.Light
//            GLFW_KEY_3 -> NodeType.NorGate
//            GLFW_KEY_4 -> NodeType.AndGate
//            GLFW_KEY_5 -> NodeType.XorGate
//            else -> throw IllegalArgumentException("no node with that key bind")
//        }
//    }
//
//    override fun onScroll(window: Long, xOffset: Double, yOffset: Double) {
//        val scaleDifference = 1.1.pow(yOffset)
//        data.scale *= scaleDifference.toFloat()
//        data.scale = data.scale.coerceAtMost(10f)
//        data.scale = data.scale.coerceAtLeast(0.1f)
//        setNodesOnScreen()
//    }
//
//    override fun onMouseButtonEvent(window: Long, button: Int, action: Int, mods: Int) {
//        if (window != this.window || button != GLFW_MOUSE_BUTTON_1) return
//        if (action == GLFW_PRESS) {
//            var didBreak = false
//            for (index in hudElements.indices.reversed()) {
//                if (hudElements[index].shouldDraw) continue
//                if (hudElements[index].onClick(this, Point(xPos.toFloat(), yPos.toFloat()))) {
//                    didBreak = true
//                    break
//                }
//            }
//            if (!didBreak) {
//                data.handleMousePress(data.getWorldLocation(
//                    getVirtualScreenLocation(xPos.toFloat(), yPos.toFloat())
//                ))
//            }
//        } else if (action == GLFW_RELEASE) {
//            data.handleMouseRelease(data.getWorldLocation(
//                getVirtualScreenLocation(xPos.toFloat(), yPos.toFloat())
//            ))
//        }
//    }
//    //</editor-fold>
//
//    private fun setBuildTypesHidden(hidden: Boolean) {
//        hudElementGroup.switchElement.shouldDraw = hidden
//        hudElementGroup.lightElement.shouldDraw = hidden
//        hudElementGroup.norGateElement.shouldDraw = hidden
//        hudElementGroup.andGateElement.shouldDraw = hidden
//        hudElementGroup.xorGateElement.shouldDraw = hidden
//    }
//
//    //<editor-fold desc="init/terminate code" defaultstate="collapsed">
//    fun initHudElements() {
//        setBuildTypesHidden(data.currentTool != ToolType.Place)
//        hudElementGroup.addAllToList(hudElements)
//    }
//
//    override fun init() {
//        lastDrawCallDate = System.currentTimeMillis()
//        initHudElements()
//        data.startSimulation()
//    }
//
//    override fun terminate() {
//        data.stopSimulation()
//        //TODO: do whatever needs to be done upon closing
//    }
//
//    override fun onFocusEvent(window: Long, focused: Boolean) {
//        //TODO: call events for this
//    }
//
//    fun closeGraceFully() {
//        glfwSetWindowShouldClose(window, true)
//    }
//    //</editor-fold>
//
//    private fun doGameUpdate() {
//        val currentTime = System.currentTimeMillis()
//        val multiplier = (currentTime - lastDrawCallDate).toFloat()
//        lastDrawCallDate = currentTime
//        if (data.wasdKeysPressed[0]) data.playerY -= (0.625f / data.scale * multiplier).toInt()
//        if (data.wasdKeysPressed[1]) data.playerX -= (0.625f / data.scale * multiplier).toInt()
//        if (data.wasdKeysPressed[2]) data.playerY += (0.625f / data.scale * multiplier).toInt()
//        if (data.wasdKeysPressed[3]) data.playerX += (0.625f / data.scale * multiplier).toInt()
//        if (data.sl2ShouldChaseMouse) data.moveSl2InChase(
//            data.getWorldLocation(getVirtualScreenLocation(xPos.toFloat(), yPos.toFloat()))
//        )
//        setNodesOnScreen()
//    }
//
//    //<editor-fold desc="rendering code" defaultstate="collapsed">
//    override fun draw(canvas: Canvas) {
//        doGameUpdate()
//        canvas.clear(Colors.background)
//        drawBackground(canvas)
//
//        drawNodesAndWires(canvas)
//
//        drawHUD(canvas)
//    }
//
//    //<editor-fold desc="node/wire rendering" defaultstate="collapsed">
//    private fun drawNodesAndWires(canvas: Canvas) {
//        nodesOnScreen.forEach {
//            drawNode(it, this, canvas)
//            if (nodeShouldBeHighLighted(it)) {
//                canvas.drawCircle(
//                    getTrueScreenLocation(WorldLocation(it.x, it.y)),
//                    20f, Colors.selectionBorder
//                )
//            }
//        }
//        drawWires(canvas)
//    }
//
//    private fun nodeShouldBeHighLighted(node: Node): Boolean {
//        return data.currentTool == ToolType.Select &&
//            data.showSelection &&
//            (node.x in min(data.selectionLocation1.x, data.selectionLocation2.x)
//                    ..max(data.selectionLocation1.x, data.selectionLocation2.x)) &&
//
//            (node.y in min(data.selectionLocation1.y, data.selectionLocation2.y)
//                    ..max(data.selectionLocation1.y, data.selectionLocation2.y))
//    }
//
//    private fun drawWires(canvas: Canvas) {
//        nodesOnScreen.forEach { node ->
//            node.inputNodes.forEach {
//                drawWire1(it, node, canvas)
//            }
//            node.outputNodes.forEach {
//                drawWire1(node, it, canvas)
//            }
//        }
//    }
//
//    private fun drawWire1(from: Node, to: Node, canvas: Canvas) {
//        drawWire(
//            getTrueScreenLocation(WorldLocation(from.x, from.y)),
//            getTrueScreenLocation(WorldLocation(to.x, to.y)),
//            from.outputPower,
//            data.scale,
//            canvas
//        )
//    }
//    //</editor-fold>
//
//    private fun drawBackground(canvas: Canvas) {
//        val tileSize = data.scale * SIZE_TILE
//        val (worldTopLeftX, worldTopLeftY) = topLeftScreenLocation()
//
//        val worldOriginX = worldTopLeftX - (worldTopLeftX % SIZE_TILE)
//        val worldOriginY = worldTopLeftY - (worldTopLeftY % SIZE_TILE)
//        val chunkX = (worldOriginX / SIZE_TILE) % SIZE_CHUNK
//        val chunkY = (worldOriginY / SIZE_TILE) % SIZE_CHUNK
//
//        val (screenOriginX, screenOriginY) = getTrueScreenLocation(
//            WorldLocation(
//            worldOriginX,
//            worldOriginY
//        )
//        )
//
//        val numTilesX = (ceil(width / tileSize).toInt() + 2)
//        val numTilesY = (ceil(height / tileSize).toInt() + 2)
//        val xMax = width.toFloat()
//        val yMax = height.toFloat()
//        // draw tile lines
//        if (data.scale >= 0.125f) {
//            Paint().use {
//                it.color = Colors.tileLine
//                it.strokeWidth = 2f * data.scale
//
//                var x = screenOriginX
//                for (index in 0..numTilesX) {
//                    canvas.drawLine(x, 0f, x, yMax, it)
//                    x += tileSize
//                }
//                var y = screenOriginY
//                for (index in 0..numTilesY) {
//                    canvas.drawLine(0f, y, xMax, y, it)
//                    y += tileSize
//                }
//            }
//        }
//        Paint().use {
//            it.color = Colors.chunkLine
//            it.strokeWidth = 3f * data.scale
//
//            val chunkSize = tileSize * SIZE_CHUNK
//            var x = screenOriginX - (chunkX * tileSize)
//            for (index in 0..(numTilesX / SIZE_CHUNK)) {
//                canvas.drawLine(x, 0f, x, yMax, it)
//                x += chunkSize
//            }
//            var y = screenOriginY - (chunkY * tileSize)
//            for (index in 0..(numTilesX / SIZE_CHUNK)) {
//                canvas.drawLine(0f, y, xMax, y, it)
//                y += chunkSize
//            }
//        }
//        Paint().use {
//            it.color = Colors.zeroZeroLine
//            it.strokeWidth = 6f
//            if (data.scale > 1f) it.strokeWidth *= data.scale
//
//            val (screenX, screenY) = getTrueScreenLocation(WorldLocation(0, 0))
//            if (screenX.toInt() in -3..width) {
//                canvas.drawLine(screenX, 0f, screenX, yMax, it)
//            }
//            if (screenY.toInt() in -3..height) {
//                canvas.drawLine(0f, screenY, xMax, screenY, it)
//            }
//        }
//    }
//
//    private fun drawHUD(canvas: Canvas) {
//        drawSelection(canvas)
//        hudElements.forEach {
//            if (!it.shouldDraw) {
//                it.draw(this, canvas)
//            }
//        }
//    }
//
//    private fun drawSelection(canvas: Canvas) {
//        hudElementGroup.selectionElement.shouldDraw = !data.showSelection || data.sl2ShouldChaseMouse
//        if (!data.showSelection) return
//        when (data.currentTool) {
//            ToolType.Place -> {}
//            ToolType.Delete -> {}
//            ToolType.Interact -> {}
//            ToolType.Connect -> drawConnectSelection(canvas)
//            ToolType.Select -> drawSelectSelection(canvas)
//        }
//    }
//
//    private fun drawConnectSelection(canvas: Canvas) {
//        if (data.selectedNode === null) return
//        drawWire(
//            getTrueScreenLocation(data.selectionLocation1),
//            getTrueScreenLocation(data.selectionLocation2),
//            data.selectedNode?.first?.outputPower ?: false,
//            data.scale,
//            canvas
//        )
//    }
//
//    private fun drawSelectSelection(canvas: Canvas) {
//        val (sx1, sy1) = getTrueScreenLocation(data.selectionLocation1)
//        val (sx2, sy2) = getTrueScreenLocation(data.selectionLocation2)
//        Paint().use {
//            it.color = Colors.selectionBorder
//            it.strokeWidth = 6f
//            it.strokeCap = PaintStrokeCap.ROUND
//            canvas.drawLine(sx1, sy1, sx2, sy1, it)
//            canvas.drawLine(sx1, sy1, sx1, sy2, it)
//            canvas.drawLine(sx1, sy2, sx2, sy2, it)
//            canvas.drawLine(sx2, sy1, sx2, sy2, it)
//        }
//        // moved to SelectionElement.kt
////        if (!data.sl2ShouldChaseMouse) {
////            val topLeftX = min(data.selectionLocation1.x, data.selectionLocation2.x)
////            val topLeftY = min(data.selectionLocation1.y, data.selectionLocation2.y)
////            val drawOriginX = topLeftX - 35f
////            val drawOriginY = topLeftY.toFloat()
////            canvas.drawSvg(
////                SvgDoms.Hud.Selection.copyOption,
////                Point(drawOriginX, drawOriginY), Point(30f, 30f)
////            )
////            canvas.drawSvg(
////                SvgDoms.Hud.Selection.moveOption,
////                Point(drawOriginX, drawOriginY + 70f), Point(30f, 30f)
////            )
////            canvas.drawSvg(
////                SvgDoms.Hud.Selection.deleteOption,
////                Point(drawOriginX, drawOriginY + 140f), Point(30f, 30f)
////            )
////            canvas.drawSvg(
////                SvgDoms.Hud.Selection.packageOption,
////                Point(drawOriginX, drawOriginY + 210f), Point(30f, 30f)
////            )
////        }
//    }
//    //</editor-fold>
    //</editor-fold>

    val data = NodeSimData()
    private val inputHandler = NodeSimInputHandler()
    private val renderer = NodeSimRenderer()
    private val actorActor = NodeSimActorHandler()

    override fun onKeyPress(window: Long, key: Int, scanCode: Int, action: Int, mods: Int) {
        inputHandler.onKeyEvent(action, key, mods)
    }

    override fun onScroll(window: Long, xOffset: Double, yOffset: Double) {
        inputHandler.onScrollEvent(xOffset, yOffset)
    }

    override fun onMouseButtonEvent(window: Long, button: Int, action: Int, mods: Int) {
        inputHandler.onMouseButtonEvent(action, button, Point(mouseX.toFloat(), mouseY.toFloat()))
    }

    override fun onFocusEvent(window: Long, focused: Boolean) {
        // make sure the mouse release function is called
        inputHandler.onMouseButtonEvent(GLFW_RELEASE, GLFW_MOUSE_BUTTON_1, Point(mouseX.toFloat(), mouseY.toFloat()))
    }

    override fun onMouseMoveEvent(newMousePoint: Point) {
        inputHandler.onMouseMoveEvent(newMousePoint)
    }

    override fun init() {
        fillRegister()
        data.init()
    }

    override fun terminate() {
        data.terminate()
        //TODO: see if the renderer and inputHandler need terminate functions
    }

    override fun draw(canvas: Canvas) {
        actorActor.act()
        data.windowWidth = width
        data.windowHeight = height
        renderer.draw(canvas)
    }

    fun register(registerAble: RegisterAble) {
        if (registerAble is ConstantActor) actorActor.register(registerAble)
        if (registerAble is DrawAble) renderer.register(registerAble)
        if (registerAble is DataListener) data.dataListenerHandler.register(registerAble)
        if (registerAble is InputListener) inputHandler.register(registerAble)
    }

    fun runGame() {
        GLFWErrorCallback.createPrint(System.err).set()
        check(glfwInit()) { "Unable to initialize GLFW" }

        val videoMode = glfwGetVideoMode(glfwGetPrimaryMonitor())
        val width = (videoMode!!.width() * 0.75).toInt()
        val height = (videoMode.height() * 0.75).toInt()
        val bounds = IRect.makeXYWH(
            max(0, (videoMode.width() - width) / 2),
            max(0, (videoMode.height() - height) / 2),
            width,
            height
        )
        run(bounds)
    }
}