package ru.practicum.android.diploma.data.dto

data class Vacancy(
    val address: Address,
    val name: String,
    val salary: Salary,
)

data class Address(
    val city: String,
)

data class Salary(
    val currency: String,
    val from: Int?,
    val to: Int?,
)
