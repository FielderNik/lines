package com.nikulin.lines.core

import com.mohamedrejeb.ksoup.html.parser.KsoupHtmlHandler
import com.mohamedrejeb.ksoup.html.parser.KsoupHtmlParser
import com.nikulin.lines.domain.models.Language
import com.nikulin.lines.domain.models.Line
import com.nikulin.lines.domain.models.Translation
import io.github.vinceglb.filekit.core.PlatformFile

class LinesParser {

    suspend fun parseLines(file: PlatformFile, language: Language): List<Line> {
        var canParse = false
        val lines: MutableList<Line> = mutableListOf()
        var line: Line? = null

        val handler = KsoupHtmlHandler
            .Builder()
            .onOpenTag { name, attributes, isImplied ->
                if (name == "string") {
                    val parsedKey = attributes["name"]
                    if (!parsedKey.isNullOrEmpty()) {
                        canParse = true
                        line = Line(key = parsedKey, values = mutableMapOf())
                    }
                }
            }
            .onText { text ->
                var currentLine = line
                if (canParse && currentLine != null) {
                    currentLine = currentLine.copy(
                        values = mutableMapOf(language to Translation(value = text))
                    )
                    lines.add(currentLine)
                    line = null
                    canParse = false
                }
            }
            .build()
        val ksoupHtmlParser = KsoupHtmlParser(handler = handler)
        val fileContent = file.readBytes().decodeToString()
        ksoupHtmlParser.write(fileContent)
        ksoupHtmlParser.end()
        return lines
    }
}