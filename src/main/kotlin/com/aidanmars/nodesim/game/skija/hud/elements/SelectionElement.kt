package com.aidanmars.nodesim.game.skija.hud.elements

import com.aidanmars.nodesim.core.Node
import com.aidanmars.nodesim.core.extensions.getNodesInRegion
import com.aidanmars.nodesim.core.extensions.packageRegion
import com.aidanmars.nodesim.game.skija.*
import com.aidanmars.nodesim.game.skija.constants.SvgDoms
import com.aidanmars.nodesim.game.skija.core.NodeSimWindow
import com.aidanmars.nodesim.game.skija.hud.HudElement
import com.aidanmars.nodesim.game.skija.types.WorldLocation
import io.github.humbleui.skija.Canvas
import io.github.humbleui.types.Point
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.withLock
import org.lwjgl.glfw.GLFW.*
import kotlin.math.max
import kotlin.math.min

class SelectionElement : HudElement {
    override var shouldDraw: Boolean = true
        set(value) {
            field = value
            isFocused = !field
        }
    override var isFocused: Boolean = false
    private var copyOptionLocation = Point(0f, 0f)
    private var deleteOptionLocation = Point(0f, 0f)
    private var moveOptionLocation = Point(0f, 0f)
    private var packageOptionLocation = Point(0f, 0f)
    private val stopMoveButtonLocation = Point(15f, 15f)
    private var nodesInSelection = setOf<Node>()

    override fun draw(window: NodeSimWindow, canvas: Canvas) {
        setButtonLocations(window)
        canvas.drawSvg(
            SvgDoms.Hud.Selection.copyOption,
            copyOptionLocation, Point(30f, 30f)
        )
        canvas.drawSvg(
            SvgDoms.Hud.Selection.deleteOption,
            deleteOptionLocation, Point(30f, 30f)
        )
        canvas.drawSvg(
            SvgDoms.Hud.Selection.moveOption,
            moveOptionLocation, Point(30f, 30f)
        )
        canvas.drawSvg(
            SvgDoms.Hud.Selection.packageOption,
            packageOptionLocation, Point(30f, 30f)
        )
//        if (window.data.clickedMoveSelection) {
//            canvas.drawSvg(
//                SvgDoms.Hud.Selection.stopMoveButton,
//                stopMoveButtonLocation, Point(60f, 60f)
//            )
//        }
    }

    override fun onClick(window: NodeSimWindow, mouseLocation: Point): Boolean {
        when {
            copyOptionLocation.toCenterOfButton()
                .distance(mouseLocation) <= 15f -> doCopy(window)

            deleteOptionLocation.toCenterOfButton()
                .distance(mouseLocation) <= 15f -> doDelete(window)

            moveOptionLocation.toCenterOfButton()
                .distance(mouseLocation) <= 15f -> doMove(window)

            packageOptionLocation.toCenterOfButton()
                .distance(mouseLocation) <= 15f -> doPackage(window)

//            window.data.clickedMoveSelection &&
//                    stopMoveButtonLocation.toCenterOfStopMoveButton()
//                        .distance(mouseLocation) <= 30f -> {
//                            window.data.clickedMoveSelection = false
//                            window.data.movingNodes = setOf()
//                        }

            else -> return false
        }
        return true
    }

    override fun onKeyEvent(window: NodeSimWindow, key: Int, mods: Int): Boolean {
        when (key) {
            GLFW_KEY_Z -> doMove(window)
            GLFW_KEY_X -> doDelete(window)
            GLFW_KEY_C -> doCopy(window)
            GLFW_KEY_V -> doPackage(window)
//            GLFW_KEY_ESCAPE -> {
//                if (window.data.clickedMoveSelection) {
//                    window.data.clickedMoveSelection = false
//                    window.data.movingNodes = setOf()
//                } else {
//                    return false
//                }
//            }
            else -> return false
        }
        return true
    }

    private fun doMove(window: NodeSimWindow) {
//        window.data.movingNodes = window.data.circuit.getNodesInRegion(
//            min(window.data.selectionLocation1.x, window.data.selectionLocation2.x)..
//                    max(window.data.selectionLocation1.x, window.data.selectionLocation2.x),
//            min(window.data.selectionLocation1.y, window.data.selectionLocation2.y)..
//                    max(window.data.selectionLocation1.y, window.data.selectionLocation2.y)
//        )
//        window.data.clickedMoveSelection = true
    }

    private fun doDelete(window: NodeSimWindow) {
        withYesNo(window, "do you want to delete the selection") {
            runBlocking {
                window.data.circuitMutex.withLock {
                    nodesInSelection.forEach {
                        window.data.circuit.deleteNode(it)
                    }
                }
            }
        }
    }

    private fun doCopy(window: NodeSimWindow) {

    }

    private fun doPackage(window: NodeSimWindow) {
        withYesNo(window, "do you want to package the selection") {
            runBlocking {
                window.data.circuitMutex.withLock {
                    window.data.circuit.packageRegion(
                        min(window.data.selectionLocation1.x, window.data.selectionLocation2.x)..
                                max(window.data.selectionLocation1.x, window.data.selectionLocation2.x),
                        min(window.data.selectionLocation1.y, window.data.selectionLocation2.y)..
                                max(window.data.selectionLocation1.y, window.data.selectionLocation2.y),
                        (window.data.selectionLocation1.x + window.data.selectionLocation2.x) / 2,
                        (window.data.selectionLocation1.y + window.data.selectionLocation2.y) / 2
                    )
                }
            }
        }
    }

    private inline fun withYesNo(window: NodeSimWindow, text: String, crossinline onYes: () -> Unit) {
        setNodesInRegion(window)
//        val yesNoElement = window.hudElementGroup.yesNoElement
//        yesNoElement.text = text
//        yesNoElement.yesCallBack = {
//            onYes()
//            yesNoElement.shouldDraw = true
//        }
//        yesNoElement.noCallBack = {
//            yesNoElement.shouldDraw = true
//        }
//
//        yesNoElement.shouldDraw = false
    }

    private fun setNodesInRegion(window: NodeSimWindow) {
        nodesInSelection =  window.data.circuit.getNodesInRegion(
            min(window.data.selectionLocation1.x, window.data.selectionLocation2.x)..
                    max(window.data.selectionLocation1.x, window.data.selectionLocation2.x),
            min(window.data.selectionLocation1.y, window.data.selectionLocation2.y)..
                    max(window.data.selectionLocation1.y, window.data.selectionLocation2.y)
        )
    }

    private fun setButtonLocations(window: NodeSimWindow) {
        val topLeftX = min(window.data.selectionLocation1.x, window.data.selectionLocation2.x)
        val topLeftY = min(window.data.selectionLocation1.y, window.data.selectionLocation2.y)
//        val (screenX, screenY) = window.getTrueScreenLocation(WorldLocation(topLeftX, topLeftY))
//        val drawOriginX = screenX - 35f
//        val drawOriginY = screenY
//        copyOptionLocation = Point(drawOriginX, drawOriginY)
//        deleteOptionLocation = Point(drawOriginX, drawOriginY + 40f)
//        moveOptionLocation = Point(drawOriginX, drawOriginY + 80f)
//        packageOptionLocation = Point(drawOriginX, drawOriginY + 120f)
    }

    private fun Point.toCenterOfButton(): Point = Point(x + 15f, y + 15f)

    private fun Point.toCenterOfStopMoveButton(): Point = Point(x + 15f, y + 15f)
}