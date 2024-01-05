package com.aidanmars.nodesim.game.skija.register.world

import com.aidanmars.nodesim.game.skija.angleBetweenPoints
import com.aidanmars.nodesim.game.skija.constants.Colors
import com.aidanmars.nodesim.game.skija.middle
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
private const val triangleAngle = (PI / 4) * 3
private const val outerTriangleDistance = 12f
private const val innerTriangleDistance = 9f

class WireDrawHelper(
    private val scale: Float
) {

    private val bottomLayerLines = mutableListOf<Float>()
    private val bottomLayerTriangles = mutableListOf<Point>()
    private val onLayerLines = mutableListOf<Float>()
    private val onLayerTriangles = mutableListOf<Point>()
    private val offLayerLines = mutableListOf<Float>()
    private val offLayerTriangles = mutableListOf<Point>()

    fun drawWire(
        from: Point, to: Point,
        power: Boolean
    ) {
        bottomLayerLines.drawLine(from, to)
        drawWireTriangle(from, to, power)
        if (power) {
            onLayerLines.drawLine(from, to)
        } else {
            offLayerLines.drawLine(from, to)
        }
    }

    private fun drawWireTriangle(
        from: Point, to: Point,
        power: Boolean
    ) {
        val triangleLocation = from.middle(to)
        val angle = angleBetweenPoints(from, to)
        val mainSin = sin(angle) * scale
        val mainCos = cos(angle) * scale
        val firstBackCornerSin = sin(angle + triangleAngle).toFloat() * scale
        val firstBackCornerCos = cos(angle + triangleAngle).toFloat() * scale
        val secondBackCornerSin = sin(angle - triangleAngle).toFloat() * scale
        val secondBackCornerCos = cos(angle - triangleAngle).toFloat() * scale

        val drawSingleTriangle = { distance: Float, list: MutableList<Point> ->
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
            list.add(mainPoint)
            list.add(firstBackPoint)
            list.add(secondBackPoint)
        }

        drawSingleTriangle(outerTriangleDistance, bottomLayerTriangles)
        drawSingleTriangle(innerTriangleDistance, if (power) onLayerTriangles else offLayerTriangles)
    }

    private fun MutableList<Float>.drawLine(from: Point, to: Point) {
        add(from.x)
        add(from.y)
        add(to.x)
        add(to.y)
    }

    fun flush(canvas: Canvas) {
        Paint().use {
            it.color = Colors.wireStructure
            it.strokeCap = PaintStrokeCap.ROUND
            it.strokeWidth = LineEdgeWidth * scale
            canvas.drawLines(bottomLayerLines.toFloatArray(), it)
        }
        Paint().use {
            it.color = Colors.wireStructure
            canvas.drawTriangles(bottomLayerTriangles.toTypedArray(), null, it)
        }

        Paint().use {
            it.color = Colors.wireOnInner
            canvas.drawTriangles(onLayerTriangles.toTypedArray(), null, it)
        }
        Paint().use {
            it.color = Colors.wireOnInner
            it.strokeCap = PaintStrokeCap.ROUND
            it.strokeWidth = LineWidth * scale
            canvas.drawLines(onLayerLines.toFloatArray(), it)
        }

        Paint().use {
            it.color = Colors.wireOffInner
            canvas.drawTriangles(onLayerTriangles.toTypedArray(), null, it)
        }
        Paint().use {
            it.color = Colors.wireOffInner
            it.strokeCap = PaintStrokeCap.ROUND
            it.strokeWidth = LineWidth * scale
            canvas.drawLines(offLayerLines.toFloatArray(), it)
        }
    }
}