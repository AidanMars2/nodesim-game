package com.aidanmars.nodesim.game.skija.hud.elements

import com.aidanmars.nodesim.game.skija.*
import com.aidanmars.nodesim.game.skija.constants.Colors
import com.aidanmars.nodesim.game.skija.core.NodeSimWindow
import com.aidanmars.nodesim.game.skija.hud.HudElement
import io.github.humbleui.skija.Canvas
import io.github.humbleui.skija.Paint
import io.github.humbleui.skija.PaintStrokeCap
import io.github.humbleui.types.Point

class ExitHudElement : HudElement {
    override var shouldDraw: Boolean = false
    override var isFocused: Boolean = false

    override fun draw(window: NodeSimWindow, canvas: Canvas) {
        val (x, y) = getButtonPoint(window)
        Paint().use {
            it.color = Colors.exitElementMain
            it.strokeWidth = 10f
            it.strokeCap = PaintStrokeCap.ROUND
            canvas.drawLine(x + 40f, y + 40f, x - 40f, y - 40f, it)
            canvas.drawLine(x + 40f, y - 40f, x - 40f, y + 40f, it)
        }
    }

    override fun onClick(window: NodeSimWindow, mouseLocation: Point): Boolean {
        if (mouseLocation.distance(getButtonPoint(window)) > 60f) return false
//        window.closeGraceFully()
        return true
    }

    override fun onKeyEvent(window: NodeSimWindow, key: Int, mods: Int): Boolean {
        isFocused = false
        return false
    }

    private fun getButtonPoint(window: NodeSimWindow): Point {
        return Point(window.width - 60f, 60f)
    }
}
