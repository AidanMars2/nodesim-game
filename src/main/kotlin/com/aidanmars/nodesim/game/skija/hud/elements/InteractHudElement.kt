package com.aidanmars.nodesim.game.skija.hud.elements

import com.aidanmars.nodesim.game.skija.constants.Colors
import com.aidanmars.nodesim.game.skija.core.NodeSimWindow
import com.aidanmars.nodesim.game.skija.types.ToolType
import com.aidanmars.nodesim.game.skija.*
import com.aidanmars.nodesim.game.skija.constants.SvgDoms
import com.aidanmars.nodesim.game.skija.hud.HudElement
import io.github.humbleui.skija.*
import io.github.humbleui.types.Point

class InteractHudElement : HudElement {
    override var shouldDraw: Boolean = false
    override var isFocused: Boolean = false

    override fun draw(window: NodeSimWindow, canvas: Canvas) {
        val point = getButtonPoint(window)
        val (x, y) = point
        if (window.data.currentTool == ToolType.Interact) {
            canvas.drawCircle(point, 40f, Colors.toolbarElementBorderSelected)
        }
        canvas.drawSvg(SvgDoms.Hud.interactElement, Point(x - 35f, y - 35f), Point(70f, 70f))
//        canvas.drawCircle(point, 35f, Colors.interactElementBorder)
//        canvas.drawCircle(point, 30f, Colors.interactElementMain)
//
//        Paint().use {
//            it.color = Colors.interactElementIcon
//            canvas.drawCircle(x, y, 17f, it)
//        }
//        val point1 = Point(x + 3f, y + 4f)
//        val point2 = Point(x + 6f, y + 21f)
//        val point3 = Point(x + 16f, y + 15.5f)
//        Paint().use {
//            it.color = Colors.interactElementMain
//            it.strokeWidth = 8f
//            it.strokeCap = PaintStrokeCap.ROUND
//            it.strokeJoin = PaintStrokeJoin.ROUND
//            it.setStroke(true)
//            canvas.drawPath(
//                Path().moveTo(point1).lineTo(point2).lineTo(point3).closePath(),
//                it
//            )
//        }
//        Paint().use {
//            it.color = Colors.interactElementIcon
//            canvas.drawPath(
//                Path().moveTo(point1).lineTo(point2).lineTo(point3).closePath(),
//                it
//            )
//        }
    }

    override fun onClick(window: NodeSimWindow, mouseLocation: Point): Boolean {
        if (mouseLocation.distance(getButtonPoint(window)) > 35f) return false
        window.data.currentTool = ToolType.Interact
        return true
    }

    override fun onKeyEvent(window: NodeSimWindow, key: Int, mods: Int): Boolean {
        isFocused = false
        return false
    }

    private fun getButtonPoint(window: NodeSimWindow): Point {
        return Point(220f, window.height - 60f)
    }
}