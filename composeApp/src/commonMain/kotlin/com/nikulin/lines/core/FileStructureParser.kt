package com.nikulin.lines.core

import com.nikulin.lines.domain.models.Entry
import io.github.vinceglb.filekit.core.PlatformFile

class FileStructureParser {

    private val keyTag = "<string name=\""

    suspend fun parseStructure(file: PlatformFile): Map<Int, Entry> {
        val fileLines = file.readBytes().decodeToString().lines()
        val map = mutableMapOf<Int, Entry>()

        fileLines.forEachIndexed { index, str ->
            val entry = when  {
                str.isBlank() -> Entry.Empty
                str.containsKey() -> Entry.Data(str.parseKey())
                else -> Entry.SystemData(str)
            }
            map[index] = entry
        }
        return map
    }

    private fun String.containsKey(): Boolean {
        return this.contains(keyTag)
    }

    private fun String.parseKey(): String {
        return this.substringAfter(keyTag).substringBefore("\"")
    }

}