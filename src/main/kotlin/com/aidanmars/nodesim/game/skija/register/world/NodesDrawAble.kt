package com.aidanmars.nodesim.game.skija.register.world

import com.aidanmars.nodesim.core.Node
import com.aidanmars.nodesim.game.skija.register.types.actors.DrawAble
import com.aidanmars.nodesim.game.skija.core.NodeSimData
import com.aidanmars.nodesim.game.skija.location
import com.aidanmars.nodesim.game.skija.register.types.actors.WorldDrawAble
import io.github.humbleui.skija.Canvas

class NodesDrawAble(override val data: NodeSimData) : WorldDrawAble {
    override fun drawWorld(canvas: Canvas) {
        data.analytics.measureTime("world.nodes") {
            data.nodesOnScreen.forEach {
                drawNode(it, data, canvas)
            }
        }
        data.analytics.measureTime("world.wires") { drawWires(canvas) }
    }

    private fun drawWires(canvas: Canvas) {
        val helper = WireDrawHelper(data.scale)
        data.nodesOnScreen.forEach { node ->
            node.inputNodes.forEach {
                drawWire(it, node, helper)
            }
            node.outputNodes.forEach {
                drawWire(node, it, helper)
            }
        }
        helper.flush(canvas)
    }

    private fun drawWire(from: Node, to: Node, helper: WireDrawHelper) =
        helper.drawWire(
            data.screenPointAt(from.location()),
            data.screenPointAt(to.location()),
            from.outputPower,
        )
}