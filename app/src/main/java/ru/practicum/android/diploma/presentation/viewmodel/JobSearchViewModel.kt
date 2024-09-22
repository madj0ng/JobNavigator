package ru.practicum.android.diploma.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.data.dto.model.AreasDto
import ru.practicum.android.diploma.data.dto.model.FilterDto
import ru.practicum.android.diploma.data.dto.model.IndustriesDto
import ru.practicum.android.diploma.domain.filters.FilterInteractor
import ru.practicum.android.diploma.domain.models.FilterModel
import ru.practicum.android.diploma.domain.models.Resource
import ru.practicum.android.diploma.domain.models.VacancyModel
import ru.practicum.android.diploma.domain.models.VacancySearchParams
import ru.practicum.android.diploma.domain.search.SearchVacancyInteractor
import ru.practicum.android.diploma.presentation.models.QueryUiState
import ru.practicum.android.diploma.presentation.models.SearchUiState
import ru.practicum.android.diploma.presentation.models.VacancyInfo
import ru.practicum.android.diploma.util.FormatConverter
import ru.practicum.android.diploma.util.SingleLiveEvent
import ru.practicum.android.diploma.util.debounce

class JobSearchViewModel(
    private val searchVacancyInteractor: SearchVacancyInteractor,
    private val formatConverter: FormatConverter,
    private val filterInteractor: FilterInteractor
) : ViewModel() {

    private val toastLiveData = SingleLiveEvent<String>()
    private val queryLiveData = MutableLiveData<QueryUiState>(QueryUiState.Search(query = ""))
    fun observeSearch(): LiveData<QueryUiState> = queryLiveData
    private val filterLiveData = MutableLiveData<FilterDto>(FilterDto())

    private val _screenLiveData = MutableLiveData<SearchUiState>(SearchUiState.Default())
    val screenLiveData: LiveData<SearchUiState> get() = _screenLiveData

    private val _searchFilterLiveData = MutableLiveData<FilterModel?>()
    val searchFilterLiveData: LiveData<FilterModel?> get() = _searchFilterLiveData

    private val debounceSearch = debounce<VacancySearchParams>(
        delayMillis = SEARCH_DEBOUNCE_DELAY,
        coroutineScope = viewModelScope,
        useLastParam = true
    ) { query ->
        searchRequest(query)
    }

    fun onSearchQueryChanged(query: VacancySearchParams) {
        queryLiveData.value = if (query.vacancyName.isEmpty()) {
            // пустой запрос для отмены предыдущей задачи
            debounceSearch(query)
            QueryUiState.Search()
        } else {
            val oldQuery = queryLiveData.value?.query ?: ""
            val oldFilter = filterLiveData.value as FilterDto
            val newFilter = FilterDto(
                area = AreasDto(query.area ?: ""),
                industries = IndustriesDto(query.professionalRole ?: ""),
                salary = query.salary,
                onlyWithSalary = query.onlyWithSalary
            )
            if (query.vacancyName != oldQuery || newFilter != oldFilter) {
                debounceSearch(query)
                filterLiveData.value = newFilter
            }
            QueryUiState.Clear(query = query.vacancyName)
        }
    }

    private val _currentPage = MutableLiveData<Int?>(null)
    val currentPage: LiveData<Int?> get() = _currentPage

    private val _maxPages = MutableLiveData<Int?>(null)
    val maxPages: LiveData<Int?> get() = _maxPages

    private val _vacanciesList = MutableLiveData<List<VacancyInfo>>(emptyList())
    val vacanciesLis: LiveData<List<VacancyInfo>> get() = _vacanciesList

    private val _isNextPageLoading = MutableLiveData<Boolean>(false)
    private val isNextPageLoading: LiveData<Boolean> get() = _isNextPageLoading

    private fun searchRequest(vacancySearchParams: VacancySearchParams) {
        if (vacancySearchParams.vacancyName.isEmpty()) {
            return
        }
        if (vacancySearchParams.page == 0) {
            _screenLiveData.value = SearchUiState.Loading()
        } else {
            _screenLiveData.value = SearchUiState.LoadingPagination()
        }
        viewModelScope.launch {
            searchVacancyInteractor.searchVacancy(vacancySearchParams).collect { result ->
                renderState(result)
            }
        }
    }

    private fun clearQuery(query: String) {
        _screenLiveData.postValue(SearchUiState.Default())
        queryLiveData.postValue(QueryUiState.Search())
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
                salary = formatConverter.toSalaryString(item.salary),
                logoUrl = item.employer?.logoUrls?.size90
            )
        }
    }

    fun onLastItemReached(vacancySearchParams: VacancySearchParams) {
        if (_isNextPageLoading.value == true || _currentPage.value ?: 0 >= _maxPages.value ?: 0) {
            return
        }
        var nextPage = _currentPage.value ?: 0
        nextPage += 1
        if (nextPage <= MAX_PAGE) {
            _currentPage.value = nextPage
            _isNextPageLoading.value = true
            _screenLiveData.value = SearchUiState.LoadingPagination()
            debounceSearch(vacancySearchParams.copy(page = nextPage))
        }
    }

    fun onClickSearchClear() {
        if (queryLiveData.value is QueryUiState.Clear) {
            clearQuery("")
        }
    }

    fun getFilter() {
        viewModelScope.launch {
            val filterModel = filterInteractor.getFilter()
            _searchFilterLiveData.value = filterModel
        }
    }

    companion object {
        const val SEARCH_DEBOUNCE_DELAY = 2000L
        const val ERROR_INTERNET = -1
        const val MAX_PAGE = 99
    }

}
