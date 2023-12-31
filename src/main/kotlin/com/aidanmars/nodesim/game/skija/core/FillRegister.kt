package com.aidanmars.nodesim.game.skija.core

import com.aidanmars.nodesim.game.skija.register.AnalyticsHandler
import com.aidanmars.nodesim.game.skija.register.MovementHandler
import com.aidanmars.nodesim.game.skija.register.VerificationHandler
import com.aidanmars.nodesim.game.skija.register.hud.*
import com.aidanmars.nodesim.game.skija.register.tools.*
import com.aidanmars.nodesim.game.skija.register.world.BackGroundDrawAble
import com.aidanmars.nodesim.game.skija.register.world.NodesDrawAble

fun NodeSimWindow.fillRegister() {
    // registerAbles registered first will be drawn on top and take input first,
    // and hence have the opportunity to block input to other registerAbles

    // high priority (things like a yes/no screen)
    run {
        register(AnalyticsHandler(data))
        register(VerificationHandler(data))
    }

    // medium priority (things like the heads-up display)
    run {
        // exit/close
        register(ExitHudElement(data))

        // snap distance button
        register(SnapHudElement(data))

        // saving
        register(SaveTool(data))

        // toolbar
        register(SelectTool(data))
        register(PlaceTool(data))
        register(DeleteTool(data))
        register(InteractTool(data))
        register(ConnectTool(data))

        // player movement
        register(MovementHandler(data))
    }

    // low priority (things like the background)
    run {

        // world drawAbles
        register(NodesDrawAble(data))// nodes on top of the lines
        register(BackGroundDrawAble(data))
    }
}