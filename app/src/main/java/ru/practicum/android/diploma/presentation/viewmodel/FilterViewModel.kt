package ru.practicum.android.diploma.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.filters.FilterInteractor
import ru.practicum.android.diploma.domain.models.AreaFilterModel
import ru.practicum.android.diploma.domain.models.CityModel
import ru.practicum.android.diploma.domain.models.CountryModel
import ru.practicum.android.diploma.domain.models.FilterModel
import ru.practicum.android.diploma.domain.models.IndustriesFilterModel
import ru.practicum.android.diploma.domain.models.IndustryModel
import ru.practicum.android.diploma.domain.models.RegionModel
import ru.practicum.android.diploma.domain.models.Resource
import ru.practicum.android.diploma.presentation.mapper.MapperFilter
import ru.practicum.android.diploma.presentation.models.AreasScreenState
import ru.practicum.android.diploma.presentation.models.IndustryScreenState
import ru.practicum.android.diploma.presentation.models.RegionScreenState

class FilterViewModel(
    private val filterInteractor: FilterInteractor,
    private val mapperFilter: MapperFilter,
) : ViewModel() {
    private var _selectIndustryLiveData = MutableLiveData<IndustryModel?>()
    val selectIndustryLiveData: LiveData<IndustryModel?> get() = _selectIndustryLiveData
    private var _industryLiveData = MutableLiveData<IndustryScreenState>()
    val industryLiveData: LiveData<IndustryScreenState> get() = _industryLiveData
    private var _searchFilterLiveData = MutableLiveData<FilterModel?>()
    val searchFilterLiveData: LiveData<FilterModel?> get() = _searchFilterLiveData
    private var industryList = listOf<IndustryModel>()
    private var salaryBase: Int? = null
    private var doNotShowWithoutSalary: Boolean = false
    fun setDontShowWithoutSalary(show: Boolean) {
        doNotShowWithoutSalary = show
        saveCheckSalary(show)
    }
    fun setSalary(salary: String) {
        if (salary.isEmpty()) {
            salaryBase = null
        } else {
            salaryBase = salary.toInt()
            if (salaryBase!! == 0) salaryBase = null
        }
        saveSalary()
    }
    fun saveSalary() { viewModelScope.launch { filterInteractor.saveSalary(salaryBase) } }
    fun selectIndustry(industryModel: IndustryModel? = null) { _selectIndustryLiveData.postValue(industryModel) }
    fun searchIndustry(strIndustry: String) {
        if (strIndustry.isNotEmpty()) {
            val listRes = industryList.filter { industry ->
                industry.name.lowercase().contains(strIndustry.lowercase())
            }
            if (listRes.isEmpty()) {
                _industryLiveData.postValue(IndustryScreenState.ErrorContent)
            } else {
                _industryLiveData.postValue(IndustryScreenState.Content(listRes))
            }
        } else {
            _industryLiveData.postValue(IndustryScreenState.Content(industryList))
        }
    }
    fun saveIndustry() {
        if (_selectIndustryLiveData.value != null) {
            viewModelScope.launch {
                filterInteractor.saveIndustries(mapperFilter.map(_selectIndustryLiveData.value!!))
            }
        } else {
            deleteIndustries()
        }
    }
    fun saveSelectedFromFilter(filterModel: FilterModel?) {
        salaryBase = filterModel?.salary
        doNotShowWithoutSalary = filterModel?.onlyWithSalary ?: false
    }
    fun saveFilter(filterModel: FilterModel?) {
        viewModelScope.launch(Dispatchers.IO) { filterInteractor.saveFilter(filterModel) }
    }
    fun getFilter() {
        viewModelScope.launch {
            filterInteractor.getIndustrias().collect() { when (it) {
                is Resource.Error -> _industryLiveData.postValue(IndustryScreenState.ErrorInternet)
                is Resource.Success -> {
                    industryList = it.data
                    _industryLiveData.postValue(IndustryScreenState.Content(industryList))
                }
            }
            }
            filterInteractor.getFilter().collect { filter ->
                saveSelectedFromFilter(filter)
                _searchFilterLiveData.postValue(filter)
                selectIndustry(getIndustriesModelFromFilter(filter?.industries))
            }
        }
    }
    fun deletePlaceOfWork() { viewModelScope.launch { filterInteractor.deletePlaceOfWork() } }
    fun deleteIndustries() { viewModelScope.launch { filterInteractor.deleteIndustries() } }
    fun getIndustriesModelFromFilter(industry: IndustriesFilterModel?): IndustryModel? {
        return if (industry != null) mapperFilter.map(industry) else null
    }
    fun saveCheckSalary(onlyWithSalary: Boolean) {
        viewModelScope.launch { filterInteractor.saveCheckSalary(onlyWithSalary) }
    }
}
