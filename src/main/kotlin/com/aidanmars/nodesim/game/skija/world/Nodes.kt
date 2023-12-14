package com.aidanmars.nodesim.game.skija.world

import com.aidanmars.nodesim.game.skija.Colors
import com.aidanmars.nodesim.game.skija.*
import io.github.humbleui.skija.Canvas
import io.github.humbleui.skija.Paint
import io.github.humbleui.types.Point

object Nodes {
    private const val NodeGlowRadius = 27f
    private const val NodeEdge1Radius = 20f
    private const val NodeStructure1Radius = 18f
    private const val NodeEdge2Radius = 15f
    private const val NodeStructure2Radius = 13f
    private const val NodeEdge3Radius = 10f
    private const val NodeStructure3Radius = 8f
    private const val Sin45Degrees = 0.707106781f
    private const val XorLineOffSet = Sin45Degrees * NodeEdge2Radius + 0.05f
    private const val XorLineWidth = NodeStructure2Radius * 0.3f
    private const val XorLineEdgeWidth = XorLineWidth + 3f

    private fun drawNodeBase(center: Point, scale: Float, canvas: Canvas) {
        canvas.drawCircle(center, NodeEdge1Radius * scale, Colors.nodeEdge)
        canvas.drawCircle(center, NodeStructure1Radius * scale, Colors.nodeStructure)
    }

    private fun drawNodeBase2(center: Point, scale: Float, canvas: Canvas) {
        canvas.drawCircle(center, NodeEdge2Radius * scale, Colors.nodeEdge)
        canvas.drawCircle(center, NodeStructure2Radius * scale, Colors.nodeStructure)
    }

    private fun drawNodeBase3(center: Point, scale: Float, canvas: Canvas) {
        canvas.drawCircle(center, NodeEdge3Radius * scale, Colors.nodeEdge)
        canvas.drawCircle(center, NodeStructure3Radius * scale, Colors.nodeStructure)
    }

    private fun drawNodeGlow(
        shouldDraw: Boolean, center: Point,
        scale: Float, canvas: Canvas,
        innerColor: Int, outerColor: Int,) {
        if (shouldDraw) {
            canvas.drawCircle(center, NodeGlowRadius * scale, innerColor, outerColor)
        }
    }

    fun drawSwitch(center: Point, power: Boolean, scale: Float, canvas: Canvas) {
        drawNodeGlow(power, center, scale, canvas, Colors.switchOnGlowInner, Colors.switchOnGlowOuter)
        drawNodeBase(center, scale, canvas)

        canvas.drawCircle(center, NodeEdge2Radius * scale, Colors.nodeEdge)
        if (power) {
            canvas.drawCircle(center, NodeStructure2Radius * scale, Colors.switchOnInner, Colors.switchOnOuter)
        } else {
            canvas.drawCircle(center, NodeStructure2Radius * scale, Colors.switchOff)
        }
    }

    fun drawLight(center: Point, power: Boolean, scale: Float, canvas: Canvas) {
        if (power) canvas.drawCircle(center, 75f * scale, Colors.lightOnGlowInner, Colors.lightOnGlowOuter)

        canvas.drawCircle(center, NodeEdge1Radius * scale, Colors.nodeEdge)

        if (power) {
            canvas.drawCircle(
                center, NodeStructure1Radius * scale,
                Colors.lightOnInner, Colors.lightOnOuter
            )
        } else {
            canvas.drawCircle(
                center, NodeStructure1Radius * scale,
                Colors.lightOff
            )
        }
        drawNodeBase3(center, scale, canvas)
    }

    fun drawNorGate(center: Point, power: Boolean, scale: Float, canvas: Canvas) {
        drawNodeGlow(power, center, scale, canvas, Colors.norGateOnGlowInner, Colors.norGateOnGlowOuter)
        drawNodeBase(center, scale, canvas)
        canvas.drawCircle(center, NodeEdge2Radius * scale, Colors.nodeEdge)
        canvas.drawCircle(center, NodeStructure2Radius * scale, if (power) Colors.norGateOn else Colors.norGateOff)
        drawNodeBase3(center, scale, canvas)
    }

    fun drawAndGate(center: Point, power: Boolean, scale: Float, canvas: Canvas) {
        drawNodeGlow(power, center, scale, canvas, Colors.andGateOnGlowInner, Colors.andGateOnGlowOuter)
        drawNodeBase(center, scale, canvas)
        canvas.drawCircle(center, NodeEdge2Radius * scale, Colors.nodeEdge)
        canvas.drawCircle(center, NodeStructure2Radius * scale, if (power) Colors.andGateOn else Colors.andGateOff)
        drawNodeBase3(center, scale, canvas)
    }

    fun drawXorGate(center: Point, power: Boolean, scale: Float, canvas: Canvas) {
        drawNodeGlow(power, center, scale, canvas, Colors.xorGateOnGlowInner, Colors.xorGateOnGlowOuter)
        drawNodeBase(center, scale, canvas)
        canvas.drawCircle(center, NodeEdge2Radius * scale, Colors.nodeEdge)
        canvas.drawCircle(center, NodeStructure2Radius * scale, if (power) Colors.xorGateOn else Colors.xorGateOff)
        val (x, y) = center
        val xMin = x - (XorLineOffSet * scale)
        val yMin = y - (XorLineOffSet * scale)
        val xMax = x + (XorLineOffSet * scale)
        val yMax = y + (XorLineOffSet * scale)
        Paint().use {
            it.color = Colors.nodeEdge
            it.strokeWidth = XorLineEdgeWidth * scale
            canvas.drawLine(xMin, yMin, xMax, yMax, it)
            canvas.drawLine(xMin, yMax, xMax, yMin, it)
        }
        Paint().use {
            it.color = Colors.nodeStructure
            it.strokeWidth = XorLineWidth * scale
            canvas.drawLine(xMin, yMin, xMax, yMax, it)
            canvas.drawLine(xMin, yMax, xMax, yMin, it)
        }
    }
}