package com.aidanmars.nodesim.game.skija.register.types

import com.aidanmars.nodesim.game.skija.core.NodeSimData

interface Listener : RegisterAble {
    val data: NodeSimData
}