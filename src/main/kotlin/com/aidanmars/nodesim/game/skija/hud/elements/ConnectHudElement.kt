package com.aidanmars.nodesim.game.skija.hud.elements

import com.aidanmars.nodesim.game.skija.*
import com.aidanmars.nodesim.game.skija.hud.HudElement
import io.github.humbleui.skija.Canvas
import io.github.humbleui.types.Point

class ConnectHudElement : HudElement {
    private companion object {
        val elementDom = getSvgFromResource("hud/connectElement.svg")
    }
    override var isHidden: Boolean = false
    override var isFocused: Boolean = false

    override fun draw(window: NodeSimWindow, canvas: Canvas) {
        val point = getConnectElementPoint(window)
        val (x, y) = point
        if (window.data.currentTool == ToolType.Connect) {
            canvas.drawCircle(point, 40f, Colors.toolbarElementBorderSelected)
        }
        canvas.drawSvg(elementDom, Point(x - 35f, y - 35f), Point(70f, 70f))
//        canvas.drawCircle(point, 35f, Colors.connectElementBorder)
//        canvas.drawCircle(point, 30f, Colors.connectElementMain)
//
//        val circlePoint1 = Point(x + 11f, y - 11f)
//        val circlePoint2 = Point(x - 11f, y + 11f)
//        canvas.drawCircle(circlePoint1, 9f, Colors.connectElementIcon)
//        canvas.drawCircle(circlePoint2, 9f, Colors.connectElementIcon)
//        canvas.drawCircle(circlePoint1, 5f, Colors.connectElementMain)
//        canvas.drawCircle(circlePoint2, 5f, Colors.connectElementMain)
//        Paint().use {
//            it.color = Colors.connectElementIcon
//            it.strokeCap = PaintStrokeCap.ROUND
//            it.strokeWidth = 4f
//            canvas.drawLine(circlePoint1.x, circlePoint1.y, circlePoint2.x, circlePoint2.y, it)
//        }
    }

    override fun onClick(window: NodeSimWindow, mouseLocation: Point): Boolean {
        if (mouseLocation.distance(getConnectElementPoint(window)) > 35f) return false
        window.data.currentTool = ToolType.Connect
        return true
    }

    override fun onKeyEvent(window: NodeSimWindow, key: Int, mods: Int): Boolean {
        isFocused = false
        return false
    }

    private fun getConnectElementPoint(window: NodeSimWindow): Point {
        return Point(300f, window.height - 60f)
    }
}