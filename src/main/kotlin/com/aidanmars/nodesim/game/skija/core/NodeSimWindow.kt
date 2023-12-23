package com.aidanmars.nodesim.game.skija.core

import com.aidanmars.nodesim.game.skija.register.types.RegisterAble
import com.aidanmars.nodesim.game.skija.Window
import com.aidanmars.nodesim.game.skija.constants.Colors
import com.aidanmars.nodesim.game.skija.core.registers.NodeSimActorHandler
import com.aidanmars.nodesim.game.skija.core.registers.NodeSimInputHandler
import com.aidanmars.nodesim.game.skija.core.registers.NodeSimRenderer
import com.aidanmars.nodesim.game.skija.register.types.actors.ConstantActor
import com.aidanmars.nodesim.game.skija.register.types.actors.DrawAble
import com.aidanmars.nodesim.game.skija.register.types.data.DataListener
import com.aidanmars.nodesim.game.skija.register.types.input.InputListener
import io.github.humbleui.skija.Canvas
import io.github.humbleui.types.IRect
import io.github.humbleui.types.Point
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWErrorCallback
import kotlin.math.*

//TODO: circuit saving/blueprints
//TODO: advanced circuit manipulation(copying, pasting, cutting, mass deletion, packaging)
//TODO: undo/redo
class NodeSimWindow : Window("NodeSim") {
    val data = NodeSimData(::closeGraceFully)
    private val inputHandler = NodeSimInputHandler()
    private val renderer = NodeSimRenderer()
    private val actorActor = NodeSimActorHandler()

    override fun onKeyPress(window: Long, key: Int, scanCode: Int, action: Int, mods: Int) {
        inputHandler.onKeyEvent(action, key, mods)
    }

    override fun onScroll(window: Long, xOffset: Double, yOffset: Double) {
        inputHandler.onScrollEvent(xOffset, yOffset)
    }

    override fun onMouseButtonEvent(window: Long, button: Int, action: Int, mods: Int) {
        inputHandler.onMouseButtonEvent(action, button, Point(mouseX.toFloat(), mouseY.toFloat()))
    }

    override fun onFocusEvent(window: Long, focused: Boolean) {
        // make sure the mouse release function is called
        inputHandler.onMouseButtonEvent(GLFW_RELEASE, GLFW_MOUSE_BUTTON_1, Point(mouseX.toFloat(), mouseY.toFloat()))
    }

    override fun onMouseMoveEvent(newMousePoint: Point) {
        inputHandler.onMouseMoveEvent(newMousePoint)
    }

    override fun init() {
        fillRegister()
        data.init()
    }

    override fun terminate() {
        data.terminate()
        //TODO: see if the renderer and inputHandler need terminate functions
    }

    override fun draw(canvas: Canvas) {
        actorActor.act()
        data.windowWidth = width
        data.windowHeight = height
        canvas.clear(Colors.background)
        renderer.draw(canvas)
    }

    fun register(registerAble: RegisterAble) {
        if (registerAble is ConstantActor) actorActor.register(registerAble)
        if (registerAble is DrawAble) renderer.register(registerAble)
        if (registerAble is DataListener) data.dataListenerHandler.register(registerAble)
        if (registerAble is InputListener) inputHandler.register(registerAble)
    }

    fun runGame() {
        GLFWErrorCallback.createPrint(System.err).set()
        check(glfwInit()) { "Unable to initialize GLFW" }

        val videoMode = glfwGetVideoMode(glfwGetPrimaryMonitor())
        val width = (videoMode!!.width() * 0.75).toInt()
        val height = (videoMode.height() * 0.75).toInt()
        val bounds = IRect.makeXYWH(
            max(0, (videoMode.width() - width) / 2),
            max(0, (videoMode.height() - height) / 2),
            width,
            height
        )
        run(bounds)
    }

    fun closeGraceFully() {
        glfwSetWindowShouldClose(window, true)
    }
}