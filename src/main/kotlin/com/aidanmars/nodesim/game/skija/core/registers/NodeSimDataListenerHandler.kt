package com.aidanmars.nodesim.game.skija.core.registers

import com.aidanmars.nodesim.game.skija.register.types.data.*

class NodeSimDataListenerHandler {
    private val movementListeners = mutableListOf<PlayerMovementListener>()
    private val toolSwitchListeners = mutableListOf<ToolSwitchListener>()
    private val verificationAnswerListeners = mutableListOf<VerificationAnswerListener>()

    fun register(listener: DataListener) {
        if (listener is PlayerMovementListener) movementListeners.add(listener)
        if (listener is ToolSwitchListener) toolSwitchListeners.add(listener)
        if (listener is VerificationAnswerListener) verificationAnswerListeners.add(listener)
    }

    fun onPlayerMove() {
        movementListeners.forEach { it.onPlayerMove() }
    }

    fun onToolSwitch() {
        toolSwitchListeners.forEach { it.onToolSwitch() }
    }

    fun onYesNoAnswer(answer: Boolean) {
        verificationAnswerListeners.forEach {
            if (it.onAnswer(answer)) return
        }
    }
}