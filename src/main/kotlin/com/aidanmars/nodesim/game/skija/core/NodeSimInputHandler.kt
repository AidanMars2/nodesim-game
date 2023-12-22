package com.aidanmars.nodesim.game.skija.core

import com.aidanmars.nodesim.game.skija.listeners.KeyListener
import com.aidanmars.nodesim.game.skija.listeners.MouseListener
import com.aidanmars.nodesim.game.skija.listeners.ScrollListener
import io.github.humbleui.types.Point
import org.lwjgl.glfw.GLFW.*

class NodeSimInputHandler {
    val mouseListeners = mutableListOf<MouseListener>()

    /**
     * this value is to indicate what listener is listening to mouse events
     */
    private var currentSelectedMouseListener: MouseListener? = null
    val keyListeners = mutableListOf<KeyListener>()
    val scrollListeners = mutableListOf<ScrollListener>()

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
                    if (it.onPress(location)) {
                        currentSelectedMouseListener = it
                        return
                    }
                }
            }
            GLFW_RELEASE -> {
                currentSelectedMouseListener?.onRelease(location)
                currentSelectedMouseListener = null
            }
        }
    }

    fun onMouseMoveEvent(newLocation: Point) {
        currentSelectedMouseListener?.onDrag(newLocation)
    }

    fun onScrollEvent(xOffset: Double, yOffset: Double) {
        scrollListeners.forEach {
            if (it.onScroll(xOffset, yOffset)) return
        }
    }

    fun init(data: NodeSimData) {
        //TODO: write init code including listener registration
    }
}