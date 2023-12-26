package com.aidanmars.nodesim.game.skija.register.types.actors

import io.github.humbleui.skija.Canvas

interface HudDrawAble : DrawAble {
    /**
     * draws anything that is contained within the general hud
     * @param canvas the canvas to draw on
     */
    fun drawHud(canvas: Canvas)
}