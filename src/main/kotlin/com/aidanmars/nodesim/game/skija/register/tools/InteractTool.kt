package com.aidanmars.nodesim.game.skija.register.tools

import com.aidanmars.nodesim.core.Node
import com.aidanmars.nodesim.game.skija.constants.SvgDoms
import com.aidanmars.nodesim.game.skija.core.NodeSimData
import com.aidanmars.nodesim.game.skija.types.ToolType
import com.aidanmars.nodesim.game.skija.types.WorldLocation
import io.github.humbleui.skija.Canvas
import io.github.humbleui.skija.svg.SVGDOM
import io.github.humbleui.types.Point
import org.lwjgl.glfw.GLFW

class InteractTool(override val data: NodeSimData) : NodeSimTool() {
    override val toolType: ToolType = ToolType.Interact
    override val buttonIcon: SVGDOM = SvgDoms.Hud.interactElement
    override val keyToSelect: Int = GLFW.GLFW_KEY_F

    private var pressLocation = WorldLocation(0, 0)
    private var topNodeAtPress: Node? = null
    private var isDragging = false

    override fun drawSelection(canvas: Canvas) {}

    override fun onWorldPress(clickLocation: WorldLocation) {
        pressLocation = clickLocation
        topNodeAtPress = data.getTopNodeAt(clickLocation)
    }

    override fun onWorldDrag(newLocation: WorldLocation) {
        if (isDragging || newLocation.distanceTo(pressLocation) > (20f / data.scale)) {
            isDragging = true
            data.placeNodeAtLocation(topNodeAtPress ?: return, newLocation)
        }
    }

    override fun onWorldRelease(clickLocation: WorldLocation) {
        if (isDragging || clickLocation.distanceTo(pressLocation) > (20f / data.scale)) {
            if (topNodeAtPress != null) {
                data.placeNodeAtLocation(topNodeAtPress!!, clickLocation)
            }
            isDragging = false
            topNodeAtPress = null
            return
        }

        val topNode = data.getTopNodeAt(clickLocation) ?: return
        data.withCircuitMutexLocked {
            data.circuit.triggerNode(topNode)
        }
    }

    override fun getButtonPoint() = Point(220f, data.windowHeight - 60f)
}