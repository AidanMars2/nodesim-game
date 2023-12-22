package com.aidanmars.nodesim.game.skija.listeners

interface KeyListener : Listener {
    /**
     * keep in mind that this function may be called for the same key before said key has been released.
     * @param key the key pressed.
     * @param mods the modification keys pressed (ctrl, shift, alt, etc.).
     * @return true if the event should not be passed down the chain of listeners
     */
    fun onKeyPress(key: Int, mods: Int): Boolean

    /**
     * keep in mind that the release function may not be called after a key press.
     * @param key the key released.
     * @param mods the modification keys pressed (ctrl, shift, alt, etc.).
     * @return true if the event should not be passed down the chain of listeners
     */
    fun onKeyRelease(key: Int, mods: Int): Boolean
}