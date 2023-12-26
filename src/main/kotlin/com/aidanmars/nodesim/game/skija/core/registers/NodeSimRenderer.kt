package com.aidanmars.nodesim.game.skija.core.registers

import com.aidanmars.nodesim.game.skija.register.types.actors.DrawAble
import com.aidanmars.nodesim.game.skija.register.types.actors.HudDrawAble
import com.aidanmars.nodesim.game.skija.register.types.actors.OverlayDrawAble
import com.aidanmars.nodesim.game.skija.register.types.actors.WorldDrawAble
import io.github.humbleui.skija.Canvas

class NodeSimRenderer {
    private val worldDrawAbles = mutableListOf<WorldDrawAble>()
    private val hudDrawAbles = mutableListOf<HudDrawAble>()
    private val overlayDrawAbles = mutableListOf<OverlayDrawAble>()

    fun register(drawAble: DrawAble) {
        if (drawAble is WorldDrawAble) worldDrawAbles.add(drawAble)
        if (drawAble is HudDrawAble) hudDrawAbles.add(drawAble)
        if (drawAble is OverlayDrawAble) overlayDrawAbles.add(drawAble)
    }

    fun draw(canvas: Canvas) {
        for (index in worldDrawAbles.indices.reversed()) {
            worldDrawAbles[index].drawWorld(canvas)
        }
        for (index in hudDrawAbles.indices.reversed()) {
            hudDrawAbles[index].drawHud(canvas)
        }
        for (index in overlayDrawAbles.indices.reversed()) {
            overlayDrawAbles[index].drawOverlay(canvas)
        }
    }
}