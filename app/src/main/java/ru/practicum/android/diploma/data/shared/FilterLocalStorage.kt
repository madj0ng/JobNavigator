package ru.practicum.android.diploma.data.shared

import android.content.SharedPreferences
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
            val filterString = gson.toJson(filter)
            pref.edit().putString(FILTER_KEY, filterString).apply()
        }
    }
}
