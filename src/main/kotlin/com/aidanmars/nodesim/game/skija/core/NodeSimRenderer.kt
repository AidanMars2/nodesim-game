package com.aidanmars.nodesim.game.skija.core

import com.aidanmars.nodesim.game.skija.actors.DrawAble
import com.aidanmars.nodesim.game.skija.actors.drawables.BackGround
import com.aidanmars.nodesim.game.skija.actors.drawables.NodesDrawAble
import io.github.humbleui.skija.Canvas

class NodeSimRenderer {
    val drawAbles = mutableListOf<DrawAble>()
    private var lastDrawCallDate = 0L

    fun init(data: NodeSimData) {
        initActors(data)
        lastDrawCallDate = System.currentTimeMillis()
    }

    private fun initActors(data: NodeSimData) {
        drawAbles.add(BackGround(data))
        drawAbles.add(NodesDrawAble(data))

        // register drawables here
    }

    fun draw(canvas: Canvas) {
        drawAbles.forEach {
            it.draw(canvas)
        }
    }
}