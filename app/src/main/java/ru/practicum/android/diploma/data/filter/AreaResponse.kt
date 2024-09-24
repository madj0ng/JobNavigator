package ru.practicum.android.diploma.data.filter

import com.google.gson.annotations.SerializedName
import ru.practicum.android.diploma.data.dto.NetworkResponse

data class AreaResponse(
    val name: String,
    val id: String,
    @SerializedName("areas")
    val regions: List<Region>,
) : NetworkResponse()

data class Region(
    val id: String,
    val name: String,
    @SerializedName("areas")
    val city: List<City>
)

data class City(
    val id: String,
    val name: String
)
