package ru.practicum.android.diploma.data.network

import retrofit2.HttpException

class NetworkExeption(message: String, throwable: Throwable? = null) : Exception(message, throwable)
