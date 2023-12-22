package com.aidanmars.nodesim.game.skija.core

import com.aidanmars.nodesim.game.skija.register.MovementRegisterAble
import com.aidanmars.nodesim.game.skija.register.world.BackGroundDrawAble
import com.aidanmars.nodesim.game.skija.register.world.NodesDrawAble

fun NodeSimWindow.fillRegister() {
    // drawable priority goes from low to high,
    // listener/actor priority goes from high to low

    // world drawAbles (low prio)
    register(BackGroundDrawAble(data))
    register(NodesDrawAble(data))

    // player movement (high prio)
    register(MovementRegisterAble(data))
}