package com.aidanmars.nodesim.game.skija.register.types.input

interface ScrollListener : InputListener {
    /**
     * @return true if the event should not be passed further down the chain
     */
    fun onScroll(xOffset: Double, yOffset: Double): Boolean
}