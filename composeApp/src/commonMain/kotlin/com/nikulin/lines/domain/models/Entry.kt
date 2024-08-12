package com.nikulin.lines.domain.models

interface Entry {
    data object Empty : Entry
    data class Data(val key: String) : Entry
    data class SystemData(val string: String) : Entry
}