package com.aidanmars.nodesim.game.skija.types

import com.aidanmars.nodesim.core.getChunk
import kotlin.math.sqrt

data class WorldLocation(val x: Int, val y: Int) {
    fun distanceTo(other: WorldLocation): Double {
        val dx = x - other.x
        val dy = y - other.y
        return sqrt((dx * dx + dy * dy).toDouble())
    }

    fun chunk(): WorldLocation {
        val (chunkX, chunkY) = getChunk(x, y)
        return WorldLocation(chunkX, chunkY)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as WorldLocation

        if (x != other.x) return false
        if (y != other.y) return false

        return true
    }

    override fun hashCode(): Int {
        var result = x
        result = 31 * result + y
        return result
    }


}
