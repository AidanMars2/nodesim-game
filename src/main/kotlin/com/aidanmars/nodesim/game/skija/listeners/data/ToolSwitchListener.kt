package com.aidanmars.nodesim.game.skija.listeners.data

import com.aidanmars.nodesim.game.skija.listeners.DataListener

interface ToolSwitchListener : DataListener {
    fun onToolSwitch(): Boolean
}