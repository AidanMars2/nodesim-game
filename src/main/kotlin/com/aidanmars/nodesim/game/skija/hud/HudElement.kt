package com.aidanmars.nodesim.game.skija.hud

import com.aidanmars.nodesim.game.skija.NodeSimWindow
import io.github.humbleui.skija.Canvas
import io.github.humbleui.types.Point

interface HudElement {
    var isHidden: Boolean

    var isFocused: Boolean

    fun draw(window: NodeSimWindow, canvas: Canvas)

    /**
     * @return true if the event should not be passed on to another element
     */
    fun onClick(window: NodeSimWindow, mouseLocation: Point): Boolean

    /**
     * only passed to this element if it is focused
     * @return false if the event should be passed to another element
     */
    fun onKeyEvent(window: NodeSimWindow, key: Int, mods: Int): Boolean
}