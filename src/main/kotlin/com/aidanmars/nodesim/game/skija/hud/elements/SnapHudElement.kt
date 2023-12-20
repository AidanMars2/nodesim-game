package com.aidanmars.nodesim.game.skija.hud.elements

import com.aidanmars.nodesim.game.skija.*
import com.aidanmars.nodesim.game.skija.constants.Colors
import com.aidanmars.nodesim.game.skija.hud.HudElement
import io.github.humbleui.skija.Canvas
import io.github.humbleui.skija.Paint
import io.github.humbleui.skija.PaintStrokeCap
import io.github.humbleui.types.Point

class SnapHudElement: HudElement {
    override var isHidden: Boolean = false
    override var isFocused: Boolean = false
    var cycleStage = 0
        set(value) {field = value % 3}

    fun getSnapDistance(): Int {
        return when (cycleStage) {
            0 -> 1
            1 -> 40
            2 -> 20
            else -> 40
        }
    }

    override fun draw(window: NodeSimWindow, canvas: Canvas) {
        val point = getButtonPoint(window)
        val (x, y) = point
        canvas.drawCircle(point, 35f, Colors.tileLine)
        canvas.drawCircle(point, 30f, Colors.snapElementMain)
        val squareLeftX = x - 18f
        val squareRightX = x + 18f
        val squareTopY = y - 18f
        val squareBottomY = y + 18f
        Paint().use {
            it.color = Colors.tileLine
            it.strokeWidth = 2.5f
            if (cycleStage > 0) {
                canvas.drawLine(x, squareTopY, x, squareBottomY, it)
                canvas.drawLine(squareLeftX, y, squareRightX, y, it)
            }
            if (cycleStage > 1) {
                val halfRightX = x + 9f
                val halfLeftX = x - 9f
                val halfTopY = y - 9f
                val halfBottomY = y + 9f
                canvas.drawLine(squareLeftX, halfTopY, squareRightX, halfTopY, it)
                canvas.drawLine(squareLeftX, halfBottomY, squareRightX, halfBottomY, it)
                canvas.drawLine(halfLeftX, squareTopY, halfLeftX, squareBottomY, it)
                canvas.drawLine(halfRightX, squareBottomY, halfRightX, squareTopY, it)
            }
        }
        Paint().use {
            it.color = Colors.chunkLine
            it.strokeWidth = 3f
            it.strokeCap = PaintStrokeCap.SQUARE
            canvas.drawLine(squareLeftX, squareTopY, squareRightX, squareTopY, it)
            canvas.drawLine(squareLeftX, squareBottomY, squareRightX, squareBottomY, it)
            canvas.drawLine(squareLeftX, squareTopY, squareLeftX, squareBottomY, it)
            canvas.drawLine(squareRightX, squareBottomY, squareRightX, squareTopY, it)
        }
    }

    override fun onClick(window: NodeSimWindow, mouseLocation: Point): Boolean {
        if (getButtonPoint(window).distance(mouseLocation) > 35f) return false
        cycleStage += 1
        window.data.placeSnapDistance = getSnapDistance()
        return true
    }

    override fun onKeyEvent(window: NodeSimWindow, key: Int, mods: Int): Boolean {
        isFocused = false
        return false
    }

    private fun getButtonPoint(window: NodeSimWindow): Point {
        return Point(window.width - 60f, 200f)
    }
}