package ru.practicum.android.diploma.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.R
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
    private val queryLiveData = MutableLiveData<QueryUiState>(QueryUiState())
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

    private var _currentPage = 0
    private var _maxPages = 0
    private var _vacanciesList = emptyList<VacancyInfo>()
    private var _isNextPageLoading = false

    fun onSearchQueryChanged(query: VacancySearchParams) {
        setEmptyList()

        val newQuery = query.vacancyName
        val oldQuery = queryLiveData.value?.query ?: ""
        val isNewQuery = newQuery != oldQuery

        val oldFilter = filterLiveData.value as FilterDto
        val newFilter = getFilter(query)
        val isNewFilter = newFilter != oldFilter

        if (isNewQuery || isNewFilter) debounceSearch(query)
        if (isNewFilter) setFilterState(newFilter)
        if (isNewQuery) setQueryUiState(query.vacancyName)
    }

    fun searchRequest(vacancySearchParams: VacancySearchParams) {
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

    private fun clearQuery() {
        debounceSearch(VacancySearchParams())
        _screenLiveData.postValue(SearchUiState.Default())
        setQueryUiState("")
        setEmptyList()
    }

    private fun setEmptyList() {
        _currentPage = 0
        _maxPages = 0
        _vacanciesList = emptyList()
        _isNextPageLoading = false
    }

    private fun setFilterState(filter: FilterDto) {
        filterLiveData.postValue(filter)
    }

    private fun setQueryUiState(query: String) {
        queryLiveData.postValue(
            if (query.isEmpty()) {
                QueryUiState(R.drawable.ic_search_lens, query, false)
            } else {
                QueryUiState(R.drawable.ic_close_cross, query, true)
            }
        )
    }

    private fun getFilter(query: VacancySearchParams): FilterDto {
        return FilterDto(
            area = AreasDto(query.area ?: ""),
            industries = IndustriesDto(query.professionalRole ?: ""),
            salary = query.salary,
            onlyWithSalary = query.onlyWithSalary
        )
    }

    private fun renderState(result: Resource<List<VacancyModel>>) {
        when (result) {
            is Resource.Success -> {
                _isNextPageLoading = false
                if (result.data.isEmpty()) {
                    _screenLiveData.value = SearchUiState.ErrorData()
                } else {
                    val vacancyInfoList = mapListVacancyModelToListVacancyInfo(result.data)
                    _vacanciesList = _vacanciesList + vacancyInfoList
                    _currentPage = result.page ?: 0
                    _maxPages = result.pages ?: 0
                    _screenLiveData.value =
                        SearchUiState.Content(data = _vacanciesList, found = result.found ?: 0)
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
                logoUrl = item.employer?.logoUrls?.size90,
                city = item.region.name,
            )
        }
    }

    fun onLastItemReached(vacancySearchParams: VacancySearchParams) {
        if (_isNextPageLoading || _currentPage >= _maxPages - 1) {
            return
        }
        var nextPage = _currentPage
        nextPage += 1
        if (nextPage <= MAX_PAGE) {
            _currentPage = nextPage
            _isNextPageLoading = true
            _screenLiveData.value = SearchUiState.LoadingPagination()
            searchRequest(vacancySearchParams.copy(page = nextPage))
        }
    }

    fun onClickSearchClear() {
        if (true == queryLiveData.value?.isClose) {
            clearQuery()
        }
    }

    fun getFilter() {
        viewModelScope.launch(Dispatchers.IO) {
            filterInteractor.getFilter().collect { filter ->
                _searchFilterLiveData.postValue(filter)
            }
        }
    }

    companion object {
        const val SEARCH_DEBOUNCE_DELAY = 2000L
        const val ERROR_INTERNET = -1
        const val MAX_PAGE = 99
    }

}
