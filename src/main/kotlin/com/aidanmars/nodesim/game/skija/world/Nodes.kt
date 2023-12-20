package com.aidanmars.nodesim.game.skija.world

import com.aidanmars.nodesim.game.skija.*
import io.github.humbleui.skija.Canvas
import io.github.humbleui.skija.Data
import io.github.humbleui.skija.svg.SVGDOM
import io.github.humbleui.types.Point

object Nodes {
    private val switchOffDom = SVGDOM(Data.makeFromBytes(
        Thread.currentThread().contextClassLoader.getResource("nodes/switch-off.svg")!!.readBytes())
    )
    private val switchOnDom = SVGDOM(Data.makeFromBytes(
        Thread.currentThread().contextClassLoader.getResource("nodes/switch-on.svg")!!.readBytes())
    )
    private val lightOffDom = SVGDOM(Data.makeFromBytes(
        Thread.currentThread().contextClassLoader.getResource("nodes/light-off.svg")!!.readBytes())
    )
    private val lightOnDom = SVGDOM(Data.makeFromBytes(
        Thread.currentThread().contextClassLoader.getResource("nodes/light-on.svg")!!.readBytes())
    )
    private val norGateOffDom = SVGDOM(Data.makeFromBytes(
        Thread.currentThread().contextClassLoader.getResource("nodes/norGate-off.svg")!!.readBytes())
    )
    private val norGateOnDom = SVGDOM(Data.makeFromBytes(
        Thread.currentThread().contextClassLoader.getResource("nodes/norGate-on.svg")!!.readBytes())
    )
    private val andGateOffDom = SVGDOM(Data.makeFromBytes(
        Thread.currentThread().contextClassLoader.getResource("nodes/andGate-off.svg")!!.readBytes())
    )
    private val andGateOnDom = SVGDOM(Data.makeFromBytes(
        Thread.currentThread().contextClassLoader.getResource("nodes/andGate-on.svg")!!.readBytes())
    )
    private val xorGateOffDom = SVGDOM(Data.makeFromBytes(
        Thread.currentThread().contextClassLoader.getResource("nodes/xorGate-off.svg")!!.readBytes())
    )
    private val xorGateOnDom = SVGDOM(Data.makeFromBytes(
        Thread.currentThread().contextClassLoader.getResource("nodes/xorGate-on.svg")!!.readBytes())
    )
//    private const val NodeGlowRadius = 27f
//    private const val NodeEdge1Radius = 20f
//    private const val NodeStructure1Radius = 18f
//    private const val NodeEdge2Radius = 15f
//    private const val NodeStructure2Radius = 13f
//    private const val NodeEdge3Radius = 10f
//    private const val NodeStructure3Radius = 8f
//    private const val Sin45Degrees = 0.707106781f
//    private const val XorLineOffSet = Sin45Degrees * NodeEdge2Radius + 0.05f
//    private const val XorLineWidth = NodeStructure2Radius * 0.3f
//    private const val XorLineEdgeWidth = XorLineWidth + 3f

//    private fun drawNodeBase(center: Point, scale: Float, canvas: Canvas) {
//        canvas.drawCircle(center, NodeEdge1Radius * scale, Colors.nodeEdge)
//        canvas.drawCircle(center, NodeStructure1Radius * scale, Colors.nodeStructure)
//    }
//
//    private fun drawNodeBase2(center: Point, scale: Float, canvas: Canvas) {
//        canvas.drawCircle(center, NodeEdge2Radius * scale, Colors.nodeEdge)
//        canvas.drawCircle(center, NodeStructure2Radius * scale, Colors.nodeStructure)
//    }
//
//    private fun drawNodeBase3(center: Point, scale: Float, canvas: Canvas) {
//        canvas.drawCircle(center, NodeEdge3Radius * scale, Colors.nodeEdge)
//        canvas.drawCircle(center, NodeStructure3Radius * scale, Colors.nodeStructure)
//    }
//
//    private fun drawNodeGlow(
//        shouldDraw: Boolean, center: Point,
//        scale: Float, canvas: Canvas,
//        innerColor: Int, outerColor: Int,) {
//        if (shouldDraw) {
//            canvas.drawCircle(center, NodeGlowRadius * scale, innerColor, outerColor)
//        }
//    }

    private fun drawNode(
        center: Point, power: Boolean,
        scale: Float, canvas: Canvas,
        powerOnSize: Float, powerOffSize: Float,
        powerOnDom: SVGDOM, powerOffDom: SVGDOM) {
        val dom: SVGDOM
        val size: Float
        if (power) {
            dom = powerOnDom
            size = powerOnSize * scale
        } else {
            dom = powerOffDom
            size = powerOffSize * scale
        }
        canvas.drawSvg(dom, center, size)
    }

    fun drawSwitch(center: Point, power: Boolean, scale: Float, canvas: Canvas) {
        drawNode(
            center, power, scale, canvas,
            54f, 40f,
            switchOnDom, switchOffDom
        )
//        drawNodeGlow(power, center, scale, canvas, Colors.switchOnGlowInner, Colors.switchOnGlowOuter)
//        drawNodeBase(center, scale, canvas)
//
//        canvas.drawCircle(center, NodeEdge2Radius * scale, Colors.nodeEdge)
//        if (power) {
//            canvas.drawCircle(center, NodeStructure2Radius * scale, Colors.switchOnInner, Colors.switchOnOuter)
//        } else {
//            canvas.drawCircle(center, NodeStructure2Radius * scale, Colors.switchOff)
//        }
    }

    fun drawLight(center: Point, power: Boolean, scale: Float, canvas: Canvas) {
        drawNode(
            center, power, scale, canvas,
            144f, 40f,
            lightOnDom, lightOffDom
        )
//        if (power) canvas.drawCircle(center, 75f * scale, Colors.lightOnGlowInner, Colors.lightOnGlowOuter)
//
//        canvas.drawCircle(center, NodeEdge1Radius * scale, Colors.nodeEdge)
//
//        if (power) {
//            canvas.drawCircle(
//                center, NodeStructure1Radius * scale,
//                Colors.lightOnInner, Colors.lightOnOuter
//            )
//        } else {
//            canvas.drawCircle(
//                center, NodeStructure1Radius * scale,
//                Colors.lightOff
//            )
//        }
//        drawNodeBase3(center, scale, canvas)
    }

    fun drawNorGate(center: Point, power: Boolean, scale: Float, canvas: Canvas) {
        drawNode(
            center, power, scale, canvas,
            54f, 40f,
            norGateOnDom, norGateOffDom
        )
//        drawNodeGlow(power, center, scale, canvas, Colors.norGateOnGlowInner, Colors.norGateOnGlowOuter)
//        drawNodeBase(center, scale, canvas)
//        canvas.drawCircle(center, NodeEdge2Radius * scale, Colors.nodeEdge)
//        canvas.drawCircle(center, NodeStructure2Radius * scale, if (power) Colors.norGateOn else Colors.norGateOff)
//        drawNodeBase3(center, scale, canvas)
    }

    fun drawAndGate(center: Point, power: Boolean, scale: Float, canvas: Canvas) {
        drawNode(
            center, power, scale, canvas,
            54f, 40f,
            andGateOnDom, andGateOffDom
        )
//        drawNodeGlow(power, center, scale, canvas, Colors.andGateOnGlowInner, Colors.andGateOnGlowOuter)
//        drawNodeBase(center, scale, canvas)
//        canvas.drawCircle(center, NodeEdge2Radius * scale, Colors.nodeEdge)
//        canvas.drawCircle(center, NodeStructure2Radius * scale, if (power) Colors.andGateOn else Colors.andGateOff)
//        drawNodeBase3(center, scale, canvas)
    }

    fun drawXorGate(center: Point, power: Boolean, scale: Float, canvas: Canvas) {
        drawNode(
            center, power, scale, canvas,
            54f, 40f,
            xorGateOnDom, xorGateOffDom
        )
//        drawNodeGlow(power, center, scale, canvas, Colors.xorGateOnGlowInner, Colors.xorGateOnGlowOuter)
//        drawNodeBase(center, scale, canvas)
//        canvas.drawCircle(center, NodeEdge2Radius * scale, Colors.nodeEdge)
//        canvas.drawCircle(center, NodeStructure2Radius * scale, if (power) Colors.xorGateOn else Colors.xorGateOff)
//        val (x, y) = center
//        val xMin = x - (XorLineOffSet * scale)
//        val yMin = y - (XorLineOffSet * scale)
//        val xMax = x + (XorLineOffSet * scale)
//        val yMax = y + (XorLineOffSet * scale)
//        Paint().use {
//            it.color = Colors.nodeEdge
//            it.strokeWidth = XorLineEdgeWidth * scale
//            canvas.drawLine(xMin, yMin, xMax, yMax, it)
//            canvas.drawLine(xMin, yMax, xMax, yMin, it)
//        }
//        Paint().use {
//            it.color = Colors.nodeStructure
//            it.strokeWidth = XorLineWidth * scale
//            it.strokeCap = PaintStrokeCap.ROUND
//            canvas.drawLine(xMin, yMin, xMax, yMax, it)
//            canvas.drawLine(xMin, yMax, xMax, yMin, it)
//        }
    }

    private fun Canvas.drawSvg(dom: SVGDOM, center: Point, size: Float) {
        val radius = size / 2
        drawSvg(dom, Point(center.x - radius, center.y - radius), Point(size, size))
    }
}