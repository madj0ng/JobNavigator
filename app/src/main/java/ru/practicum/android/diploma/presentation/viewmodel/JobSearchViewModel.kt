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
import ru.practicum.android.diploma.presentation.models.JobSearchScreenState
import ru.practicum.android.diploma.presentation.models.VacancyInfo
import ru.practicum.android.diploma.util.SingleLiveEvent

class JobSearchViewModel(
    val searchVacancyInteractor: SearchVacancyInteractor,
    val context: Context
) : ViewModel() {

    private val toastLiveData = SingleLiveEvent<String>()

    private val _screenLiveData = MutableLiveData<JobSearchScreenState>(JobSearchScreenState.Default)
    val screenLiveData: LiveData<JobSearchScreenState> get() = _screenLiveData

    private val _currentPage = MutableLiveData<Int?>(null)
    val currentPage: LiveData<Int?> get() = _currentPage

    private val _maxPages = MutableLiveData<Int?>(null)
    val maxPages: LiveData<Int?> get() = _maxPages

    private val _vacanciesList = MutableLiveData<List<VacancyInfo>>(emptyList())
    val vacanciesLis: LiveData<List<VacancyInfo>> get() = _vacanciesList

    private val _isNextPageLoading = MutableLiveData<Boolean>(false)
    private val isNextPageLoading: LiveData<Boolean> get() = _isNextPageLoading

    private var currentSearchQuery: String = ""

    fun onSearchQueryChanged(query: String) {
        currentSearchQuery = query
        _currentPage.value = 0
        searchRequest(query, 0)
    }

    private fun searchRequest(newSearchQuery: String, page: Int = 0) {
        if (newSearchQuery.isEmpty()) {
            return
        }

        val searchParams = VacancySearchParams(
            vacancyName = newSearchQuery,
            page = page
        )

        if (page == 0) {
            _screenLiveData.value = SearchUiState.Loading()
        } else {
            _screenLiveData.value = SearchUiState.ShowPaginationLoading
        }

        viewModelScope.launch {
            searchVacancyInteractor.searchVacancy(searchParams).collect { result ->
                renderState(result)
            }
        }
    }

    private fun renderState(result: Resource<List<VacancyModel>>) {
        when (result) {
            is Resource.Success -> {
                _isNextPageLoading.value = false
                if (result.data.isEmpty()) {
                    _screenLiveData.value = SearchUiState.ErrorData()
                } else {
                    val vacancyInfoList = mapListVacancyModelToListVacancyInfo(result.data)
                    _vacanciesList.value = _vacanciesList.value.orEmpty() + vacancyInfoList
                    _currentPage.value = result.page
                    _maxPages.value = result.pages
                    _screenLiveData.value = SearchUiState.Content(data = vacancyInfoList, found = result.found ?: 0)
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

    fun onLastItemReached() {
        if (_isNextPageLoading.value == true || _currentPage.value ?: 0 >= _maxPages.value ?: 0) {
            return
        }
        val nextPage = _currentPage.value ?: 0 + 1
        _currentPage.value = nextPage

        _isNextPageLoading.value = true
        searchRequest(currentSearchQuery, nextPage)
    }

    companion object {
        const val ERROR_INTERNET = -1
    }

}
