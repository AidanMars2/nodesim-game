package com.aidanmars.nodesim.game.skija.hud

import com.aidanmars.nodesim.game.skija.hud.elements.*

data class HudElementGroup(
    val placeElement: PlaceHudElement = PlaceHudElement(),
    val deleteElement: DeleteHudElement = DeleteHudElement(),
    val interactElement: InteractHudElement = InteractHudElement(),
    val exitElement: ExitHudElement = ExitHudElement(),
    val connectElement: ConnectHudElement = ConnectHudElement()
) {
    fun addAllToList(list: MutableList<HudElement>) {
        list.add(placeElement)
        list.add(deleteElement)
        list.add(interactElement)
        list.add(exitElement)
        list.add(connectElement)
    }
}
