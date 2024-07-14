package com.nikulin.lines.domain.models

data class Line(
    val key: String,
    val values: List<Translation>
)

data class Line1(
    val key: String,
    val values: Map<Language, Translation>
)
