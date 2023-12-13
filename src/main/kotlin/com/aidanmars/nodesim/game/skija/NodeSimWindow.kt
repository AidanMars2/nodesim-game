package com.aidanmars.nodesim.game.skija

import com.aidanmars.nodesim.game.skija.Constants.SIZE_CHUNK
import com.aidanmars.nodesim.game.skija.Constants.SIZE_TILE
import com.aidanmars.nodesim.game.skija.hud.HudElement
import com.aidanmars.nodesim.game.skija.hud.HudElementGroup
import com.aidanmars.nodesim.game.skija.hud.PlaceHudElement
import io.github.humbleui.skija.Canvas
import io.github.humbleui.skija.Paint
import io.github.humbleui.types.IRect
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWErrorCallback
import kotlin.math.ceil
import kotlin.math.max

class NodeSimWindow : Window("NodeSim") {
    val data = GameData()
    val HUDElements = mutableListOf<HudElement>() // elements later in the list have priority
    val hudElementGroup = HudElementGroup()

    fun runGame() {
        GLFWErrorCallback.createPrint(System.err).set()
        check(glfwInit()) { "Unable to initialize GLFW" }

        val vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor())
        val width = (vidmode!!.width() * 0.75).toInt()
        val height = (vidmode.height() * 0.75).toInt()
        val bounds = IRect.makeXYWH(
            max(0f, ((vidmode.width() - width) / 2).toFloat()).toInt(),
            max(0f, ((vidmode.height() - height) / 2).toFloat()).toInt(),
            width,
            height
        )
        run(bounds)
    }
    fun getVirtualScreenLocation(trueScreenX: Float, trueScreenY: Float): VirtualScreenLocation {
        return VirtualScreenLocation(trueScreenX - (width / 2), trueScreenY - (height / 2))
    }

    fun getTrueScreenLocation(virtualScreenLocation: VirtualScreenLocation): Pair<Float, Float> {
        return virtualScreenLocation.x + (width / 2) to virtualScreenLocation.y + (height / 2)
    }

    fun getTrueScreenLocation(worldLocation: WorldLocation): Pair<Float, Float> {
        return getTrueScreenLocation(data.getVirtualScreenLocation(worldLocation))
    }

    fun topLeftScreenLocation() = data.getWorldLocation(
        getVirtualScreenLocation(0f, 0f)
    )

    fun bottomRightScreenLocation() = data.getWorldLocation(
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
        //TODO: handle keys for the main area
    }

    override fun onScroll(window: Long, xOffset: Double, yOffset: Double) {
        //TODO: handle scale changing
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
                data.selectionLocation1 = data.getWorldLocation(
                    getVirtualScreenLocation(xPos.toFloat(), yPos.toFloat())
                )
            }
        } else if (action == GLFW_RELEASE) {
            data.selectionLocation2 = data.getWorldLocation(
                getVirtualScreenLocation(xPos.toFloat(), yPos.toFloat())
            )
        }
    }

    fun initHudElements() {
        HUDElements.add(PlaceHudElement())
    }


    override fun init() {
        initHudElements()
    }

    override fun terminate() {
        //TODO: do whatever needs to be done upon closing
    }

    override fun draw(canvas: Canvas) {
        canvas.clear(Colors.background)
        drawBackground(canvas)

        //TODO: draw nodes and wires

        drawHUD(canvas)
    }

    private fun drawBackground(canvas: Canvas) {
        val tileSize = data.scale * SIZE_TILE
        val (worldTopLeftX, worldTopLeftY) = topLeftScreenLocation()

        val worldOriginX = worldTopLeftX - (worldTopLeftX % SIZE_TILE)
        val worldOriginY = worldTopLeftY - (worldTopLeftY % SIZE_TILE)
        val chunkX = (worldOriginX / SIZE_TILE) % SIZE_CHUNK
        val chunkY = (worldOriginY / SIZE_TILE) % SIZE_CHUNK

        var (screenOriginX, screenOriginY) = getTrueScreenLocation(WorldLocation(worldOriginX, worldOriginY))
        screenOriginX *= data.scale
        screenOriginY *= data.scale

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
            it.strokeWidth = 5f * data.scale

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
    }
}