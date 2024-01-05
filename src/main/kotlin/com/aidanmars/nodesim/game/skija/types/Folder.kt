package com.aidanmars.nodesim.game.skija.types

class Folder<V>(
    val name: String
) {
    val subFolders: MutableMap<String, Folder<V>> = mutableMapOf()
    val files: MutableMap<String, V> = mutableMapOf()
    
    operator fun get(key: String): V? {
        val split = key.split('.')
        return accessFolder(split.subList(0, split.lastIndex)).files[split.last()]
    }

    operator fun set(key: String, value: V) {
        val split = key.split('.')
        accessFolder(split.subList(0, split.lastIndex)).files[split.last()] = value
    }

    private fun accessFolder(chain: List<String>): Folder<V> {
        var currentFolder = this
        chain.forEach {
            currentFolder = currentFolder.subFolders.getOrPut(it) { Folder(it) }
        }
        return currentFolder
    }
}