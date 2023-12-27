package com.aidanmars.nodesim.game.skija.register.tools

import com.aidanmars.nodesim.core.Circuit
import com.aidanmars.nodesim.core.Node
import com.aidanmars.nodesim.core.extensions.*
import com.aidanmars.nodesim.game.skija.*
import com.aidanmars.nodesim.game.skija.constants.Colors
import com.aidanmars.nodesim.game.skija.constants.SvgDoms
import com.aidanmars.nodesim.game.skija.core.NodeSimData
import com.aidanmars.nodesim.game.skija.register.types.actors.HudDrawAble
import com.aidanmars.nodesim.game.skija.register.types.actors.WorldDrawAble
import com.aidanmars.nodesim.game.skija.register.types.data.ToolSwitchListener
import com.aidanmars.nodesim.game.skija.register.types.data.VerificationAnswerListener
import com.aidanmars.nodesim.game.skija.register.types.input.ClickLayer
import com.aidanmars.nodesim.game.skija.register.types.input.KeyListener
import com.aidanmars.nodesim.game.skija.register.types.input.MouseListener
import com.aidanmars.nodesim.game.skija.register.world.drawNode
import com.aidanmars.nodesim.game.skija.types.ToolType
import com.aidanmars.nodesim.game.skija.types.WorldLocation
import io.github.humbleui.skija.Canvas
import io.github.humbleui.skija.Color
import io.github.humbleui.skija.Paint
import io.github.humbleui.skija.PaintStrokeCap
import io.github.humbleui.types.Point
import org.lwjgl.glfw.GLFW.*
import java.awt.Toolkit
import java.awt.datatransfer.DataFlavor
import java.awt.datatransfer.StringSelection
import java.awt.datatransfer.Transferable
import java.awt.datatransfer.UnsupportedFlavorException
import java.io.IOException
import java.io.UnsupportedEncodingException
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
    private var clipboardString = ""

    override fun onKeyPress(key: Int, mods: Int): Boolean {
        if (data.currentTool == ToolType.Select) {
            when (state) {
                State.Selected -> if (selectedStateKeyPress(key, mods)) return true
                State.Pasting -> if (pastingStateKeyPress(key)) return true
                else -> {}
            }
        }
        when (key) {
            GLFW_KEY_Q -> data.currentTool = ToolType.Select
            GLFW_KEY_V -> {
                if (mods and GLFW_MOD_CONTROL == 0) return false
                val trueCBString = getClipboardString()
                if (trueCBString != null) {
                    if (clipboardString != trueCBString) {
                        try {
                            clipboardCircuit = deserializeToCircuit(clipboardString)
                        } catch (_: IllegalArgumentException) {}
                    }
                }
                data.currentTool = ToolType.Select
                state = State.Pasting
            }
            else -> return false
        }
        return true
    }

    private fun selectedStateKeyPress(key: Int, mods: Int): Boolean {
        when (key) {
            GLFW_KEY_X, GLFW_KEY_C -> {
                if (mods and GLFW_MOD_CONTROL == 0) return false
                val cut = key == GLFW_KEY_X
                doCopy(cut)
            }
            GLFW_KEY_BACKSPACE -> {
                state = State.YesNoAwaitDelete
                data.startVerification()
            }
            GLFW_KEY_Z -> {
                state = State.YesNoAwaitPackage
                data.startVerification()
            }
            else -> return false
        }
        return true
    }

    private fun pastingStateKeyPress(key: Int): Boolean {
        if (key == GLFW_KEY_ESCAPE) {
            state = State.ToolSelected
            return true
        }
        return false
    }

    override fun onKeyRelease(key: Int, mods: Int): Boolean = false

    override fun onPress(clickLocation: Point, clickLayer: ClickLayer): Boolean {
        when (clickLayer) {
            ClickLayer.Overlay -> return false
            ClickLayer.Hud -> {
                when {
                    clickLocation.distance(getButtonPoint()) <= 35f -> data.currentTool = ToolType.Select

//                    data.currentTool != ToolType.Select -> return false
//                    clickLocation.distance(getCutButtonPoint()) <= 20f -> doCopy(true)
//                    clickLocation.distance(getCopyButtonPoint()) <= 20f -> doCopy(false)
//                    clickLocation.distance(getPackageButtonPoint()) <= 20f -> {
//                        state = State.YesNoAwaitPackage
//                        data.startVerification()
//                    }
//                    clickLocation.distance(getDeleteButtonPoint()) <= 20f -> {
//                        state = State.YesNoAwaitDelete
//                        data.startVerification()
//                    }
                    else -> return false
                }
                return true
            }
            ClickLayer.World -> {
                if (data.currentTool == ToolType.Select) {
                    onWorldPress(data.worldLocationAt(clickLocation))
                    return true
                }
            }
        }
        return false
    }

    private fun doCopy(cut: Boolean) {
        val (offsetX, offsetY) = data.getNodePlaceLocation(selectionLocation1.middle(selectionLocation2))
        data.withCircuitMutexLocked {
            clipboardCircuit = data.circuit.copy(
                getSelectionXRegion(),
                getSelectionYRegion(),
                -offsetX,
                -offsetY,
                cut
            )
        }
        state = State.Pasting
        clipboardString = clipboardCircuit.stringSerialize()
        val selectionString = StringSelection(clipboardString)
        Toolkit.getDefaultToolkit().systemClipboard.setContents(selectionString, selectionString)
    }

    override fun onDrag(newLocation: Point, clickLayer: ClickLayer) {
        if (clickLayer == ClickLayer.World) {
            onWorldDrag(data.worldLocationAt(newLocation))
        }
    }

    override fun onRelease(clickLocation: Point, clickLayer: ClickLayer) {
        if (clickLayer == ClickLayer.World) {
            onWorldRelease(data.worldLocationAt(clickLocation))
        }
    }

    fun onWorldPress(clickLocation: WorldLocation) {
        when (state) {
            State.Pasting -> {}
            else -> {
                selectionLocation1 = data.getNodePlaceLocation(clickLocation)
                selectionLocation2 = selectionLocation1
                state = State.Dragging
            }
        }
    }

    fun onWorldDrag(newLocation: WorldLocation) {
        when (state) {
            State.Pasting -> {}
            else -> {
                selectionLocation2 = data.getNodePlaceLocation(newLocation)
            }
        }
    }

    fun onWorldRelease(clickLocation: WorldLocation) {
        when (state) {
            State.Pasting -> {
                val pasteLocation = data.getNodePlaceLocation(clickLocation)
                data.withCircuitMutexLocked {
                    data.circuit.paste(clipboardCircuit, pasteLocation.x, pasteLocation.y)
                }
            }
            else -> {
                selectionLocation2 = data.getNodePlaceLocation(clickLocation)
                state = State.Selected
            }
        }
    }

    override fun drawHud(canvas: Canvas) {
        val point = getButtonPoint()
        val (x, y) = point
        if (data.currentTool == ToolType.Select) {
            canvas.drawCircle(point, 40f, Colors.toolbarElementBorderSelected)
        }
        canvas.drawSvg(
            SvgDoms.Hud.selectElement,
            Point(x - 35f, y - 35f),
            Point(70f, 70f)
        )
//        if (!shouldDrawActionButtons()) return
    }

    override fun drawWorld(canvas: Canvas) {
        if (data.currentTool != ToolType.Select) return
        val screenSelection1 = data.screenPointAt(selectionLocation1)
        val screenSelection2 = data.screenPointAt(selectionLocation2)

        if (shouldDrawSelection()) {
            Paint().use {
                it.color = Color.makeARGB(128, 0, 0, 255)
                it.strokeCap = PaintStrokeCap.ROUND
                it.strokeWidth = 5f
                canvas.drawLine(screenSelection1.x, screenSelection1.y, screenSelection1.x, screenSelection2.y, it)
                canvas.drawLine(screenSelection1.x, screenSelection1.y, screenSelection2.x, screenSelection1.y, it)
                canvas.drawLine(screenSelection2.x, screenSelection1.y, screenSelection2.x, screenSelection2.y, it)
                canvas.drawLine(screenSelection1.x, screenSelection2.y, screenSelection2.x, screenSelection2.y, it)
            }
            Paint().use {
                it.color = Color.makeARGB(128, 0, 0, 255)
                data.circuit.getNodesInRegion(getSelectionXRegion(), getSelectionYRegion()).forEach { node ->
                    val (nodeX, nodeY) = data.screenPointAt(node.location())
                    canvas.drawCircle(
                        nodeX, nodeY,
                        20f * data.scale, it
                    )
                }
            }
        }
        if (state == State.Pasting) {
            val layer = canvas.saveLayerAlpha(null, 128)

            val pastePlaceLocation = data.getNodePlaceLocation(WorldLocation(data.mouseWorldX, data.mouseWorldY))
            clipboardCircuit.nodes.forEach { (_, node) ->
                val centerPoint = data.screenPointAt(
                    WorldLocation(pastePlaceLocation.x + node.x, pastePlaceLocation.y + node.y)
                )
                drawNode(node.type, centerPoint, node.outputPower, data.scale, canvas)
            }

            canvas.restoreToCount(layer)
        }
    }

    override fun onToolSwitch() {
        state = if (data.currentTool == ToolType.Select) State.ToolSelected else State.Inactive
    }

    override fun onAnswer(answer: Boolean): Boolean {
        when (state) {
            State.YesNoAwaitPackage -> {
                if (!answer) return true
                val (centerX, centerY) = data.getNodePlaceLocation(getSelectionCenter())
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
//    private fun getCopyButtonPoint() = selectionTopLeftScreenLocation().offset(-25f, 0f)
//    private fun getCutButtonPoint(): Point = selectionTopLeftScreenLocation().offset(-25f, 50f)
//    private fun getDeleteButtonPoint() = selectionTopLeftScreenLocation().offset(-25f, 100f)
//    private fun getPackageButtonPoint() = selectionTopLeftScreenLocation().offset(-25f, 150f)

    private fun shouldDrawSelection() = when (state) {
        State.Dragging, State.Selected, State.YesNoAwaitPackage, State.YesNoAwaitDelete -> true
        else -> false
    }

//    private fun shouldDrawActionButtons() = when (state) {
//        State.Selected -> true// TODO: see if this is nice UX
//        else -> false
//    }

    private fun getSelectionXRegion() = min(selectionLocation1.x, selectionLocation2.x)..
            max(selectionLocation1.x, selectionLocation2.x)

    private fun getSelectionYRegion() = min(selectionLocation1.y, selectionLocation2.y)..
            max(selectionLocation1.y, selectionLocation2.y)

    private fun getSelectionCenter() = WorldLocation(
        (selectionLocation1.x + selectionLocation2.x) ushr 1,
        (selectionLocation1.y + selectionLocation2.y) ushr 1,
    )

    private fun getClipboardString(): String? {
        try {
            val clipboard = Toolkit.getDefaultToolkit().systemClipboard
            if (!clipboard.isDataFlavorAvailable(DataFlavor.stringFlavor)) return null
            return clipboard.getContents(null).getTransferData(DataFlavor.stringFlavor) as? String
        }
        catch (_: IOException) {}
        catch (_: UnsupportedFlavorException) {}
        return null
    }

    private enum class State {
        Inactive,
        ToolSelected,
        Dragging,
        Selected,
        Pasting,
//        Moving, // too complicated
        YesNoAwaitDelete,
        YesNoAwaitPackage
    }
}