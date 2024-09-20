package ru.practicum.android.diploma.data.network

import ru.practicum.android.diploma.data.dto.NetworkResponse

data class ListResponseWrapper<T>(val list: List<T>) : NetworkResponse()
