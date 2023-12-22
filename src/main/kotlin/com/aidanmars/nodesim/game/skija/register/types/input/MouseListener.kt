package com.aidanmars.nodesim.game.skija.register.types.input

import io.github.humbleui.types.Point

interface MouseListener : InputListener {

    /**
     * called when a click occurs on screen
     *
     * if this function returns true. This event will not continue down the list of listeners
     * and the [onDrag] function will be called on only this listener until the [onRelease] function is called once,
     * then other listeners may accept events again.
     */
    fun onPress(clickLocation: Point): Boolean

    fun onRelease(clickLocation: Point)

    fun onDrag(newLocation: Point)
}