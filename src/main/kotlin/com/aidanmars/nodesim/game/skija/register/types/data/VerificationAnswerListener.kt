package com.aidanmars.nodesim.game.skija.register.types.data

interface VerificationAnswerListener : DataListener {
    /**
     * @return true to block this event from going down the chain
     */
    fun onAnswer(answer: Boolean): Boolean
}