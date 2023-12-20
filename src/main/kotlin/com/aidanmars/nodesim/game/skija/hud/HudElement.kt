package com.aidanmars.nodesim.game.skija.hud

import com.aidanmars.nodesim.game.skija.NodeSimWindow
import io.github.humbleui.skija.Canvas
import io.github.humbleui.types.Point

interface HudElement {
    /**
     * true if this element should not be drawn
     */
    var isHidden: Boolean

    /**
     * true if this element wants to get key events
     */
    var isFocused: Boolean

    fun draw(window: NodeSimWindow, canvas: Canvas)

    /**
     * @return false if the event should be passed to another element
     */
    fun onClick(window: NodeSimWindow, mouseLocation: Point): Boolean

    /**
     * only passed to this element if it is focused
     * @return false if the event should be passed to another element
     */
    fun onKeyEvent(window: NodeSimWindow, key: Int, mods: Int): Boolean
}