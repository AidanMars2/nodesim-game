package com.aidanmars.nodesim.game.skija.actors.drawables

import com.aidanmars.nodesim.game.skija.core.NodeSimData
import com.aidanmars.nodesim.game.skija.actors.DrawAble
import com.aidanmars.nodesim.game.skija.component1
import com.aidanmars.nodesim.game.skija.component2
import com.aidanmars.nodesim.game.skija.constants.Colors
import com.aidanmars.nodesim.game.skija.constants.Constants.SIZE_CHUNK
import com.aidanmars.nodesim.game.skija.constants.Constants.SIZE_TILE
import com.aidanmars.nodesim.game.skija.types.WorldLocation
import io.github.humbleui.skija.Canvas
import io.github.humbleui.skija.Paint
import kotlin.math.ceil

class BackGround(override val data: NodeSimData) : DrawAble {
    override fun draw(canvas: Canvas) {
        val tileSize = data.scale * SIZE_TILE
        val (worldTopLeftX, worldTopLeftY) = data.topLeftScreenLocation()

        val worldOriginX = worldTopLeftX - (worldTopLeftX % SIZE_TILE)
        val worldOriginY = worldTopLeftY - (worldTopLeftY % SIZE_TILE)
        val chunkX = (worldOriginX / SIZE_TILE) % SIZE_CHUNK
        val chunkY = (worldOriginY / SIZE_TILE) % SIZE_CHUNK

        val (screenOriginX, screenOriginY) = data.screenPointAt(
            WorldLocation(
                worldOriginX,
                worldOriginY
            )
        )

        val numTilesX = (ceil(data.windowWidth / tileSize).toInt() + 2)
        val numTilesY = (ceil(data.windowHeight / tileSize).toInt() + 2)
        val xMax = data.windowWidth.toFloat()
        val yMax = data.windowHeight.toFloat()
        // draw tile lines
        if (data.scale >= 0.125f) {
            Paint().use {
                it.color = Colors.tileLine
                it.strokeWidth = 2f * data.scale

                var x = screenOriginX
                for (index in 0..numTilesX) {
                    canvas.drawLine(x, 0f, x, yMax, it)
                    x += tileSize
                }
                var y = screenOriginY
                for (index in 0..numTilesY) {
                    canvas.drawLine(0f, y, xMax, y, it)
                    y += tileSize
                }
            }
        }
        Paint().use {
            it.color = Colors.chunkLine
            it.strokeWidth = 3f * data.scale

            val chunkSize = tileSize * SIZE_CHUNK
            var x = screenOriginX - (chunkX * tileSize)
            for (index in 0..(numTilesX / SIZE_CHUNK)) {
                canvas.drawLine(x, 0f, x, yMax, it)
                x += chunkSize
            }
            var y = screenOriginY - (chunkY * tileSize)
            for (index in 0..(numTilesX / SIZE_CHUNK)) {
                canvas.drawLine(0f, y, xMax, y, it)
                y += chunkSize
            }
        }
        Paint().use {
            it.color = Colors.zeroZeroLine
            it.strokeWidth = 6f
            if (data.scale > 1f) it.strokeWidth *= data.scale

            val (screenX, screenY) = data.screenPointAt(WorldLocation(0, 0))
            if (screenX.toInt() in -3..data.windowWidth) {
                canvas.drawLine(screenX, 0f, screenX, yMax, it)
            }
            if (screenY.toInt() in -3..data.windowHeight) {
                canvas.drawLine(0f, screenY, xMax, screenY, it)
            }
        }
    }
}