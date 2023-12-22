package com.aidanmars.nodesim.game.skija.core

import com.aidanmars.nodesim.game.skija.listeners.DataListener
import com.aidanmars.nodesim.game.skija.listeners.data.NodesOnScreenMovementListener
import com.aidanmars.nodesim.game.skija.listeners.data.PlayerMovementListener
import com.aidanmars.nodesim.game.skija.listeners.data.ToolSwitchListener

class NodeSimDataListenerHandler {
    private val movementListeners = mutableListOf<PlayerMovementListener>()
    private val toolSwitchListeners = mutableListOf<ToolSwitchListener>()

    fun registerListener(listener: DataListener) {
        if (listener is PlayerMovementListener) movementListeners.add(listener)
        if (listener is ToolSwitchListener) toolSwitchListeners.add(listener)
    }

    fun onPlayerMove() {
        movementListeners.forEach { it.onPlayerMove() }
    }

    fun onToolSwitch() {
        toolSwitchListeners.forEach { it.onToolSwitch() }
    }

    fun initListeners(data: NodeSimData) {
        registerListener(NodesOnScreenMovementListener(data))
        //TODO: register listeners
    }
}