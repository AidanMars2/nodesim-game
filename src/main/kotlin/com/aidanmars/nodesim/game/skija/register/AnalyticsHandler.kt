package com.aidanmars.nodesim.game.skija.register

import com.aidanmars.nodesim.game.skija.core.NodeSimData
import com.aidanmars.nodesim.game.skija.register.types.actors.ConstantActor

class AnalyticsHandler(override val data: NodeSimData) : ConstantActor {
    private var totalTimePassedSinceLastPrint = 0

    override fun act(timePassedMillis: Int) {
        if (timePassedMillis < 0) return
        totalTimePassedSinceLastPrint += timePassedMillis
        if (totalTimePassedSinceLastPrint > 10_000) {
            data.analytics.print()
            totalTimePassedSinceLastPrint = 0
        }
    }
}