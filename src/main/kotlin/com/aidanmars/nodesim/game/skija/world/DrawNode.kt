package com.aidanmars.nodesim.game.skija.world

import com.aidanmars.nodesim.core.Node
import com.aidanmars.nodesim.core.NodeType
import com.aidanmars.nodesim.game.skija.*
import com.aidanmars.nodesim.game.skija.constants.SvgDoms
import io.github.humbleui.skija.Canvas
import io.github.humbleui.skija.svg.SVGDOM
import io.github.humbleui.types.Point

fun drawNode(node: Node, window: NodeSimWindow, canvas: Canvas) {
    val (x, y) = window.getTrueScreenLocation(WorldLocation(node.x, node.y))
    val point = Point(x, y)
    when (node.type) {
        NodeType.Switch, NodeType.SwitchOn ->
            drawNode(point, node.outputPower, window.data.scale, canvas,
                SvgDoms.Nodes.switchOn, SvgDoms.Nodes.switchOff)
        NodeType.Light ->
            drawNode(point, node.outputPower, window.data.scale, canvas,
                SvgDoms.Nodes.lightOn, SvgDoms.Nodes.lightOff, 144f)
        NodeType.NorGate ->
            drawNode(point, node.outputPower, window.data.scale, canvas,
                SvgDoms.Nodes.norGateOn, SvgDoms.Nodes.norGateOff)
        NodeType.AndGate ->
            drawNode(point, node.outputPower, window.data.scale, canvas,
                SvgDoms.Nodes.andGateOn, SvgDoms.Nodes.andGateOff)
        NodeType.XorGate ->
            drawNode(point, node.outputPower, window.data.scale, canvas,
                SvgDoms.Nodes.xorGateOn, SvgDoms.Nodes.xorGateOff)
    }
}

private fun drawNode(
    center: Point, power: Boolean,
    scale: Float, canvas: Canvas,
    powerOnDom: SVGDOM, powerOffDom: SVGDOM,
    powerOnSize: Float = 54f, powerOffSize: Float = 40f,
) {
    val dom: SVGDOM
    val size: Float
    if (power) {
        dom = powerOnDom
        size = powerOnSize * scale
    } else {
        dom = powerOffDom
        size = powerOffSize * scale
    }
    val radius = size / 2
    canvas.drawSvg(dom, Point(center.x - radius, center.y - radius), Point(size, size))
}