package com.aidanmars.nodesim.game.skija

import kotlin.math.sqrt

data class Point(val x: Float, val y: Float) {
    fun distance(other: Point): Float {
        val dx = other.x - x
        val dy = other.y - y
        return sqrt(dx * dx + dy * dy)
    }
}
