package com.aidanmars.nodesim.game.skija.core.registers

import com.aidanmars.nodesim.game.skija.register.types.actors.ConstantActor

class NodeSimActorHandler {
    private var lastActCallDate: Long = 0L
    private val constantActors = mutableListOf<ConstantActor>()

    fun register(actor: ConstantActor) {
        constantActors.add(actor)
    }

    fun act() {
        val currentTime = System.currentTimeMillis()
        val millisPassed = (currentTime - lastActCallDate).toInt()
        constantActors.forEach {
            it.act(millisPassed)
        }
        lastActCallDate = currentTime
    }
}