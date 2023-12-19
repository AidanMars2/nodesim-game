package com.aidanmars.nodesim.game.skija.hud.elements

import com.aidanmars.nodesim.game.skija.Colors
import com.aidanmars.nodesim.game.skija.NodeSimWindow
import com.aidanmars.nodesim.game.skija.ToolType
import com.aidanmars.nodesim.game.skija.*
import com.aidanmars.nodesim.game.skija.hud.HudElement
import io.github.humbleui.skija.Canvas
import io.github.humbleui.skija.Paint
import io.github.humbleui.skija.PaintStrokeCap
import io.github.humbleui.types.Point

class PlaceHudElement : HudElement {
    private companion object {
        val elementDom = getSvgFromResource("hud/placeElement.svg")
    }
    override var isHidden: Boolean = false
    override var isFocused: Boolean = false

    override fun draw(window: NodeSimWindow, canvas: Canvas) {
        val point = getButtonPoint(window)
        val (x, y) = point
        if (window.data.currentTool == ToolType.Place) {
            canvas.drawCircle(point, 40f, Colors.toolbarElementBorderSelected)
        }
        canvas.drawSvg(elementDom, Point(x - 35f, y - 35f), Point(70f, 70f))
//        canvas.drawCircle(point, 35f, Colors.placeElementBorder)
//        canvas.drawCircle(point, 30f, Colors.placeElementMain)
//
//        Paint().use {
//            it.color = Colors.placeElementPlus
//            it.strokeWidth = 5f
//            it.strokeCap = PaintStrokeCap.ROUND
//            canvas.drawLine(x - 18f, y, x + 18f, y, it)
//            canvas.drawLine(x, y - 18f, x, y + 18f, it)
//        }
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