package com.aidanmars.nodesim.game.skija

import io.github.humbleui.skija.Color

object Colors {
    // background
    val background = Color.makeRGB(192, 192, 192)
    val tileLine = Color.makeRGB(128, 128, 128)
    val chunkLine = Color.makeRGB(64, 64, 64)
    val zeroZeroLine = Color.makeRGB(0, 0, 0)

    // hud elements
    val toolbarElementBorderSelected = Color.makeRGB(0, 255, 255)
    val placeElementMain = Color.makeRGB(0, 192, 0)
    val placeElementBorder = Color.makeRGB(0, 96, 0)
    val placeElementPlus = Color.makeRGB(0, 0, 0)
    val deleteElementMain = Color.makeRGB(255, 0, 0)
    val deleteElementBorder = Color.makeRGB(128, 0, 0)
    val deleteElementCross = Color.makeRGB(255, 255, 255)
    val interactElementMain = Color.makeRGB(0, 128, 255)
    val interactElementBorder = Color.makeRGB(0, 64, 128)
    val interactElementIcon = Color.makeRGB(255, 255, 255)
    val exitElementMain = Color.makeRGB(0, 0, 0)
    val connectElementMain = Color.makeRGB(128, 128, 128)
    val connectElementBorder = Color.makeRGB(42, 42, 42)
    val connectElementIcon = Color.makeRGB(0, 0, 0)
    val snapElementMain = Color.makeRGB(255, 255, 255)

    // node colors
    val nodeEdge = Color.makeRGB(0, 0, 0)
    val nodeStructure = Color.makeRGB(192, 192, 192)
    //switch
    val switchOff = Color.makeRGB(188, 0, 0)
    val switchOnInner = Color.makeRGB(188, 125, 0)
    val switchOnOuter = Color.makeRGB(255, 0, 0)
    val switchOnGlowInner = switchOnOuter
    val switchOnGlowOuter = Color.withA(switchOnGlowInner, 0)
    //light
    val lightOff = Color.makeRGB(51, 51, 51)
    val lightOnInner = Color.makeRGB(255, 255, 255)
    val lightOnOuter = Color.makeRGB(255, 229, 0)
    val lightOnGlowInner = lightOnOuter
    val lightOnGlowOuter = Color.withA(lightOnGlowInner, 0)
    //nor gate
    val norGateOff = Color.makeRGB(0, 100, 132)
    val norGateOn = Color.makeRGB(0, 255, 255)
    val norGateOnGlowInner = norGateOn
    val norGateOnGlowOuter = Color.withA(norGateOnGlowInner, 0)
    //and gate
    val andGateOff = Color.makeRGB(137, 0, 73)
    val andGateOn = Color.makeRGB(255, 0, 135)
    val andGateOnGlowInner = andGateOn
    val andGateOnGlowOuter = Color.withA(andGateOnGlowInner, 0)
    //xor gate
    val xorGateOff = Color.makeRGB(0, 102, 0)
    val xorGateOn = Color.makeRGB(0, 255, 0)
    val xorGateOnGlowInner = xorGateOn
    val xorGateOnGlowOuter = Color.withA(xorGateOnGlowInner, 0)

    // wire colors
    val wireStructure = Color.makeRGB(0, 0, 0)
    val wireOffInner = Color.makeRGB(64, 64, 64)
    val wireOnInner = Color.makeRGB(255, 255, 0)
}