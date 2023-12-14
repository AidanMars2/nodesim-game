package com.aidanmars.nodesim.game.skija.world

import com.aidanmars.nodesim.core.Node
import com.aidanmars.nodesim.core.NodeType
import com.aidanmars.nodesim.game.skija.NodeSimWindow
import com.aidanmars.nodesim.game.skija.WorldLocation
import com.aidanmars.nodesim.game.skija.component1
import com.aidanmars.nodesim.game.skija.component2
import io.github.humbleui.skija.Canvas
import io.github.humbleui.types.Point

fun drawNode(node: Node, window: NodeSimWindow, canvas: Canvas) {
    val (x, y) = window.getTrueScreenLocation(WorldLocation(node.x, node.y))
    val point = Point(x, y)
    when (node.type) {
        NodeType.Switch -> Nodes.drawSwitch(point, node.outputPower, window.data.scale, canvas)
        NodeType.SwitchOn -> Nodes.drawSwitch(point, node.outputPower, window.data.scale, canvas)
        NodeType.Light -> Nodes.drawLight(point, node.outputPower, window.data.scale, canvas)
        NodeType.NorGate -> Nodes.drawNorGate(point, node.outputPower, window.data.scale, canvas)
        NodeType.AndGate -> Nodes.drawAndGate(point, node.outputPower, window.data.scale, canvas)
        NodeType.XorGate -> Nodes.drawXorGate(point, node.outputPower, window.data.scale, canvas)
    }
}