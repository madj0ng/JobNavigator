package ru.practicum.android.diploma.data.network

import ru.practicum.android.diploma.data.dto.NetworkResponse

data class ListResponseWrapper<T>(var list: List<T> = emptyList()) : NetworkResponse()
