package com.aidanmars.nodesim.game.skija

import io.github.humbleui.skija.*
import io.github.humbleui.skija.impl.*
import io.github.humbleui.types.*
import org.lwjgl.glfw.Callbacks.*
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.opengl.*
import org.lwjgl.system.MemoryUtil.*
import java.util.*


abstract class Window(val title: String) {
    var window: Long = 0
    var width: Int = 0
    var height: Int = 0
    var dpi: Float = 1f
    var xpos: Int = 0
    var ypos: Int = 0
    var vsync: Boolean = true
    var stats: Boolean = true
    private lateinit var refreshRates: IntArray
    private val os = System.getProperty("os.name").lowercase(Locale.getDefault())

    private fun getRefreshRates(): IntArray {
        val monitors = glfwGetMonitors()
        val res = IntArray(monitors!!.capacity())
        for (i in 0 until monitors.capacity()) {
            res[i] = glfwGetVideoMode(monitors[i])!!.refreshRate()
        }
        return res
    }

    fun run(bounds: IRect) {
        refreshRates = getRefreshRates()

        createWindow(bounds)
        loop()

        glfwFreeCallbacks(window)
        glfwDestroyWindow(window)
        glfwTerminate()
        glfwSetErrorCallback(null)!!.free()
    }

    private fun updateDimensions() {
        val width = IntArray(1)
        val height = IntArray(1)
        glfwGetFramebufferSize(window, width, height)

        val xscale = FloatArray(1)
        val yscale = FloatArray(1)
        glfwGetWindowContentScale(window, xscale, yscale)
        assert(xscale[0] == yscale[0]) { "Horizontal dpi=" + xscale[0] + ", vertical dpi=" + yscale[0] }
        this.width = (width[0] / xscale[0]).toInt()
        this.height = (height[0] / yscale[0]).toInt()
        this.dpi = xscale[0]
        //println("FramebufferSize ${width[0]}x${height[0]}, scale ${this.dpi}, window ${this.width}x${this.height}")
    }

    private fun createWindow(bounds: IRect) {
        glfwDefaultWindowHints() // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE) // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE) // the window will be resizable

        window = glfwCreateWindow(bounds.width, bounds.height, title, NULL, NULL)
        if (window == NULL) throw RuntimeException("Failed to create the GLFW window")

        glfwSetKeyCallback(
            window
        ) { window: Long, key: Int, scancode: Int, action: Int, mods: Int ->
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) glfwSetWindowShouldClose(window, true)
        }

        glfwSetWindowPos(window, bounds.left, bounds.top)
        updateDimensions()
        xpos = width / 2
        ypos = height / 2

        glfwMakeContextCurrent(window)
        glfwSwapInterval(if (vsync) 1 else 0) // Enable v-sync
        glfwShowWindow(window)
    }

    private var context: DirectContext? = null
    private var renderTarget: BackendRenderTarget? = null
    private var surface: Surface? = null
    private var canvas: Canvas? = null

    private fun initSkia() {
        Stats.enabled = true

        if (surface != null) surface!!.close()
        if (renderTarget != null) renderTarget!!.close()

        renderTarget = BackendRenderTarget.makeGL(
            (width * dpi).toInt(),
            (height * dpi).toInt(),  /*samples*/
            0,  /*stencil*/
            8,  /*fbId*/
            0,
            FramebufferFormat.GR_GL_RGBA8
        )

        surface = Surface.wrapBackendRenderTarget(
            context!!,
            renderTarget!!,
            SurfaceOrigin.BOTTOM_LEFT,
            SurfaceColorFormat.RGBA_8888,
            ColorSpace.getDisplayP3(),  // TODO load monitor profile
            SurfaceProps(PixelGeometry.RGB_H)
        )

        canvas = surface!!.canvas
    }

    abstract fun draw(canvas: Canvas)

    private fun loop() {
        GL.createCapabilities()
        if ("false" == System.getProperty("skija.staticLoad")) Library.load()
        context = DirectContext.makeGL()

        glfwSetWindowSizeCallback(window) { window: Long, width: Int, height: Int ->
            updateDimensions()
            initSkia()
            draw(canvas!!)
        }

        glfwSetCursorPosCallback(window) { window: Long, xpos: Double, ypos: Double ->
            if (os.contains("mac") || os.contains("darwin")) {
                this.xpos = xpos.toInt()
                this.ypos = ypos.toInt()
            } else {
                this.xpos = (xpos / dpi).toInt()
                this.ypos = (ypos / dpi).toInt()
            }
        }

        glfwSetMouseButtonCallback(window, ::onMouseButtonEvent)

        glfwSetScrollCallback(window, ::onScroll)

        glfwSetKeyCallback(window, ::onKeyPress)
        initSkia()

        while (!glfwWindowShouldClose(window)) {
            draw(canvas!!)
            context!!.flush()
            glfwSwapBuffers(window)
            glfwPollEvents()
        }
    }

    abstract fun onKeyPress(window: Long, key: Int, scanCode: Int, action: Int, mods: Int)

    abstract fun onScroll(window: Long, xOffset: Double, yOffset: Double)

    abstract fun onMouseButtonEvent(window: Long, button: Int, action: Int, mods: Int)
}