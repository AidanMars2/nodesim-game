package com.aidanmars.nodesim.game.skija.core.registers

import com.aidanmars.nodesim.game.skija.register.types.input.*
import io.github.humbleui.types.Point
import org.lwjgl.glfw.GLFW.*

class NodeSimInputHandler {
    private val mouseListeners = mutableListOf<MouseListener>()

    /**
     * this value is to indicate what listener is listening to mouse events
     */
    private var currentSelectedMouseListener: MouseListener? = null
    private var currentSelectedListenerLayer: ClickLayer = ClickLayer.World
    private val keyListeners = mutableListOf<KeyListener>()
    private val scrollListeners = mutableListOf<ScrollListener>()

    fun onKeyEvent(action: Int, key: Int, mods: Int) {
        keyListeners.forEach {
            when (action) {
                GLFW_PRESS -> if (it.onKeyPress(key, mods)) return
                GLFW_RELEASE -> if (it.onKeyRelease(key, mods)) return
            }
        }
    }

    fun onMouseButtonEvent(action: Int, button: Int, location: Point) {
        if (button != GLFW_MOUSE_BUTTON_1) return
        when (action) {
            GLFW_PRESS -> {
                currentSelectedMouseListener = null
                mouseListeners.forEach {
                    if (it.onPress(location, ClickLayer.Overlay)) {
                        currentSelectedListenerLayer = ClickLayer.Overlay
                        currentSelectedMouseListener = it
                        return
                    }
                }
                mouseListeners.forEach {
                    if (it.onPress(location, ClickLayer.Hud)) {
                        currentSelectedListenerLayer = ClickLayer.Hud
                        currentSelectedMouseListener = it
                        return
                    }
                }
                mouseListeners.forEach {
                    if (it.onPress(location, ClickLayer.World)) {
                        currentSelectedListenerLayer = ClickLayer.World
                        currentSelectedMouseListener = it
                        return
                    }
                }
            }
            GLFW_RELEASE -> {
                currentSelectedMouseListener?.onRelease(location, currentSelectedListenerLayer)
                currentSelectedMouseListener = null
            }
        }
    }

    fun onMouseMoveEvent(newLocation: Point) {
        currentSelectedMouseListener?.onDrag(newLocation, currentSelectedListenerLayer)
    }

    fun onScrollEvent(xOffset: Double, yOffset: Double) {
        scrollListeners.forEach {
            if (it.onScroll(xOffset, yOffset)) return
        }
    }

    fun register(listener: InputListener) {
        if (listener is KeyListener) keyListeners.add(listener)
        if (listener is MouseListener) mouseListeners.add(listener)
        if (listener is ScrollListener) scrollListeners.add(listener)
    }
}