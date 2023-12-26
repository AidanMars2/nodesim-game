package com.aidanmars.nodesim.game.skija.register.types.actors

import io.github.humbleui.skija.Canvas

interface OverlayDrawAble : DrawAble {
    /**
     * draws anything that must be above the general hud
     * @param canvas the canvas to draw on
     */
    fun drawOverlay(canvas: Canvas)
}