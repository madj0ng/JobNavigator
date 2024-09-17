package ru.practicum.android.diploma.data.filter

interface FilterRepository {

    fun getCountries():List<AreaResponse>
    fun getIndustries():List<IndustryResponse>
}
