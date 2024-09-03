package ru.practicum.android.diploma.data.db

import ru.practicum.android.diploma.data.dto.DatabaseResponse

interface DatabaseClient {
    suspend fun doRequest(dto: Any): DatabaseResponse
}
