package ru.practicum.android.diploma.data.shared

import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.practicum.android.diploma.data.dto.model.AreasDto
import ru.practicum.android.diploma.data.dto.model.CountriesDto
import ru.practicum.android.diploma.data.dto.model.FilterDto
import ru.practicum.android.diploma.data.dto.model.IndustriesDto

class FilterLocalStorage(
    private val pref: SharedPreferences,
    private val gson: Gson,
) {
    companion object {
        const val FILTER_KEY = "filter_key"
    }

    suspend fun getFromStorage(): FilterDto? {
        return withContext(Dispatchers.IO) {
            val filterString = pref.getString(FILTER_KEY, null)
            if (filterString != null) gson.fromJson(filterString, FilterDto::class.java) else null

        }
    }

    suspend fun saveStorage(filter: FilterDto?) {
        withContext(Dispatchers.IO) {
            if (filter != null) {
                val filterString = gson.toJson(filter)
                pref.edit().putString(FILTER_KEY, filterString).apply()
            } else {
                pref.edit().remove(FILTER_KEY).apply()
            }
        }
    }

    suspend fun savePlaceOfWork(countriesDto: CountriesDto, areasDto: AreasDto) {
        var filter = getFromStorage()
        if (filter != null) {
            filter.country = countriesDto
            filter.area = areasDto
        } else {
            filter = FilterDto(country = countriesDto, area = areasDto)
        }
        saveStorage(filter)
    }

    suspend fun saveIndustries(industriesDto: IndustriesDto) {
        var filter = getFromStorage()
        if (filter != null) {
            filter.industries = industriesDto
        } else {
            filter = FilterDto(industries = industriesDto)
        }
        saveStorage(filter)
    }

    suspend fun saveSalary(salaryDto: Int?) {
        var filter = getFromStorage()
        if (filter != null) {
            if (getConditionToSalary(filter) && salaryDto == null) {
                saveStorage(null)
            } else {
                filter.salary = salaryDto
                Log.d("AAAAAAA", "${filter.salary}")
                saveStorage(filter)
            }
        } else {
            if (salaryDto != null) {
                filter = FilterDto(salary = salaryDto)
                saveStorage(filter)
            }
        }
    }

    suspend fun deletePlaceOfWork() {
        val filter = getFromStorage()
        if (getConditionToPow(filter)) {
            saveStorage(null)
        } else {
            filter?.area = null
            filter?.country = null
            saveStorage(filter)
        }
    }

    suspend fun deleteIndustries() {
        val filter = getFromStorage()
        if (getConditionToInd(filter)) {
            saveStorage(null)
        } else {
            filter?.industries = null
            saveStorage(filter)
        }
    }

    suspend fun saveCheckSalary(onlyWithSalary: Boolean) {
        var filter = getFromStorage()
        if (filter == null) {
            if (onlyWithSalary) {
                filter = FilterDto(onlyWithSalary = onlyWithSalary)
                saveStorage(filter)
            }
        } else {
            if (getConditionToCheckSalary(filter) && !onlyWithSalary) {
                saveStorage(null)
            } else {
                compareCheckAndSaveCheck(onlyWithSalary, filter)
            }
        }
    }

    private suspend fun compareCheckAndSaveCheck(onlyWithSalary: Boolean, filter: FilterDto?) {
        if (filter?.onlyWithSalary != onlyWithSalary) {
            if (onlyWithSalary) {
                filter?.onlyWithSalary = true
                saveStorage(filter)
            } else {
                if (getConditionToCheckSalary(filter)) {
                    saveStorage(null)
                } else {
                    filter?.onlyWithSalary = onlyWithSalary
                    saveStorage(filter)
                }
            }
        }
    }

    private fun getConditionToPow(filter: FilterDto?): Boolean {
        return filter?.onlyWithSalary == null &&
            filter?.industries == null &&
            filter?.salary == null
    }

    private fun getConditionToInd(filter: FilterDto?): Boolean {
        return filter?.onlyWithSalary == null &&
            filter?.country == null &&
            filter?.area == null &&
            filter?.salary == null
    }

    private fun getConditionToCheckSalary(filter: FilterDto?): Boolean {
        return filter?.salary == null &&
            filter?.country == null &&
            filter?.area == null &&
            filter?.industries == null
    }

    private fun getConditionToSalary(filter: FilterDto?): Boolean {
        return filter?.onlyWithSalary == null &&
            filter?.country == null &&
            filter?.area == null &&
            filter?.industries == null
    }
}
