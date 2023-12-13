package com.aidanmars.nodesim.game.skija

import com.aidanmars.nodesim.core.Circuit
import com.aidanmars.nodesim.core.NodeType

class GameData {
    var playerX = 0
    var playerY = 0
    var scale = 1f
    var circuit = Circuit()
    var selectionLocation1 = WorldLocation(0, 0)
    var selectionLocation2 = WorldLocation(0, 0)
    var sl2ShouldChaseMouse = false
    var showSelection = false
    var currentPlaceType = NodeType.Switch
    var wasdKeysPressed = BooleanArray(4)
    var currentTool = ToolType.Interact

    fun getVirtualScreenLocation(worldLocation: WorldLocation): VirtualScreenLocation =
        VirtualScreenLocation((worldLocation.x - playerX) * scale, (worldLocation.y - playerY) * scale)

    fun getWorldLocation(virtualScreenLocation: VirtualScreenLocation): WorldLocation =
        WorldLocation(
            ((virtualScreenLocation.x / scale) + playerX).toInt(),
            ((virtualScreenLocation.y / scale) + playerY).toInt()
        )
}