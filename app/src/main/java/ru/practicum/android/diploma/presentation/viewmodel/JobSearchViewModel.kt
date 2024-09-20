package ru.practicum.android.diploma.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.domain.models.Resource
import ru.practicum.android.diploma.domain.models.SalaryModel
import ru.practicum.android.diploma.domain.models.VacancyModel
import ru.practicum.android.diploma.domain.models.VacancySearchParams
import ru.practicum.android.diploma.domain.search.SearchVacancyInteractor
import ru.practicum.android.diploma.presentation.models.SearchUiState
import ru.practicum.android.diploma.presentation.models.VacancyInfo
import ru.practicum.android.diploma.util.SingleLiveEvent

class JobSearchViewModel(
    val searchVacancyInteractor: SearchVacancyInteractor,
    val context: Context
) : ViewModel() {

    private val toastLiveData = SingleLiveEvent<String>()

    private val _screenLiveData = MutableLiveData<SearchUiState>(SearchUiState.Default())
    val screenLiveData: LiveData<SearchUiState> get() = _screenLiveData

    fun onSearchQueryChanged(query: String) {
        searchRequest(query)
    }

    private fun searchRequest(newSearchQuery: String) {
        if (newSearchQuery.isEmpty()) {
            return
        }

        val searchParams = VacancySearchParams(
            vacancyName = newSearchQuery,
        )

        _screenLiveData.value = SearchUiState.Loading()

        viewModelScope.launch {
            searchVacancyInteractor.searchVacancy(searchParams).collect { result ->
                renderState(result)
            }
        }
    }

    private fun renderState(result: Resource<List<VacancyModel>>) {
        when (result) {
            is Resource.Success -> {
                if (result.data.isEmpty()) {
                    _screenLiveData.value = SearchUiState.ErrorData()
                } else {
                    val vacancyInfoList = mapListVacancyModelToListVacancyInfo(result.data)
                    val found = result.found ?: 0
                    _screenLiveData.value = SearchUiState.Content(data = vacancyInfoList, found = found)
                }
            }

            is Resource.Error -> {
                if (result.resultCode == ERROR_INTERNET) {
                    _screenLiveData.value = SearchUiState.ErrorConnect()
                } else {
                    _screenLiveData.value = SearchUiState.ErrorServer()
                }
                toastLiveData.value = result.message
            }
        }
    }

    private fun mapListVacancyModelToListVacancyInfo(vacancies: List<VacancyModel>): List<VacancyInfo> {
        return vacancies.map { item ->
            VacancyInfo(
                id = item.id,
                vacancyName = item.name,
                departamentName = item.employer?.name ?: "",
                salary = getSalary(item.salary),
                logoUrl = item.employer?.logoUrls?.size90
            )
        }
    }

    private fun getSalary(salary: SalaryModel?): String {
        if (salary == null) {
            return context.getString(R.string.salary_not_specified)
        }

        val from = getSalaryFrom(salary.from)
        val to = getSalaryTo(salary.to)
        val currency = salary.currency?.let { " $it" } ?: ""

        return if (from.isEmpty() && to.isEmpty()) {
            context.getString(R.string.salary_not_specified)
        } else {
            "$from$to$currency"
        }
    }

    private fun getSalaryFrom(from: Int?): String {
        return from?.let { "от $it" } ?: ""
    }

    private fun getSalaryTo(to: Int?): String {
        return to?.let { " до $it" } ?: ""
    }

    companion object {
        const val ERROR_INTERNET = -1
    }

}
