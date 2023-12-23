package com.aidanmars.nodesim.game.skija.register.hud

import com.aidanmars.nodesim.core.NodeType
import com.aidanmars.nodesim.game.skija.*
import com.aidanmars.nodesim.game.skija.constants.Colors
import com.aidanmars.nodesim.game.skija.constants.SvgDoms
import com.aidanmars.nodesim.game.skija.core.NodeSimData
import com.aidanmars.nodesim.game.skija.register.types.actors.DrawAble
import com.aidanmars.nodesim.game.skija.register.types.input.KeyListener
import com.aidanmars.nodesim.game.skija.register.types.input.MouseListener
import com.aidanmars.nodesim.game.skija.types.ToolType
import com.aidanmars.nodesim.game.skija.types.WorldLocation
import io.github.humbleui.skija.Canvas
import io.github.humbleui.types.Point
import org.lwjgl.glfw.GLFW.*

class PlaceHudElement(override val data: NodeSimData) : KeyListener, MouseListener, DrawAble {
    private var clickedWorld = false
    private var currentPlaceType = NodeType.Switch

    override fun draw(canvas: Canvas) {
        val point = getButtonPoint()
        val (x, y) = point
        if (data.currentTool == ToolType.Place) {
            canvas.drawCircle(point, 40f, Colors.toolbarElementBorderSelected)
            drawToolbarNodes(canvas)
        }
        canvas.drawSvg(SvgDoms.Hud.placeElement, Point(x - 35f, y - 35f), Point(70f, 70f))
    }

    private fun drawToolbarNodes(canvas: Canvas) {
        val selectedNodeTypeCenter = when (currentPlaceType) {
            NodeType.Switch, NodeType.SwitchOn -> getSwitchButtonPoint()
            NodeType.Light -> getLightButtonPoint()
            NodeType.NorGate -> getNorGateButtonPoint()
            NodeType.AndGate -> getAndGateButtonPoint()
            NodeType.XorGate -> getXorGateButtonPoint()
        }
        canvas.drawCircle(selectedNodeTypeCenter, 35f, Colors.toolbarElementBorderSelected)

        canvas.drawSvg(
            SvgDoms.Nodes.switchOff,
            getSwitchButtonPoint().offset(-30f, -30f),
            Point(60f, 60f)
        )
        canvas.drawSvg(
            SvgDoms.Nodes.lightOff,
            getLightButtonPoint().offset(-30f, -30f),
            Point(60f, 60f)
        )
        canvas.drawSvg(
            SvgDoms.Nodes.norGateOn,
            getNorGateButtonPoint().offset(-40.5f, -40.5f),
            Point(81f, 81f)
        )
        canvas.drawSvg(
            SvgDoms.Nodes.andGateOn,
            getAndGateButtonPoint().offset(-40.5f, -40.5f),
            Point(81f, 81f)
        )
        canvas.drawSvg(
            SvgDoms.Nodes.xorGateOff,
            getXorGateButtonPoint().offset(-30f, -30f),
            Point(60f, 60f)
        )
    }

    override fun onKeyPress(key: Int, mods: Int): Boolean {
        when (key) {
            GLFW_KEY_E -> {}
            GLFW_KEY_1 -> currentPlaceType = NodeType.Switch
            GLFW_KEY_2 -> currentPlaceType = NodeType.Light
            GLFW_KEY_3 -> currentPlaceType = NodeType.NorGate
            GLFW_KEY_4 -> currentPlaceType = NodeType.AndGate
            GLFW_KEY_5 -> currentPlaceType = NodeType.XorGate
            else -> return false
        }
        data.currentTool = ToolType.Place
        return true
    }

    override fun onKeyRelease(key: Int, mods: Int): Boolean {
        return false
    }

    override fun onPress(clickLocation: Point): Boolean {
        if (clickLocation.distance(getButtonPoint()) <= 35f) {
            data.currentTool = ToolType.Place
            return true
        }
        if (data.currentTool != ToolType.Place) return false
        when {
            clickLocation.distance(getSwitchButtonPoint()) <= 35f -> currentPlaceType = NodeType.Switch
            clickLocation.distance(getLightButtonPoint()) <= 35f -> currentPlaceType = NodeType.Light
            clickLocation.distance(getNorGateButtonPoint()) <= 35f -> currentPlaceType = NodeType.NorGate
            clickLocation.distance(getAndGateButtonPoint()) <= 35f -> currentPlaceType = NodeType.AndGate
            clickLocation.distance(getXorGateButtonPoint()) <= 35f -> currentPlaceType = NodeType.XorGate

            else -> {
                clickedWorld = true
                onWorldPress(data.worldLocationAt(clickLocation))
            }
        }
        return true
    }

    override fun onDrag(newLocation: Point) {
        if (clickedWorld) {
            onWorldDrag(data.worldLocationAt(newLocation))
        }
    }

    override fun onRelease(clickLocation: Point) {
        if (clickedWorld) {
            onWorldRelease(data.worldLocationAt(clickLocation))
            clickedWorld = false
        }
    }

    private fun onWorldPress(clickLocation: WorldLocation) {}

    private fun onWorldDrag(newLocation: WorldLocation) {}

    private fun onWorldRelease(clickLocation: WorldLocation) {
        data.withCircuitMutexLocked {
            val (nodeX, nodeY) = data.getNodePlaceLocation(clickLocation)
            data.circuit.createNode(nodeX, nodeY, currentPlaceType)
        }
    }

    private fun getButtonPoint() = Point(60f, data.windowHeight - 60f)

    private fun getSwitchButtonPoint() = Point(60f, data.windowHeight - 140f)
    private fun getLightButtonPoint() = Point(140f, data.windowHeight - 140f)
    private fun getNorGateButtonPoint() = Point(220f, data.windowHeight - 140f)
    private fun getAndGateButtonPoint() = Point(300f, data.windowHeight - 140f)
    private fun getXorGateButtonPoint() = Point(380f, data.windowHeight - 140f)
}