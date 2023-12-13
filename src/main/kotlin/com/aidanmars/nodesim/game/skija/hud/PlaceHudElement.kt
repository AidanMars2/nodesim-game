package com.aidanmars.nodesim.game.skija.hud

import com.aidanmars.nodesim.game.skija.Colors
import com.aidanmars.nodesim.game.skija.NodeSimWindow
import com.aidanmars.nodesim.game.skija.Point
import com.aidanmars.nodesim.game.skija.ToolType
import io.github.humbleui.skija.Canvas
import io.github.humbleui.skija.Paint
import io.github.humbleui.skija.PaintStrokeCap

class PlaceHudElement : HudElement {
    override var isHidden: Boolean = false
    override var isFocused: Boolean = false

    override fun draw(window: NodeSimWindow, canvas: Canvas) {
        val (x, y) = getButtonPoint(window)
        Paint().use {
            val selected = window.data.currentTool == ToolType.Place
            it.color = if (selected) Colors.placeElementBorderSelected else Colors.placeElementBorder
            canvas.drawCircle(x, y, 35f, it)
        }
        Paint().use {
            it.color = Colors.placeElementMain
            canvas.drawCircle(x, y, 30f, it)
        }
        Paint().use {
            it.color = Colors.placeElementPlus
            it.strokeWidth = 5f
            it.strokeCap = PaintStrokeCap.ROUND
            canvas.drawLine(x - 18f, y, x + 18f, y, it)
            canvas.drawLine(x, y - 18f, x, y + 18f, it)
        }
    }

    override fun onClick(window: NodeSimWindow, mouseLocation: Point): Boolean {
        if (getButtonPoint(window).distance(mouseLocation) > 35f) return false
        window.data.currentTool = ToolType.Place
        return true
    }

    override fun onKeyEvent(window: NodeSimWindow, key: Int, mods: Int): Boolean {
        isFocused = false
        return false
    }

    private fun getButtonPoint(window: NodeSimWindow): Point {
        return Point(60f, window.height - 60f)
    }
}