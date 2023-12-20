package com.aidanmars.nodesim.game.skija.hud.elements.nodes

import com.aidanmars.nodesim.core.NodeType
import com.aidanmars.nodesim.game.skija.*
import com.aidanmars.nodesim.game.skija.constants.Colors
import com.aidanmars.nodesim.game.skija.constants.SvgDoms
import com.aidanmars.nodesim.game.skija.hud.HudElement
import io.github.humbleui.skija.Canvas
import io.github.humbleui.types.Point

class AndGateHudElement : HudElement {
    override var isHidden: Boolean = false
    override var isFocused: Boolean = false

    override fun draw(window: NodeSimWindow, canvas: Canvas) {
        val point = getButtonPoint(window)
        if (window.data.currentPlaceType === NodeType.AndGate) {
            canvas.drawCircle(point, 35f, Colors.toolbarElementBorderSelected)
        }
        canvas.drawSvg(SvgDoms.Nodes.andGateOn, point, Point(60f, 60f))
    }

    override fun onClick(window: NodeSimWindow, mouseLocation: Point): Boolean {
        if (getButtonPoint(window).distance(mouseLocation) > 30f) return false
        window.data.currentPlaceType = NodeType.AndGate
        return true
    }

    private fun getButtonPoint(window: NodeSimWindow): Point {
        return Point(300f, window.height - 140f)
    }

    override fun onKeyEvent(window: NodeSimWindow, key: Int, mods: Int): Boolean {
        isFocused = false
        return false
    }
}