package com.aidanmars.nodesim.game.skija.register.tools

import com.aidanmars.nodesim.core.Node
import com.aidanmars.nodesim.game.skija.constants.SvgDoms
import com.aidanmars.nodesim.game.skija.core.NodeSimData
import com.aidanmars.nodesim.game.skija.location
import com.aidanmars.nodesim.game.skija.types.ToolType
import com.aidanmars.nodesim.game.skija.types.WorldLocation
import com.aidanmars.nodesim.game.skija.register.world.drawWire
import io.github.humbleui.skija.Canvas
import io.github.humbleui.skija.svg.SVGDOM
import io.github.humbleui.types.Point
import org.lwjgl.glfw.GLFW.*

class ConnectTool(override val data: NodeSimData) : NodeSimTool() {
    override val toolType: ToolType = ToolType.Connect
    override val buttonIcon: SVGDOM = SvgDoms.Hud.connectElement
    override val keyToSelect: Int = GLFW_KEY_G

    private var showSelection = false
    private var fromLocation = WorldLocation(0, 0)
    private var toLocation = WorldLocation(0, 0)
    private var fromNode: Node? = null
        set(value) {
            field = value
            if (value == null) {
                showSelection = false
                return
            }
            showSelection = true
            fromLocation = value.location()
        }

    override fun drawSelection(canvas: Canvas) {
        if (showSelection) {
            drawWire(
                data.screenPointAt(fromLocation),
                data.screenPointAt(toLocation),
                fromNode?.outputPower ?: false,
                data.scale,
                canvas
            )
        }
    }

    override fun onWorldPress(clickLocation: WorldLocation) {
        fromNode = data.getTopNodeAt(clickLocation)
        toLocation = clickLocation
    }

    override fun onWorldDrag(newLocation: WorldLocation) {
        toLocation = data.getTopNodeAt(newLocation)?.location() ?: newLocation
    }

    override fun onWorldRelease(clickLocation: WorldLocation) {
        val toNode = data.getTopNodeAt(clickLocation)
        val thisFromNode = fromNode
        if (thisFromNode == null || toNode == null) return
        data.withCircuitMutexLocked {
            data.circuit.connectNodes(thisFromNode, toNode)
        }
        fromNode = null
    }

    override fun getButtonPoint(): Point = Point(300f, data.windowHeight - 60f)
}