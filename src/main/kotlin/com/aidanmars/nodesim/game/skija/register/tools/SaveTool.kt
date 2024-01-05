package com.aidanmars.nodesim.game.skija.register.tools

import com.aidanmars.nodesim.core.extensions.advancedDeserializeCircuit
import com.aidanmars.nodesim.core.extensions.advancedSerialize
import com.aidanmars.nodesim.game.skija.core.NodeSimData
import com.aidanmars.nodesim.game.skija.register.types.input.KeyListener
import org.lwjgl.glfw.GLFW.*
import java.io.File
import java.io.IOException

class SaveTool(override val data: NodeSimData) : KeyListener {
    override fun onKeyPress(key: Int, mods: Int): Boolean {
        if (mods and GLFW_MOD_CONTROL == 0) return false
        when (key) {
            GLFW_KEY_S -> {
                val file = File("./gamecircuit.dat")
                file.createNewFile()
                data.withCircuitMutexLocked { data.circuit.advancedSerialize(file.outputStream()) }
            }
            GLFW_KEY_L -> {
                val file = File("./gamecircuit.dat")
                if (!file.canRead()) return true
                data.withCircuitMutexLocked {
                    try {
                        data.circuit = advancedDeserializeCircuit(file.inputStream())
                    } catch (_: IOException) {}
                    catch (_: IllegalArgumentException) {}
                }
            }
            else -> return false
        }
        return true
    }

    override fun onKeyRelease(key: Int, mods: Int): Boolean {
        return false
    }
}