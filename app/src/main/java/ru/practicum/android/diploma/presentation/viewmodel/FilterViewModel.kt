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

class FilterViewModel(private val filterInteractor: FilterInteractor, private val mapperFilter: MapperFilter,
) : ViewModel() {
    private var _areaLiveData = MutableLiveData<AreasScreenState>()
    val areaLiveData: LiveData<AreasScreenState> get() = _areaLiveData
    private var _regionsLiveData = MutableLiveData<RegionScreenState>()
    val regionsLiveData: LiveData<RegionScreenState> get() = _regionsLiveData
    private var _cityLiveData = MutableLiveData<List<CityModel>>()
    val cityLiveData: LiveData<List<CityModel>> get() = _cityLiveData
    private var _selectIndustryLiveData = MutableLiveData<IndustryModel?>()
    val selectIndustryLiveData: LiveData<IndustryModel?> get() = _selectIndustryLiveData
    private var _industryLiveData = MutableLiveData<IndustryScreenState>()
    val industryLiveData: LiveData<IndustryScreenState> get() = _industryLiveData
    private var _searchFilterLiveData = MutableLiveData<FilterModel?>()
    val searchFilterLiveData: LiveData<FilterModel?> get() = _searchFilterLiveData
    private var selectRegionLiveData = MutableLiveData<RegionModel?>()
    fun getSelectRegionLiveData(): LiveData<RegionModel?> = selectRegionLiveData
    private var selectCountryLiveData = MutableLiveData<CountryModel?>()
    fun getSelectCountryLiveData(): LiveData<CountryModel?> = selectCountryLiveData
    private var selectCityLiveData = MutableLiveData<CityModel?>()
    fun getSelectCityLiveData(): LiveData<CityModel?> = selectCityLiveData
    private var areaList = listOf<CountryModel>()
    private var regionsList = listOf<RegionModel>()
    private var cityList = listOf<CityModel>()
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

    fun saveSalary() {
        viewModelScope.launch {
            filterInteractor.saveSalary(salaryBase)
        }
    }

    private fun setCountryModel(model: CountryModel?) {
        selectCountryLiveData.postValue(model)
    }

    fun setRegionModel(model: RegionModel?) {
        selectRegionLiveData.postValue(model)
    }

    private fun setCityModel(model: CityModel?) {
        selectCityLiveData.postValue(model)
    }

    fun saveArea() {
        var area: AreaFilterModel? = null
        val country = if (selectCountryLiveData.value != null) {
            mapperFilter.map(selectCountryLiveData.value!!)
        } else {
            null
        }
        if (selectRegionLiveData.value != null) {
            area = mapperFilter.mapRegion(selectRegionLiveData.value!!)
        }
        if (selectCityLiveData.value != null) {
            area = mapperFilter.mapCity(selectCityLiveData.value!!)
        }
        viewModelScope.launch {
            filterInteractor.savePlaceOfWork(country, area)
        }
    }

    fun selectCountry(country: CountryModel) {
        setCityModel(null)
        setRegionModel(null)
        setCountryModel(country)
    }

    fun selectRegion(regionModel: RegionModel) {
        var country: CountryModel? = null
        areaList.forEach {
            if (it.regions.contains(regionModel)) country = it
        }
        setCountryModel(country)
        setRegionModel(regionModel)
    }

    fun selectCity(cityModel: CityModel) {
        setCityModel(cityModel)
    }

    fun unSelectCountry() {
        setCityModel(null)
        setCountryModel(null)
        setRegionModel(null)
    }

    fun searchRegion(strRegion: String) {
        if (strRegion.isNotEmpty()) {
            val listRes = regionsList.filter { area ->
                area.name.lowercase().contains(strRegion.lowercase())
            }
            if (listRes.isEmpty()) {
                _regionsLiveData.postValue(RegionScreenState.ErrorNoRegion)
            } else {
                _regionsLiveData.postValue(RegionScreenState.Content(listRes))
            }
        } else {
            _regionsLiveData.postValue(RegionScreenState.Content(regionsList))
        }
    }

    fun searchCity(strCity: String) {
        val listRes = cityList.filter { city ->
            city.name.lowercase().contains(strCity.lowercase())
        }
        _cityLiveData.value = listRes
    }

    fun getRegions() {
        val country = selectCountryLiveData.value
        if (country == null) {
            val list = mutableListOf<RegionModel>()
            areaList.forEach { list.addAll(it.regions) }
            regionsList = list
            if (regionsList.isEmpty()) {
                _regionsLiveData.postValue(RegionScreenState.ErrorNoList)
            } else {
                _regionsLiveData.postValue(RegionScreenState.Content(regionsList))
            }
        } else {
            if (country.regions.isEmpty()) {
                _regionsLiveData.postValue(RegionScreenState.ErrorNoList)
            } else {
                regionsList = country.regions
                _regionsLiveData.postValue(RegionScreenState.Content(regionsList))
            }
        }
    }

    fun getCity() {
        regionsList.forEach {
            if (it.name == selectRegionLiveData.value?.name) {
                cityList = it.city
                _cityLiveData.value = it.city
            }
        }
    }

    fun selectIndustry(industryModel: IndustryModel?) {
        _selectIndustryLiveData.postValue(industryModel)
    }

    fun unSelectIndustry() {
        _selectIndustryLiveData.postValue(null)
    }

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
        selectedAreaFromFilter(filterModel)
        salaryBase = filterModel?.salary
        doNotShowWithoutSalary = filterModel?.onlyWithSalary ?: false
    }

    private fun selectedAreaFromFilter(filterModel: FilterModel?) {
        if (selectCountryLiveData.value != null) {
            areaList.forEach {
                if (it.id == filterModel?.country?.id) setCountryModel(it)
            }
        }
        selectedRegionFromFilter(filterModel)
    }

    private fun selectedRegionFromFilter(filterModel: FilterModel?) {
        if (selectRegionLiveData.value != null) {
            selectCountryLiveData.value?.regions?.forEach {
                selectedCityFromFilter(it.city, filterModel)
                if (it.id == filterModel?.area?.id) setRegionModel(it)
            }
        }
    }

    private fun selectedCityFromFilter(city: List<CityModel>, filterModel: FilterModel?) {
        if (city.isNotEmpty()) {
            city.forEach {
                if (it.id == filterModel?.area?.id) setCityModel(it)
            }
        }
    }

    fun saveFilter(filterModel: FilterModel?) {
        viewModelScope.launch(Dispatchers.IO) {
            filterInteractor.saveFilter(filterModel)
        }
    }

    fun getFilter() {
        viewModelScope.launch {
            filterInteractor.getIndustrias().collect() { res ->
                when (res) {
                    is Resource.Error -> _industryLiveData.postValue(IndustryScreenState.ErrorInternet)
                    is Resource.Success -> {
                        industryList = res.data
                        _industryLiveData.postValue(IndustryScreenState.Content(industryList))
                    }
                }
            }
            filterInteractor.getAreas().collect { res ->
                when (res) {
                    is Resource.Error -> _areaLiveData.postValue(AreasScreenState.Error)
                    else -> {
                        areaList = (res as Resource.Success).data
                        _areaLiveData.postValue(AreasScreenState.Content(res.data))
                    }
                }
            }
            filterInteractor.getFilter().collect { filter ->
                _searchFilterLiveData.postValue(filter)
                selectIndustry(getIndustriesModelFromFilter(filter?.industries))
            }
        }
    }

    fun deletePlaceOfWork() {
        viewModelScope.launch {
            filterInteractor.deletePlaceOfWork()
        }
    }

    fun deleteIndustries() {
        viewModelScope.launch {
            filterInteractor.deleteIndustries()
        }
    }

    fun getIndustriesModelFromFilter(industry: IndustriesFilterModel?): IndustryModel? {
        return if (industry != null) mapperFilter.map(industry) else null
    }

    fun saveCheckSalary(onlyWithSalary: Boolean) {
        viewModelScope.launch {
            filterInteractor.saveCheckSalary(onlyWithSalary)
        }
    }
}
