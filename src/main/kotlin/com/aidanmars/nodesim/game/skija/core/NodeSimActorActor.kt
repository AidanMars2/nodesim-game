package com.aidanmars.nodesim.game.skija.core

import com.aidanmars.nodesim.game.skija.actors.ConstantActor

class NodeSimActorActor {
    private var lastActCallDate: Long = 0L
    val constantActors = mutableListOf<ConstantActor>()

    fun init(data: NodeSimData) {
        lastActCallDate = System.currentTimeMillis()
        //TODO: make actors
    }

    fun act() {
        val currentTime = System.currentTimeMillis()
        constantActors.forEach {
            it.act((currentTime - lastActCallDate).toInt())
        }
        lastActCallDate = currentTime
    }
}