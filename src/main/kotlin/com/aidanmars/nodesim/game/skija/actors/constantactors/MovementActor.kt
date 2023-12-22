package com.aidanmars.nodesim.game.skija.actors.constantactors

import com.aidanmars.nodesim.game.skija.actors.ConstantActor
import com.aidanmars.nodesim.game.skija.core.NodeSimData
import com.aidanmars.nodesim.game.skija.types.WorldLocation

class MovementActor(override val data: NodeSimData) : ConstantActor {
    override fun act(timePassedMillis: Int) {
        var playerXOffset = 0f
        var playerYOffset = 0f
        if (data.wasdKeysPressed[0]) playerYOffset -= (0.625f / data.scale * timePassedMillis).toInt()
        if (data.wasdKeysPressed[1]) playerXOffset -= (0.625f / data.scale * timePassedMillis).toInt()
        if (data.wasdKeysPressed[2]) playerYOffset += (0.625f / data.scale * timePassedMillis).toInt()
        if (data.wasdKeysPressed[3]) playerXOffset += (0.625f / data.scale * timePassedMillis).toInt()
        data.playerLocation = WorldLocation(
            (data.playerX + playerXOffset).toInt(),
            (data.playerY + playerYOffset).toInt()
        )
    }
}