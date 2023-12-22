package com.aidanmars.nodesim.game.skija.listeners.data

import com.aidanmars.nodesim.core.extensions.getNodesInRegion
import com.aidanmars.nodesim.game.skija.core.NodeSimData

class NodesOnScreenMovementListener(override val data: NodeSimData) : PlayerMovementListener {
    override fun onPlayerMove() {
        data.nodesOnScreen.clear()
        val (sx1, sy1) = data.topLeftScreenLocation()
        val (sx2, sy2) = data.bottomRightScreenLocation()
        data.nodesOnScreen.addAll(
            data.circuit.getNodesInRegion(
                sx1..sx2,
                sy1..sy2
            )
        )
    }
}