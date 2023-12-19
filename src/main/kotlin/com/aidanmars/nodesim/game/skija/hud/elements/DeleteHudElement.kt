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

class DeleteHudElement : HudElement {
    private companion object {
        val elementDom = getSvgFromResource("hud/deleteElement.svg")
    }
    override var isHidden: Boolean = false
    override var isFocused: Boolean = false

    override fun draw(window: NodeSimWindow, canvas: Canvas) {
        val point = getButtonPoint(window)
        val (x, y) = point
        if (window.data.currentTool == ToolType.Delete) {
            canvas.drawCircle(point, 40f, Colors.toolbarElementBorderSelected)
        }
        canvas.drawSvg(elementDom, Point(x - 35f, y - 35f), Point(70f, 70f))
//        canvas.drawCircle(point, 35f, Colors.deleteElementBorder)
//        canvas.drawCircle(point, 30f, Colors.deleteElementMain)
//
//        Paint().use {
//            it.color = Colors.deleteElementCross
//            it.strokeWidth = 5f
//            it.strokeCap = PaintStrokeCap.ROUND
//            val xMin = x - 15f
//            val yMin = y - 15f
//            val xMax = x + 15f
//            val yMax = y + 15f
//            canvas.drawLine(xMin, yMin, xMax, yMax, it)
//            canvas.drawLine(xMin, yMax, xMax, yMin, it)
//        }
    }

    override fun onClick(window: NodeSimWindow, mouseLocation: Point): Boolean {
        if (mouseLocation.distance(getButtonPoint(window)) > 35f) return false
        window.data.currentTool = ToolType.Delete
        return true
    }

    override fun onKeyEvent(window: NodeSimWindow, key: Int, mods: Int): Boolean {
        isFocused = false
        return false
    }

    private fun getButtonPoint(window: NodeSimWindow): Point {
        return Point(140f, window.height - 60f)
    }
}