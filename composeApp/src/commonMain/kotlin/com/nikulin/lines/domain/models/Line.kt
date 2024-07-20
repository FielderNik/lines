package com.nikulin.lines.domain.models


data class Line(
    val key: String,
    val values: MutableMap<Language, Translation>
)

fun MutableList<Line>.addLine(line: Line) {
    val foundLine = this.find { it.key == line.key }
    if (foundLine != null) {
        foundLine.values.putAll(line.values)
    } else {
        this.add(line)
    }
}
