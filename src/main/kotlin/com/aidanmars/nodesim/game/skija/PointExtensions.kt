package com.aidanmars.nodesim.game.skija

import com.aidanmars.nodesim.core.Node
import io.github.humbleui.types.Point
import kotlin.math.sqrt

fun Point.distance(other: Point): Float {
    val dx = x - other.x
    val dy = y - other.y
    return sqrt(dx * dx + dy * dy)
}

operator fun Point.component1(): Float = x

operator fun Point.component2(): Float = y

fun calculateClosestPointToLine(
    point: WorldLocation,
    linePoint1: WorldLocation, linePoint2: WorldLocation
): WorldLocation {
    val lineSlope = (linePoint1.y - linePoint2.y).toFloat() / (linePoint1.x - linePoint2.x)
    val perpendicular = -1 / lineSlope
    var closestLineX = (((
            ((lineSlope * linePoint1.x) - linePoint1.y) -
                    (perpendicular * point.x)) + point.y) / (lineSlope - perpendicular)).toInt()
    var closestLineY = ((lineSlope * (closestLineX - linePoint1.x)) + linePoint1.y).toInt()
    when {
        lineSlope == 0F -> {
            closestLineX = point.x
            closestLineY = linePoint1.y
        }
        (1 / lineSlope) == 0F -> {
            closestLineX = linePoint1.x
            closestLineY = point.y
        }
    }
    val p1RightOfP2 = linePoint1.x > linePoint2.x
    val cPoint = if (p1RightOfP2) linePoint2 else linePoint1
    val dPoint = if (p1RightOfP2) linePoint1 else linePoint2
    if (closestLineX > dPoint.x) {
        closestLineX = dPoint.x
        closestLineY = dPoint.y
    }
    if (closestLineX < cPoint.x) {
        closestLineX = cPoint.x
        closestLineY = cPoint.y
    }
    return WorldLocation(closestLineX, closestLineY)
}

fun distanceToLine(
    point: WorldLocation,
    linePoint1: WorldLocation, linePoint2: WorldLocation
): Float = calculateClosestPointToLine(point, linePoint1, linePoint2).distanceTo(point).toFloat()

fun Node.location(): WorldLocation = WorldLocation(x, y)