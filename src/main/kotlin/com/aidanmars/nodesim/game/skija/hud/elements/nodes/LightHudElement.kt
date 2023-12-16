package com.aidanmars.nodesim.game.skija.hud.elements.nodes

import com.aidanmars.nodesim.core.NodeType
import com.aidanmars.nodesim.game.skija.Colors
import com.aidanmars.nodesim.game.skija.NodeSimWindow
import com.aidanmars.nodesim.game.skija.distance
import com.aidanmars.nodesim.game.skija.drawCircle
import com.aidanmars.nodesim.game.skija.hud.HudElement
import com.aidanmars.nodesim.game.skija.world.Nodes
import io.github.humbleui.skija.Canvas
import io.github.humbleui.types.Point

class LightHudElement : HudElement {
    override var isHidden: Boolean = false
    override var isFocused: Boolean = false

    override fun draw(window: NodeSimWindow, canvas: Canvas) {
        if (window.data.currentPlaceType === NodeType.Light) {
            canvas.drawCircle(getButtonPoint(window), 35f, Colors.toolbarElementBorderSelected)
        }
        Nodes.drawLight(getButtonPoint(window), false, 1.5f, canvas)
    }

    override fun onClick(window: NodeSimWindow, mouseLocation: Point): Boolean {
        if (getButtonPoint(window).distance(mouseLocation) > 30f) return false
        window.data.currentPlaceType = NodeType.Light
        return true
    }

    private fun getButtonPoint(window: NodeSimWindow): Point {
        return Point(140f, window.height - 140f)
    }

    override fun onKeyEvent(window: NodeSimWindow, key: Int, mods: Int): Boolean {
        isFocused = false
        return false
    }
}