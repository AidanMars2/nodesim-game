package com.aidanmars.nodesim.game.skija.constants

import io.github.humbleui.skija.Color

object Colors {
    // background
    val background = Color.makeRGB(192, 192, 192)
    val tileLine = Color.makeRGB(128, 128, 128)
    val chunkLine = Color.makeRGB(64, 64, 64)
    val zeroZeroLine = Color.makeRGB(0, 0, 0)

    // hud elements
    val toolbarElementBorderSelected = Color.makeRGB(0, 255, 255)
    val exitElementMain = Color.makeRGB(0, 0, 0)
    val snapElementMain = Color.makeRGB(255, 255, 255)

    // wire colors
    val wireStructure = Color.makeRGB(0, 0, 0)
    val wireOffInner = Color.makeRGB(64, 64, 64)
    val wireOnInner = Color.makeRGB(255, 255, 0)
}