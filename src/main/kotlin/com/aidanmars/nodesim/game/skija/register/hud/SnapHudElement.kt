package com.aidanmars.nodesim.game.skija.register.hud

import com.aidanmars.nodesim.game.skija.component1
import com.aidanmars.nodesim.game.skija.component2
import com.aidanmars.nodesim.game.skija.constants.Colors
import com.aidanmars.nodesim.game.skija.core.NodeSimData
import com.aidanmars.nodesim.game.skija.distance
import com.aidanmars.nodesim.game.skija.drawCircle
import com.aidanmars.nodesim.game.skija.register.types.actors.DrawAble
import com.aidanmars.nodesim.game.skija.register.types.input.MouseListener
import io.github.humbleui.skija.Canvas
import io.github.humbleui.skija.Paint
import io.github.humbleui.skija.PaintStrokeCap
import io.github.humbleui.types.Point

class SnapHudElement(override val data: NodeSimData) : MouseListener, DrawAble {
    private var cycleStage = 0

    private fun getSnapDistance(): Int {
        return when (cycleStage) {
            0 -> 1
            1 -> 40
            2 -> 20
            else -> 40
        }
    }

    override fun draw(canvas: Canvas) {
        val point = getButtonPoint()
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

    override fun onPress(clickLocation: Point): Boolean {
        if (getButtonPoint().distance(clickLocation) > 35f) return false
        cycleStage += 1
        cycleStage %= 3
        data.placeSnapDistance = getSnapDistance()
        return true
    }

    override fun onDrag(newLocation: Point) {}

    override fun onRelease(clickLocation: Point) {}

    private fun getButtonPoint() = Point(data.windowWidth - 60f, 180f)
}