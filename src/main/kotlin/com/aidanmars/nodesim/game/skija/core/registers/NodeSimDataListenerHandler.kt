package com.aidanmars.nodesim.game.skija.core.registers

import com.aidanmars.nodesim.game.skija.register.types.data.DataListener
import com.aidanmars.nodesim.game.skija.register.types.data.PlayerMovementListener
import com.aidanmars.nodesim.game.skija.register.types.data.ToolSwitchListener

class NodeSimDataListenerHandler {
    private val movementListeners = mutableListOf<PlayerMovementListener>()
    private val toolSwitchListeners = mutableListOf<ToolSwitchListener>()

    fun register(listener: DataListener) {
        if (listener is PlayerMovementListener) movementListeners.add(listener)
        if (listener is ToolSwitchListener) toolSwitchListeners.add(listener)
    }

    fun onPlayerMove() {
        movementListeners.forEach { it.onPlayerMove() }
    }

    fun onToolSwitch() {
        toolSwitchListeners.forEach { it.onToolSwitch() }
    }
}