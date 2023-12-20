package com.aidanmars.nodesim.game.skija.hud

import com.aidanmars.nodesim.game.skija.hud.elements.*
import com.aidanmars.nodesim.game.skija.hud.elements.nodes.*

data class HudElementGroup(
    val placeElement: HudElement = PlaceHudElement(),
    val deleteElement: HudElement = DeleteHudElement(),
    val interactElement: HudElement = InteractHudElement(),
    val exitElement: HudElement = ExitHudElement(),
    val connectElement: HudElement = ConnectHudElement(),
    val switchElement: HudElement = SwitchHudElement(),
    val lightElement: HudElement = LightHudElement(),
    val norGateElement: HudElement = NorGateHudElement(),
    val andGateElement: HudElement = AndGateHudElement(),
    val xorGateElement: HudElement = XorGateHudElement(),
    val snapElement: HudElement = SnapHudElement(),
    val selectElement: HudElement = SelectHudElement()
) {
    fun addAllToList(list: MutableList<HudElement>) {
        list.add(placeElement)
        list.add(deleteElement)
        list.add(interactElement)
        list.add(exitElement)
        list.add(connectElement)
        list.add(switchElement)
        list.add(lightElement)
        list.add(norGateElement)
        list.add(andGateElement)
        list.add(xorGateElement)
        list.add(snapElement)
        list.add(selectElement)
    }
}
