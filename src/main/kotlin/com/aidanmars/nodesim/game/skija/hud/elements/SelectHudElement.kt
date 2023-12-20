package com.aidanmars.nodesim.game.skija.hud.elements

import com.aidanmars.nodesim.game.skija.*
import com.aidanmars.nodesim.game.skija.constants.Colors
import com.aidanmars.nodesim.game.skija.constants.SvgDoms
import com.aidanmars.nodesim.game.skija.hud.HudElement
import io.github.humbleui.skija.Canvas
import io.github.humbleui.types.Point

class SelectHudElement : HudElement {
    override var isHidden: Boolean = false
    override var isFocused: Boolean = false

    override fun draw(window: NodeSimWindow, canvas: Canvas) {
        val point = getButtonPoint(window)
        val (x, y) = point
        if (window.data.currentTool == ToolType.Select) {
            canvas.drawCircle(point, 40f, Colors.toolbarElementBorderSelected)
        }
        canvas.drawSvg(SvgDoms.Hud.selectElement, Point(x - 35f, y - 35f), Point(70f, 70f))
    }

    override fun onClick(window: NodeSimWindow, mouseLocation: Point): Boolean {
        if (mouseLocation.distance(getButtonPoint(window)) > 35f) return false
        window.data.currentTool = ToolType.Select
        return true
    }

    override fun onKeyEvent(window: NodeSimWindow, key: Int, mods: Int): Boolean {
        isFocused = false
        return false
    }

    private fun getButtonPoint(window: NodeSimWindow): Point {
        return Point(380f, window.height - 60f)
    }
}