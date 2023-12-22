package com.aidanmars.nodesim.game.skija.actors.constantactors

import com.aidanmars.nodesim.game.skija.actors.ConstantActor
import com.aidanmars.nodesim.game.skija.core.NodeSimData

class MovementActor(override val data: NodeSimData) : ConstantActor {
    override fun act(timePassedMillis: Int) {
        if (data.wasdKeysPressed[0]) data.playerY -= (0.625f / data.scale * timePassedMillis).toInt()
        if (data.wasdKeysPressed[1]) data.playerX -= (0.625f / data.scale * timePassedMillis).toInt()
        if (data.wasdKeysPressed[2]) data.playerY += (0.625f / data.scale * timePassedMillis).toInt()
        if (data.wasdKeysPressed[3]) data.playerX += (0.625f / data.scale * timePassedMillis).toInt()
    }
}