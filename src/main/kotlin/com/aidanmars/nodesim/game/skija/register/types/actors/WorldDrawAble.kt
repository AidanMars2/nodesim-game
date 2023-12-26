package com.aidanmars.nodesim.game.skija.register.types.actors

import io.github.humbleui.skija.Canvas

interface WorldDrawAble : DrawAble {
    /**
     * draws anything directly within the world
     * @param canvas the canvas to draw on
     */
    fun drawWorld(canvas: Canvas)
}