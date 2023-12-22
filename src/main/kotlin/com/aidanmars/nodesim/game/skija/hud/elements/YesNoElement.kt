package com.aidanmars.nodesim.game.skija.hud.elements

import com.aidanmars.nodesim.game.skija.core.NodeSimWindow
import com.aidanmars.nodesim.game.skija.constants.SvgDoms
import com.aidanmars.nodesim.game.skija.drawSvg
import com.aidanmars.nodesim.game.skija.hud.HudElement
import io.github.humbleui.skija.Canvas
import io.github.humbleui.skija.Color
import io.github.humbleui.skija.Font
import io.github.humbleui.skija.Paint
import io.github.humbleui.types.Point
import io.github.humbleui.types.Rect

class YesNoElement : HudElement {
    override var shouldDraw: Boolean = true
        set(value) {
            field = value
            isFocused = !field
        }
    override var isFocused: Boolean = false
    var text = ""
    var yesCallBack: () -> Unit = {}
    var noCallBack: () -> Unit = {}

    override fun draw(window: NodeSimWindow, canvas: Canvas) {
        val point = getElementPoint(window)
        val width = window.width.toFloat() / 4f
        Paint().use {
            it.color = Color.makeARGB(128, 0, 0, 0)
            canvas.drawRect(Rect(0f, 0f, window.width.toFloat(), window.height.toFloat()), it)
        }
        canvas.drawSvg(SvgDoms.Hud.yesNoElement, point, Point(width, width / 2))
        Paint().use {
            val font = Font(null)
            val stringSize = font.getWidths(font.getStringGlyphs(text))
                .reduce { totalSize, thisSize -> totalSize + thisSize }
            canvas.drawString(text, (window.width / 2f) - stringSize / 2f, point.y + (width / 80f), font, it)
            font.close()
        }
    }

    override fun onClick(window: NodeSimWindow, mouseLocation: Point): Boolean {
        val popUpPoint = getElementPoint(window)
        val width = window.width.toFloat() / 4f
        val ratio = width / 200f
        val adjustedX = (mouseLocation.x - popUpPoint.x) / ratio
        val adjustedY = (mouseLocation.y - popUpPoint.y) / ratio
        if (adjustedX in 20f..80f && adjustedY in 60f..85f) yesCallBack()
        if (adjustedX in 120f..180f && adjustedY in 60f..85f) noCallBack()
        return true // block out any other input
    }

    override fun onKeyEvent(window: NodeSimWindow, key: Int, mods: Int): Boolean {
        return true // block out any other input
    }

    private fun getElementPoint(window: NodeSimWindow): Point {
        return Point(window.width.toFloat() / 4f, window.height.toFloat() / 4f)
    }
}