package com.nikulin.lines.domain.repositories

import com.nikulin.lines.domain.models.Language
import com.nikulin.lines.domain.models.Line
import com.nikulin.lines.domain.models.Line1
import com.nikulin.lines.domain.models.Translation

interface LinesRepository {
    suspend fun getLines(): Result<List<Line>>
    suspend fun hasLines(): Result<Boolean>
}

class LinesRepositoryImpl : LinesRepository {
    override suspend fun getLines(): Result<List<Line>> {
        return Result.success(mock)
    }

    override suspend fun hasLines(): Result<Boolean> {
        return Result.success(mock.isNotEmpty())
    }

}

val mock = listOf(
    Line(
        key = "action",
        values = listOf(
            Translation(
                value = "действие",
                language = Language("ru")
            )
        )
    )
)

val mock1 = listOf(
    Line1(
        key = "action",
        values = mapOf(
            Language("ru") to Translation(
                value = "действие",
                language = Language("ru")
            ),
            Language("en") to Translation(
                value = "action",
                language = Language("en")
            )
        )
    ),
    Line1(
        key = "apply",
        values = mapOf(
            Language("ru") to Translation(
                value = "применить",
                language = Language("ru")
            ),
            Language("en") to Translation(
                value = "apply",
                language = Language("en")
            )
        )
    ),
    Line1(
        key = "table",
        values = mapOf(
            Language("ru") to Translation(
                value = "стол",
                language = Language("ru")
            )
        )
    ),
    Line1(
        key = "chair",
        values = mapOf(
            Language("en") to Translation(
                value = "chair",
                language = Language("en")
            )
        )
    ),
)

val languages = listOf(
    Language("ru"),
    Language("en"),
)