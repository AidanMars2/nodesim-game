package com.aidanmars.nodesim.game.skija.register.tools

import com.aidanmars.nodesim.game.skija.*
import com.aidanmars.nodesim.game.skija.constants.Colors
import com.aidanmars.nodesim.game.skija.register.types.actors.DrawAble
import com.aidanmars.nodesim.game.skija.register.types.actors.HudDrawAble
import com.aidanmars.nodesim.game.skija.register.types.actors.WorldDrawAble
import com.aidanmars.nodesim.game.skija.register.types.input.ClickLayer
import com.aidanmars.nodesim.game.skija.register.types.input.KeyListener
import com.aidanmars.nodesim.game.skija.register.types.input.MouseListener
import com.aidanmars.nodesim.game.skija.types.ToolType
import com.aidanmars.nodesim.game.skija.types.WorldLocation
import io.github.humbleui.skija.Canvas
import io.github.humbleui.skija.svg.SVGDOM
import io.github.humbleui.types.Point

abstract class NodeSimTool : MouseListener, KeyListener, HudDrawAble, WorldDrawAble {
    abstract val toolType: ToolType
    abstract val buttonIcon: SVGDOM
    abstract val keyToSelect: Int

    override fun onPress(clickLocation: Point, clickLayer: ClickLayer): Boolean {
        when (clickLayer) {
            ClickLayer.Overlay -> return false
            ClickLayer.Hud -> {
                if (clickLocation.distance(getButtonPoint()) > 35f) return false

                data.currentTool = toolType
                return true
            }
            ClickLayer.World -> {
                if (data.currentTool == toolType) {
                    onWorldPress(data.worldLocationAt(clickLocation))
                    return true
                }
            }
        }
        return false
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

    override fun drawHud(canvas: Canvas) {
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

    override fun drawWorld(canvas: Canvas) {
        if (data.currentTool == toolType) drawSelection(canvas)
    }

    override fun onKeyPress(key: Int, mods: Int): Boolean {
        if (key != keyToSelect) return false
        data.currentTool = toolType

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