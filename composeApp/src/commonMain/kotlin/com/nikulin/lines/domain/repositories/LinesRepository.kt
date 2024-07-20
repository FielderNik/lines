package com.nikulin.lines.domain.repositories

import com.nikulin.lines.domain.models.Language
import com.nikulin.lines.domain.models.Line

interface LinesRepository {
    suspend fun getLines(): Result<List<Line>>
    suspend fun hasLines(): Result<Boolean>
    suspend fun saveLines(lines: List<Line>): Result<Unit>
    suspend fun getLanguages(): Result<List<Language>>
}

class LinesRepositoryImpl : LinesRepository {

    private var lines: List<Line> = mutableListOf()

    override suspend fun getLines(): Result<List<Line>> {
        return Result.success(lines)
    }

    override suspend fun hasLines(): Result<Boolean> {
        return Result.success(lines.isNotEmpty())
    }

    override suspend fun saveLines(lines: List<Line>): Result<Unit> {
        this.lines = lines
        return Result.success(Unit)
    }

    override suspend fun getLanguages(): Result<List<Language>> {
        return if (lines.isEmpty()) {
            Result.failure(RuntimeException("lines empty"))
        } else {
            val languages = lines.map { it.values }.map { it.keys }.flatten().distinct()
            Result.success(languages)
        }
    }

}