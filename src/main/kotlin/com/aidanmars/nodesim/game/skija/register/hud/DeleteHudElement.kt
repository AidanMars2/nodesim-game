package com.aidanmars.nodesim.game.skija.register.hud

import com.aidanmars.nodesim.game.skija.constants.SvgDoms
import com.aidanmars.nodesim.game.skija.core.NodeSimData
import com.aidanmars.nodesim.game.skija.types.ToolType
import com.aidanmars.nodesim.game.skija.types.WorldLocation
import io.github.humbleui.skija.Canvas
import io.github.humbleui.skija.svg.SVGDOM
import io.github.humbleui.types.Point
import org.lwjgl.glfw.GLFW

class DeleteHudElement(override val data: NodeSimData) : ToolBarElement() {
    override val toolType: ToolType = ToolType.Delete
    override val buttonIcon: SVGDOM = SvgDoms.Hud.deleteElement
    override val keyToSelect: Int = GLFW.GLFW_KEY_R
    override fun drawSelection(canvas: Canvas) {}

    override fun onWorldPress(clickLocation: WorldLocation) {
        deleteAt(clickLocation)
    }

    override fun onWorldDrag(newLocation: WorldLocation) {
        deleteAt(newLocation)
    }

    override fun onWorldRelease(clickLocation: WorldLocation) {
        deleteAt(clickLocation)
    }

    private fun deleteAt(location: WorldLocation) {
        val topObject = data.getTopObjectAt(location) ?: return

        if (topObject.second == null) {
            data.withCircuitMutexLocked {
                data.circuit.deleteNode(topObject.first)
            }
        } else {
            data.withCircuitMutexLocked {
                data.circuit.disconnectNodes(topObject.first, topObject.second!!)
            }
        }
    }

    override fun getButtonPoint() = Point(140f, data.windowHeight - 60f)
}