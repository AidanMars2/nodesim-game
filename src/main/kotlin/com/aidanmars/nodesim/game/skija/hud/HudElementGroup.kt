package com.aidanmars.nodesim.game.skija.hud

import com.aidanmars.nodesim.game.skija.hud.elements.*
import com.aidanmars.nodesim.game.skija.hud.elements.nodes.*

@Suppress("MemberVisibilityCanBePrivate")
class HudElementGroup {

    val placeElement = PlaceHudElement()
    val deleteElement = DeleteHudElement()
    val interactElement = InteractHudElement()
    val exitElement = ExitHudElement()
    val connectElement = ConnectHudElement()
    val switchElement = SwitchHudElement()
    val lightElement = LightHudElement()
    val norGateElement = NorGateHudElement()
    val andGateElement = AndGateHudElement()
    val xorGateElement = XorGateHudElement()
    val snapElement = SnapHudElement()
    val selectElement = SelectHudElement()
    val selectionElement = SelectionElement()
    val yesNoElement = YesNoElement()

    fun addAllToList(list: MutableList<HudElement>) {
        // low priority
        list.add(selectionElement)

        // medium priority
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

        // high priority
        list.add(yesNoElement)
    }
}
