package com.aidanmars.nodesim.game.skija

import io.github.humbleui.skija.Canvas
import io.github.humbleui.skija.Data
import io.github.humbleui.skija.Paint
import io.github.humbleui.skija.Shader
import io.github.humbleui.skija.svg.SVGDOM
import io.github.humbleui.skija.svg.SVGLength
import io.github.humbleui.skija.svg.SVGPreserveAspectRatio
import io.github.humbleui.types.Point
import io.github.humbleui.types.Rect
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
    val dx = point2.x - point1.x
    val dy = point2.y - point1.y
    return atan(dy / dx) + if (dx < 0) PI.toFloat() else 0f
}

fun Canvas.drawSvg(dom: SVGDOM, point: Point, size: Point) {
    save()
    translate(point.x, point.y)
    clipRect(Rect.makeXYWH(0f, 0f, size.x, size.y))
    dom.root!!.use {
        it.setWidth(SVGLength(size.x))
            .setHeight(SVGLength(size.y))
            .setPreserveAspectRatio(SVGPreserveAspectRatio())
    }
    dom.render(this)
    restore()

    translate(0f, 0f)
}

fun getSvgFromResource(resource: String): SVGDOM = SVGDOM(
    Data.makeFromBytes(
        Thread.currentThread()
            .contextClassLoader
            .getResource(resource)!!
            .readBytes()
    )
)