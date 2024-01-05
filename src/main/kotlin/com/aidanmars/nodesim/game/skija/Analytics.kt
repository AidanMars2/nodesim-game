package com.aidanmars.nodesim.game.skija

import com.aidanmars.nodesim.game.skija.types.Folder
import kotlin.time.Duration

class Analytics {
    private val durations = Folder<Duration>("total")

    inline fun <V> measureTime(key: String, block: () -> V): V {
        val result: V
        val time = kotlin.time.measureTime {
            result = block()
        }
        measureTime(key, time)
        return result
    }

    fun measureTime(key: String, duration: Duration) {
        durations[key] = (durations[key] ?: Duration.ZERO) + duration
    }

    fun print() {
        println("analytics:")
        printFolder(0, durations)
        durations.subFolders.clear()
        durations.files.clear()
    }

    private fun printFolder(indent: Int, folder: Folder<Duration>) {
        val indentString = String(CharArray(indent) { ' ' })
        folder.files.forEach { (key, duration) ->
            println("$indentString- $key: $duration")
        }
        folder.subFolders.forEach { (name, subFolder) ->
            println("$indentString- $name: ${subFolder.files.values
                .reduceOrNull { acc, duration -> 
                    acc + duration 
                } ?: Duration.ZERO}"
            )
            printFolder(indent + 1, subFolder)
        }
    }
}