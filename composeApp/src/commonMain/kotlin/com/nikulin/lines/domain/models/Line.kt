package com.nikulin.lines.domain.models


data class Line(
    val key: String,
    val values: Map<Language, Translation>
)
