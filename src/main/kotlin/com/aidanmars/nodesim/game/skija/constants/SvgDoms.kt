package com.aidanmars.nodesim.game.skija.constants

import com.aidanmars.nodesim.game.skija.getSvgFromResource

object SvgDoms {
    object Hud {
        val connectElement = getSvgFromResource("hud/connectElement.svg")
        val deleteElement = getSvgFromResource("hud/deleteElement.svg")
        val interactElement = getSvgFromResource("hud/interactElement.svg")
        val placeElement = getSvgFromResource("hud/placeElement.svg")
        val selectElement = getSvgFromResource("hud/selectElement.svg")
        object Selection {
            val copyOption = getSvgFromResource("hud/selection/copyOption.svg")
            val deleteOption = getSvgFromResource("hud/selection/copyOption.svg")
            val moveOption = getSvgFromResource("hud/selection/copyOption.svg")
            val packageOption = getSvgFromResource("hud/selection/copyOption.svg")
        }
    }
    object Nodes {
        val switchOff = getSvgFromResource("nodes/switch-off.svg")
        val switchOn = getSvgFromResource("nodes/switch-on.svg")
        val lightOff = getSvgFromResource("nodes/light-off.svg")
        val lightOn = getSvgFromResource("nodes/light-on.svg")
        val norGateOff = getSvgFromResource("nodes/norGate-off.svg")
        val norGateOn = getSvgFromResource("nodes/norGate-on.svg")
        val andGateOff = getSvgFromResource("nodes/andGate-off.svg")
        val andGateOn = getSvgFromResource("nodes/andGate-on.svg")
        val xorGateOff = getSvgFromResource("nodes/xorGate-off.svg")
        val xorGateOn = getSvgFromResource("nodes/xorGate-on.svg")
    }
}