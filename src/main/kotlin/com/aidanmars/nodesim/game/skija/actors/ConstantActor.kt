package com.aidanmars.nodesim.game.skija.actors

interface ConstantActor : Actor {
    /**
     * @param timePassedMillis the amount of time passed since this method was last called
     */
    fun act(timePassedMillis: Int)
}