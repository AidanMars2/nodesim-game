package com.aidanmars.nodesim.game.skija.register

import com.aidanmars.nodesim.core.extensions.getNodesInRegion
import com.aidanmars.nodesim.game.skija.core.NodeSimData
import com.aidanmars.nodesim.game.skija.register.types.actors.ConstantActor
import com.aidanmars.nodesim.game.skija.register.types.data.PlayerMovementListener
import com.aidanmars.nodesim.game.skija.register.types.input.KeyListener
import com.aidanmars.nodesim.game.skija.register.types.input.ScrollListener
import com.aidanmars.nodesim.game.skija.types.WorldLocation
import org.lwjgl.glfw.GLFW
import kotlin.math.pow

class MovementHandler(override val data: NodeSimData) :
    ConstantActor, ScrollListener, KeyListener
{
    private val wasdKeysPressed = BooleanArray(4)
    override fun act(timePassedMillis: Int) {
        var playerXOffset = 0f
        var playerYOffset = 0f
        if (wasdKeysPressed[0]) playerYOffset -= (0.625f / data.scale * timePassedMillis).toInt()
        if (wasdKeysPressed[1]) playerXOffset -= (0.625f / data.scale * timePassedMillis).toInt()
        if (wasdKeysPressed[2]) playerYOffset += (0.625f / data.scale * timePassedMillis).toInt()
        if (wasdKeysPressed[3]) playerXOffset += (0.625f / data.scale * timePassedMillis).toInt()
        data.playerLocation = WorldLocation(
            (data.playerX + playerXOffset).toInt(),
            (data.playerY + playerYOffset).toInt()
        )
    }

    override fun onKeyPress(key: Int, mods: Int): Boolean {
        when (key) {
            GLFW.GLFW_KEY_W -> wasdKeysPressed[0] = true
            GLFW.GLFW_KEY_A -> wasdKeysPressed[1] = true
            GLFW.GLFW_KEY_S -> wasdKeysPressed[2] = true
            GLFW.GLFW_KEY_D -> wasdKeysPressed[3] = true
            else -> return false
        }
        return true
    }

    override fun onKeyRelease(key: Int, mods: Int): Boolean {
        when (key) {
            GLFW.GLFW_KEY_W -> wasdKeysPressed[0] = false
            GLFW.GLFW_KEY_A -> wasdKeysPressed[1] = false
            GLFW.GLFW_KEY_S -> wasdKeysPressed[2] = false
            GLFW.GLFW_KEY_D -> wasdKeysPressed[3] = false
            else -> return false
        }
        return true
    }

    override fun onScroll(xOffset: Double, yOffset: Double): Boolean {
        val scaleDifference = 1.1.pow(yOffset)
        data.scale *= scaleDifference.toFloat()
        data.scale = data.scale.coerceAtMost(10f)
        data.scale = data.scale.coerceAtLeast(0.1f)
        return false
    }
}