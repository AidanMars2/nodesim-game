package com.aidanmars.nodesim.game.skija.register.tools

import com.aidanmars.nodesim.core.Circuit
import com.aidanmars.nodesim.core.extensions.getNodesInRegion
import com.aidanmars.nodesim.core.extensions.packageRegion
import com.aidanmars.nodesim.game.skija.core.NodeSimData
import com.aidanmars.nodesim.game.skija.register.types.actors.HudDrawAble
import com.aidanmars.nodesim.game.skija.register.types.actors.WorldDrawAble
import com.aidanmars.nodesim.game.skija.register.types.data.ToolSwitchListener
import com.aidanmars.nodesim.game.skija.register.types.data.VerificationAnswerListener
import com.aidanmars.nodesim.game.skija.register.types.input.ClickLayer
import com.aidanmars.nodesim.game.skija.register.types.input.KeyListener
import com.aidanmars.nodesim.game.skija.register.types.input.MouseListener
import com.aidanmars.nodesim.game.skija.types.ToolType
import com.aidanmars.nodesim.game.skija.types.WorldLocation
import io.github.humbleui.skija.Canvas
import io.github.humbleui.types.Point
import org.lwjgl.glfw.GLFW.*
import kotlin.math.max
import kotlin.math.min

class SelectTool(override val data: NodeSimData) :
    MouseListener, KeyListener,
    WorldDrawAble, HudDrawAble,
    ToolSwitchListener, VerificationAnswerListener
{
    private var state = State.Inactive
    private var selectionLocation1 = WorldLocation(0, 0)
    private var selectionLocation2 = WorldLocation(0, 0)
    private var clipboardCircuit = Circuit()

    override fun onKeyPress(key: Int, mods: Int): Boolean {
        if (data.currentTool == ToolType.Select) {
            when (state) {
                State.Selected -> if (selectedStateKeyPress(key, mods)) return true
                State.Moving -> if (movingStateKeyPress(key)) return true
                State.Pasting -> if (pastingStateKeyPress(key)) return true
                else -> {}
            }
        }
        if (key == GLFW_KEY_Q) {
            data.currentTool = ToolType.Select
            return true
        }
        return false
    }

    private fun selectedStateKeyPress(key: Int, mods: Int): Boolean {
        when (key) {
            GLFW_KEY_Z -> {}
            GLFW_KEY_X -> {}
            GLFW_KEY_C -> {}
            GLFW_KEY_V -> {}
            GLFW_KEY_B -> {}
            GLFW_KEY_BACKSPACE -> {}
        }
        return false
    }

    private fun movingStateKeyPress(key: Int): Boolean {
        if (key == GLFW_KEY_ESCAPE) state = State.Selected
        return false
    }

    private fun pastingStateKeyPress(key: Int): Boolean {
        if (key == GLFW_KEY_ESCAPE) state = State.Selected
        return false
    }

    override fun onKeyRelease(key: Int, mods: Int): Boolean = false

    override fun onPress(clickLocation: Point, clickLayer: ClickLayer): Boolean {
        TODO("Not yet implemented")
    }

    override fun onRelease(clickLocation: Point, clickLayer: ClickLayer) {
        TODO("Not yet implemented")
    }

    override fun onDrag(newLocation: Point, clickLayer: ClickLayer) {
        TODO("Not yet implemented")
    }

    override fun drawHud(canvas: Canvas) {

    }

    override fun drawWorld(canvas: Canvas) {

    }

    override fun onToolSwitch() {
        state = if (data.currentTool == ToolType.Select) State.ToolSelected else State.Inactive
    }

    override fun onAnswer(answer: Boolean): Boolean {
        when (state) {
            State.YesNoAwaitPackage -> {
                if (!answer) return true
                val (centerX, centerY) = getSelectionCenter()
                data.withCircuitMutexLocked {
                    data.circuit.packageRegion(
                        getSelectionXRegion(),
                        getSelectionYRegion(),
                        centerX,
                        centerY
                    )
                }
            }
            State.YesNoAwaitDelete -> {
                if (!answer) return true
                val nodes = data.circuit.getNodesInRegion(
                    getSelectionXRegion(),
                    getSelectionYRegion()
                )
                data.withCircuitMutexLocked {
                    nodes.forEach {
                        data.circuit.deleteNode(it)
                    }
                }
            }
            else -> return false
        }
        return true
    }

    private fun getButtonPoint() = Point(380f, data.windowHeight - 60f)

    private fun shouldDrawSelection() = when (state) {
        State.Inactive, State.ToolSelected -> false
        else -> true
    }

    private fun shouldDrawActionButtons() = when (state) {
        State.Selected -> true// TODO: see if this is nice UX
        else -> false
    }

    private fun getSelectionXRegion() = min(selectionLocation1.x, selectionLocation2.x)..
            max(selectionLocation1.x, selectionLocation2.x)

    private fun getSelectionYRegion() = min(selectionLocation1.y, selectionLocation2.y)..
            max(selectionLocation1.y, selectionLocation2.y)

    private fun getSelectionCenter() = WorldLocation(
        (selectionLocation1.x + selectionLocation2.x) ushr 1,
        (selectionLocation1.y + selectionLocation2.y) ushr 1,
    )

    private enum class State {
        Inactive,
        ToolSelected,
        Dragging,
        Selected,
        Pasting,
        Moving,
        YesNoAwaitDelete,
        YesNoAwaitPackage
    }
}