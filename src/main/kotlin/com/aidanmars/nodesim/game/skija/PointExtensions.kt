package com.aidanmars.nodesim.game.skija

import io.github.humbleui.types.Point
import kotlin.math.sqrt

fun Point.distance(other: Point): Float {
    val dx = x - other.x
    val dy = y - other.y
    return sqrt(dx * dx + dy * dy)
}

operator fun Point.component1(): Float = x

operator fun Point.component2(): Float = y