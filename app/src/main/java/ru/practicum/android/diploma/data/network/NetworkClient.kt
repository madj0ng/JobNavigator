package ru.practicum.android.diploma.data.network

import ru.practicum.android.diploma.data.dto.NetworkResponse

interface NetworkClient {

    suspend fun search(dto: Any): NetworkResponse

}
