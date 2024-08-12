package com.nikulin.lines.domain.repositories

import com.nikulin.lines.domain.models.Entry

interface FileStructureRepository {
    suspend fun getStructure(): Result<Map<Int, Entry>>
    suspend fun saveStructure(structure: Map<Int, Entry>): Result<Unit>
}

class FileStructureRepositoryImpl : FileStructureRepository {

    private var structure: Map<Int, Entry>? = null

    override suspend fun getStructure(): Result<Map<Int, Entry>> {
        return runCatching {
            val currentStructure = structure

            return if (currentStructure != null) {
                Result.success(currentStructure)
            } else {
                throw Exception("structure is null")
            }
        }

    }

    override suspend fun saveStructure(structure: Map<Int, Entry>): Result<Unit> {
        return runCatching {
            this.structure = structure
        }
    }

}