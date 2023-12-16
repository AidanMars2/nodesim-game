package com.aidanmars.nodesim.game.skija

import kotlin.math.sqrt

data class WorldLocation(val x: Int, val y: Int) {
    fun distanceTo(other: WorldLocation): Double {
        val dx = x - other.x
        val dy = y - other.y
        return sqrt((dx * dx + dy * dy).toDouble())
    }
}
