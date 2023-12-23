package com.aidanmars.nodesim.game.skija.register.hud

import com.aidanmars.nodesim.game.skija.constants.SvgDoms
import com.aidanmars.nodesim.game.skija.core.NodeSimData
import com.aidanmars.nodesim.game.skija.types.ToolType
import com.aidanmars.nodesim.game.skija.types.WorldLocation
import io.github.humbleui.skija.Canvas
import io.github.humbleui.skija.svg.SVGDOM
import io.github.humbleui.types.Point
import org.lwjgl.glfw.GLFW

class InteractHudElement(override val data: NodeSimData) : ToolBarElement() {
    override val toolType: ToolType = ToolType.Interact
    override val buttonIcon: SVGDOM = SvgDoms.Hud.interactElement
    override val keyToSelect: Int = GLFW.GLFW_KEY_T
    override fun drawSelection(canvas: Canvas) {}

    override fun onWorldPress(clickLocation: WorldLocation) {}

    override fun onWorldDrag(newLocation: WorldLocation) {}

    override fun onWorldRelease(clickLocation: WorldLocation) {
        val topNode = data.getTopNodeAt(clickLocation) ?: return
        data.withCircuitMutexLocked {
            data.circuit.triggerNode(topNode)
        }
    }

    override fun getButtonPoint() = Point(220f, data.windowHeight - 60f)
}