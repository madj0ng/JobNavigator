package ru.practicum.android.diploma.data.dto

data class Vacancy(
    val id: String,
    val address: Address,
    val name: String,
    val salary: Salary,
)

data class Address(
    val id: String,
    val city: String,
)

data class Salary(
    val id: String,
    val currency: String,
    val from: Int?,
    val to: Int?,
)
