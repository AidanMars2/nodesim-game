package com.aidanmars.nodesim.game.skija.listeners

interface ScrollListener : Listener {
    /**
     * @return true if the event should not be passed further down the chain
     */
    fun onScroll(xOffset: Double, yOffset: Double): Boolean
}