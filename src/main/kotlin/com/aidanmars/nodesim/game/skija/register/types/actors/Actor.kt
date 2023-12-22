package com.aidanmars.nodesim.game.skija.register.types.actors

import com.aidanmars.nodesim.game.skija.register.types.RegisterAble
import com.aidanmars.nodesim.game.skija.core.NodeSimData

interface Actor : RegisterAble {
    val data: NodeSimData
}