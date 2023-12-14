package com.aidanmars.nodesim.game.skija

import io.github.humbleui.skija.Canvas
import io.github.humbleui.skija.Paint
import io.github.humbleui.skija.Shader
import io.github.humbleui.types.Point
import kotlin.math.PI
import kotlin.math.atan

fun Canvas.drawCircle(
    center: Point, radius: Float,
    centerColor: Int, edgeColor: Int = centerColor
) {
    val (centerX, centerY) = center
    Paint().use {
        it.shader = Shader.makeRadialGradient(center, radius, intArrayOf(centerColor, edgeColor))
        drawCircle(centerX, centerY, radius, it)
    }
}

fun Canvas.drawCircle(
    center: Point, radius: Float, color: Int
): Canvas {
    return Paint().use {
        it.color = color
        drawCircle(center.x, center.y, radius, it)
    }
}

fun angleBetweenPoints(point1: Point, point2: Point): Float {
    val dx = point1.x - point2.x
    val dy = point1.y - point2.y
    return atan(dx / dy) + if (dy > 0) PI.toFloat() else 0f
}