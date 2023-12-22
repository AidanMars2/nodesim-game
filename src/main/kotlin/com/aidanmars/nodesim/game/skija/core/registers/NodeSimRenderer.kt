package com.aidanmars.nodesim.game.skija.core.registers

import com.aidanmars.nodesim.game.skija.register.types.actors.DrawAble
import io.github.humbleui.skija.Canvas

class NodeSimRenderer {
    private val drawAbles = mutableListOf<DrawAble>()

    fun register(drawAble: DrawAble) {
        drawAbles.add(drawAble)
    }

    fun draw(canvas: Canvas) {
        drawAbles.forEach {
            it.draw(canvas)
        }
    }
}