package ru.practicum.android.diploma.domain.models

data class EmployerModel(
    val id: String?,
    val name: String?,
    val logoUrls: LogoModel? = null, // Ссылка на логотип
)
