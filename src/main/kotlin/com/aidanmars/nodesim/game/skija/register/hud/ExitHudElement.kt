package com.aidanmars.nodesim.game.skija.register.hud

import com.aidanmars.nodesim.game.skija.component1
import com.aidanmars.nodesim.game.skija.component2
import com.aidanmars.nodesim.game.skija.constants.Colors
import com.aidanmars.nodesim.game.skija.core.NodeSimData
import com.aidanmars.nodesim.game.skija.distance
import com.aidanmars.nodesim.game.skija.register.types.actors.DrawAble
import com.aidanmars.nodesim.game.skija.register.types.actors.HudDrawAble
import com.aidanmars.nodesim.game.skija.register.types.input.ClickLayer
import com.aidanmars.nodesim.game.skija.register.types.input.MouseListener
import io.github.humbleui.skija.Canvas
import io.github.humbleui.skija.Paint
import io.github.humbleui.skija.PaintStrokeCap
import io.github.humbleui.types.Point

class ExitHudElement(override val data: NodeSimData) : MouseListener, HudDrawAble {
    override fun drawHud(canvas: Canvas) {

        val (x, y) = getButtonPoint()
        Paint().use {
            it.color = Colors.exitElementMain
            it.strokeWidth = 10f
            it.strokeCap = PaintStrokeCap.ROUND
            canvas.drawLine(x + 40f, y + 40f, x - 40f, y - 40f, it)
            canvas.drawLine(x + 40f, y - 40f, x - 40f, y + 40f, it)
        }
    }

    override fun onPress(clickLocation: Point, clickLayer: ClickLayer): Boolean {
        if (clickLayer != ClickLayer.Hud) return false
        if (clickLocation.distance(getButtonPoint()) > 60f) return false
        data.closeGraceFully()
        return true
    }

    override fun onRelease(clickLocation: Point, clickLayer: ClickLayer) {}

    override fun onDrag(newLocation: Point, clickLayer: ClickLayer) {}

    private fun getButtonPoint() = Point(data.windowWidth - 60f, 60f)
}