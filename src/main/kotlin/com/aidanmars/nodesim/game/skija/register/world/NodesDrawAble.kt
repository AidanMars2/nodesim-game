package com.aidanmars.nodesim.game.skija.register.world

import com.aidanmars.nodesim.core.Node
import com.aidanmars.nodesim.game.skija.register.types.actors.DrawAble
import com.aidanmars.nodesim.game.skija.core.NodeSimData
import com.aidanmars.nodesim.game.skija.location
import io.github.humbleui.skija.Canvas

class NodesDrawAble(override val data: NodeSimData) : DrawAble {
    override fun draw(canvas: Canvas) {
        data.nodesOnScreen.forEach {
            drawNode(it, data, canvas)
        }
        drawWires(canvas)
    }

    private fun drawWires(canvas: Canvas) {
        data.nodesOnScreen.forEach { node ->
            node.inputNodes.forEach {
                drawWire(it, node, canvas)
            }
            node.outputNodes.forEach {
                drawWire(node, it, canvas)
            }
        }
    }

    private fun drawWire(from: Node, to: Node, canvas: Canvas) =
        drawWire(
            data.screenPointAt(from.location()),
            data.screenPointAt(to.location()),
            from.outputPower,
            data.scale,
            canvas
        )
}