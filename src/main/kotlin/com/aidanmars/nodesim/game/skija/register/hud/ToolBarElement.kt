package com.aidanmars.nodesim.game.skija.register.hud

import com.aidanmars.nodesim.game.skija.*
import com.aidanmars.nodesim.game.skija.constants.Colors
import com.aidanmars.nodesim.game.skija.register.types.actors.DrawAble
import com.aidanmars.nodesim.game.skija.register.types.input.KeyListener
import com.aidanmars.nodesim.game.skija.register.types.input.MouseListener
import com.aidanmars.nodesim.game.skija.types.ToolType
import com.aidanmars.nodesim.game.skija.types.WorldLocation
import io.github.humbleui.skija.Canvas
import io.github.humbleui.skija.svg.SVGDOM
import io.github.humbleui.types.Point

abstract class ToolBarElement : MouseListener, KeyListener, DrawAble {
    abstract val toolType: ToolType?
    abstract val buttonIcon: SVGDOM
    abstract val keyToSelect: Int
    private var clickedWorld = false

    override fun onPress(clickLocation: Point): Boolean {
        if (clickLocation.distance(getButtonPoint()) > 35f) {
            if (data.currentTool == toolType) {
                clickedWorld = true
                onWorldPress(data.worldLocationAt(clickLocation))
                return true
            }
            return false
        }
        if (toolType != null) data.currentTool = toolType!!
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

    override fun draw(canvas: Canvas) {
        drawSelection(canvas)
        val point = getButtonPoint()
        val (x, y) = point
        if (data.currentTool == toolType) {
            canvas.drawCircle(point, 40f, Colors.toolbarElementBorderSelected)
        }
        canvas.drawSvg(
            buttonIcon,
            Point(x - 35f, y - 35f),
            Point(70f, 70f)
        )
    }

    override fun onKeyPress(key: Int, mods: Int): Boolean {
        if (key != keyToSelect) return false
        if (toolType != null) data.currentTool = toolType!!

        return true
    }

    override fun onKeyRelease(key: Int, mods: Int): Boolean {
        return false
    }

    abstract fun drawSelection(canvas: Canvas)

    abstract fun onWorldPress(clickLocation: WorldLocation)

    abstract fun onWorldDrag(newLocation: WorldLocation)

    abstract fun onWorldRelease(clickLocation: WorldLocation)

    abstract fun getButtonPoint(): Point
}