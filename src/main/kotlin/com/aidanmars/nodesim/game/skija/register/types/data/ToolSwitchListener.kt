package com.aidanmars.nodesim.game.skija.register.types.data

interface ToolSwitchListener : DataListener {
    fun onToolSwitch(): Boolean
}