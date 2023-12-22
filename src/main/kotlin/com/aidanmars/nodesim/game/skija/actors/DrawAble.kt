package com.aidanmars.nodesim.game.skija.actors

import io.github.humbleui.skija.Canvas

interface DrawAble : Actor {
    fun draw(canvas: Canvas)
}