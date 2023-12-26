package com.aidanmars.nodesim.game.skija.register

import com.aidanmars.nodesim.game.skija.constants.SvgDoms
import com.aidanmars.nodesim.game.skija.core.NodeSimData
import com.aidanmars.nodesim.game.skija.distance
import com.aidanmars.nodesim.game.skija.drawSvg
import com.aidanmars.nodesim.game.skija.register.types.actors.OverlayDrawAble
import com.aidanmars.nodesim.game.skija.register.types.input.ClickLayer
import com.aidanmars.nodesim.game.skija.register.types.input.KeyListener
import com.aidanmars.nodesim.game.skija.register.types.input.MouseListener
import io.github.humbleui.skija.Canvas
import io.github.humbleui.skija.Color
import io.github.humbleui.skija.Paint
import io.github.humbleui.types.Point
import io.github.humbleui.types.Rect
import org.lwjgl.glfw.GLFW.*

class VerificationHandler(override val data: NodeSimData) :
    OverlayDrawAble,
    KeyListener, MouseListener
{
    override fun drawOverlay(canvas: Canvas) {
        if (!data.isAskingVerification) return
        Paint().use {
            it.color = Color.makeARGB(128, 0, 0, 0)
            canvas.drawRect(
                Rect.makeXYWH(0f, 0f, data.windowWidth.toFloat(), data.windowHeight.toFloat()),
                it
            )
        }
        canvas.drawSvg(
            SvgDoms.Hud.YesNo.yesButton,
            yesButtonLocation().offset(-100f, -100f),
            Point(200f, 200f)
        )
        canvas.drawSvg(
            SvgDoms.Hud.YesNo.noButton,
            noButtonLocation().offset(-100f, -100f),
            Point(200f, 200f)
        )
    }

    override fun onKeyPress(key: Int, mods: Int): Boolean {
        if (!data.isAskingVerification) return false
        if (key == GLFW_KEY_ESCAPE) data.answerVerification(false)

        return true
    }

    override fun onKeyRelease(key: Int, mods: Int): Boolean {
        return data.isAskingVerification
    }

    override fun onPress(clickLocation: Point, clickLayer: ClickLayer): Boolean {
        return data.isAskingVerification
    }

    override fun onRelease(clickLocation: Point, clickLayer: ClickLayer) {
        when {
            yesButtonLocation().distance(clickLocation) < 100f -> {
                data.answerVerification(true)
            }
            noButtonLocation().distance(clickLocation) < 100f -> {
                data.answerVerification(false)
            }
        }
    }

    override fun onDrag(newLocation: Point, clickLayer: ClickLayer) {}

    private fun yesButtonLocation() = Point((data.windowWidth / 2f) - 250f, data.windowHeight / 2f)

    private fun noButtonLocation() = Point((data.windowWidth / 2f) + 250f, data.windowHeight / 2f)
}