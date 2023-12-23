package com.aidanmars.nodesim.game.skija.register.world

import com.aidanmars.nodesim.game.skija.constants.Colors
import com.aidanmars.nodesim.game.skija.angleBetweenPoints
import io.github.humbleui.skija.Canvas
import io.github.humbleui.skija.Paint
import io.github.humbleui.skija.PaintStrokeCap
import io.github.humbleui.skija.Path
import io.github.humbleui.types.Point
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

private const val LineEdgeWidth = 7f
private const val LineWidth = 3f

fun drawWire(
    from: Point, to: Point,
    power: Boolean, scale: Float,
    canvas: Canvas
) {
    Paint().use {
        it.strokeWidth = LineEdgeWidth * scale
        it.color = Colors.wireStructure
        it.strokeCap = PaintStrokeCap.ROUND
        canvas.drawLine(from.x, from.y, to.x, to.y, it)
    }
    drawWireTriangle(from, to, power, scale, canvas)
    Paint().use {
        it.strokeWidth = LineWidth * scale
        it.strokeCap = PaintStrokeCap.ROUND
        it.color = if (power) Colors.wireOnInner else Colors.wireOffInner
        canvas.drawLine(from.x, from.y, to.x, to.y, it)
    }
}

private const val triangleAngle = (PI / 4) * 3
private const val outerTriangleDistance = 12f
private const val innerTriangleDistance = 9f

private fun drawWireTriangle(
    from: Point, to: Point,
    power: Boolean, scale: Float,
    canvas: Canvas
) {
    val triangleLocation = from.middle(to)
    val angle = angleBetweenPoints(from, to)
    val mainSin = sin(angle) * scale
    val mainCos = cos(angle) * scale
    val firstBackCornerSin = sin(angle + triangleAngle).toFloat() * scale
    val firstBackCornerCos = cos(angle + triangleAngle).toFloat() * scale
    val secondBackCornerSin = sin(angle - triangleAngle).toFloat() * scale
    val secondBackCornerCos = cos(angle - triangleAngle).toFloat() * scale

    val drawSingleTriangle = { distance: Float, color: Int ->
        val mainPoint = Point(
            mainCos * distance + triangleLocation.x,
            mainSin * distance + triangleLocation.y
        )
        val firstBackPoint = Point(
            firstBackCornerCos * distance + triangleLocation.x,
            firstBackCornerSin * distance + triangleLocation.y
        )
        val secondBackPoint = Point(
            secondBackCornerCos * distance + triangleLocation.x,
            secondBackCornerSin * distance + triangleLocation.y
        )
        Paint().use {
            it.color = color
            canvas.drawPath(
                Path().moveTo(mainPoint)
                    .lineTo(firstBackPoint)
                    .lineTo(secondBackPoint)
                    .closePath(),
                it
            )
        }
    }
    drawSingleTriangle(outerTriangleDistance, Colors.wireStructure)
    drawSingleTriangle(innerTriangleDistance, if (power) Colors.wireOnInner else Colors.wireOffInner)
}

private fun Point.middle(other: Point): Point {
    val dx = x - other.x
    val dy = y - other.y
    return Point(x - (dx / 2), y - (dy / 2))
}
