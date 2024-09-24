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
import ru.practicum.android.diploma.domain.models.CountryFilterModel
import ru.practicum.android.diploma.domain.models.CountryModel
import ru.practicum.android.diploma.domain.models.FilterModel
import ru.practicum.android.diploma.domain.models.IndustriesFilterModel
import ru.practicum.android.diploma.domain.models.IndustryModel
import ru.practicum.android.diploma.domain.models.PlaceOfWorkModel
import ru.practicum.android.diploma.domain.models.RegionModel
import ru.practicum.android.diploma.domain.models.Resource
import ru.practicum.android.diploma.presentation.mapper.MapperIndystry
import ru.practicum.android.diploma.presentation.models.AreasScreenState
import ru.practicum.android.diploma.presentation.models.IndustryScreenState
import ru.practicum.android.diploma.presentation.models.RegionScreenState

class FilterViewModel(
    private val filterInteractor: FilterInteractor,
    private val mapperIndustry: MapperIndystry
) : ViewModel() {
    init {
        getAreas()
        getIndustries()
        getFilter()
    }

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
    private var _placeOfWorkLiveData = MutableLiveData<PlaceOfWorkModel>()
    val placeOfWorkLiveData: LiveData<PlaceOfWorkModel> get() = _placeOfWorkLiveData

    private var areaList = listOf<CountryModel>()
    private var regionsList = listOf<RegionModel>()
    private var cityList = listOf<CityModel>()
    private var industryList = listOf<IndustryModel>()
    var selectedCountry: CountryModel? = null

    var selectRegion: RegionModel? = null
    private var selectCity: CityModel? = null

    private var saveRegion: RegionModel? = null
    private var savedCity: CityModel? = null
    private var savedCountry: CountryModel? = null
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
            if (salaryBase!! == 0) {
                salaryBase = null
            }
        }
        saveSalary()
    }

    fun saveSalary() {
        viewModelScope.launch {
            filterInteractor.saveSalary(salaryBase)
        }
    }

    private fun getAreas() {
        viewModelScope.launch {
            filterInteractor.getAreas().collect { res ->
                if (res !is Resource.Error) {
                    areaList = (res as Resource.Success).data
                    _areaLiveData.postValue(AreasScreenState.Content(res.data))
                } else {
                    _areaLiveData.postValue(AreasScreenState.Error)
                }
            }
        }
    }

    fun saveArea() {
        savedCountry = selectedCountry
        saveRegion = selectRegion
        savedCity = selectCity
        viewModelScope.launch {
            if (savedCity != null) {
                filterInteractor.savePlaceOfWork(
                    CountryFilterModel(savedCountry!!.id, savedCountry!!.name),
                    AreaFilterModel(savedCity!!.id, savedCity!!.name)
                )
            } else {
                filterInteractor.savePlaceOfWork(
                    CountryFilterModel(savedCountry!!.id, savedCountry!!.name),
                    AreaFilterModel(saveRegion!!.id, saveRegion!!.name)
                )
            }
            filterInteractor.getFilter()
        }
    }

    fun selectCountry(country: CountryModel) {
        selectedCountry = country
        selectRegion = null
        selectCity = null
    }

    fun selectRegion(regionModel: RegionModel) {
        areaList.forEach {
            if (it.regions.contains(regionModel)) {
                selectedCountry = it
            }
        }
        selectRegion = regionModel
    }

    fun selectCity(cityModel: CityModel) {
        selectCity = cityModel
    }

    fun unSelectCountry() {
        selectedCountry = null
        savedCountry = null
        selectRegion = null
        saveRegion = null
        selectCity = null
        savedCity = null
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
        if (selectedCountry == null) {
            val list = mutableListOf<RegionModel>()
            areaList.forEach {
                list.addAll(it.regions)
            }
            regionsList = list
            if (regionsList.isEmpty()) {
                _regionsLiveData.postValue(RegionScreenState.ErrorNoList)
            } else {
                _regionsLiveData.postValue(RegionScreenState.Content(regionsList))
            }
        } else {
            if (selectedCountry!!.regions.isEmpty()) {
                _regionsLiveData.postValue(RegionScreenState.ErrorNoList)
            } else {
                regionsList = selectedCountry!!.regions
                _regionsLiveData.postValue(RegionScreenState.Content(regionsList))
            }
        }
    }

    fun getCity() {
        regionsList.forEach {
            if (it.name == selectRegion?.name) {
                cityList = it.city
                _cityLiveData.value = it.city
            }
        }
    }

    private fun getIndustries() {
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
                filterInteractor.saveIndustries(mapperIndustry.map(_selectIndustryLiveData.value!!))
            }
        } else {
            deleteIndustries()
        }
    }

    fun checkSelectedPlaceOfWork() {
        _placeOfWorkLiveData.postValue(PlaceOfWorkModel(selectedCountry, selectRegion, selectCity))
    }

    fun saveSelectedFromFilter(filterModel: FilterModel?) {
        selectedAreaFromFilter(filterModel)
        salaryBase = filterModel?.salary
        doNotShowWithoutSalary = filterModel?.onlyWithSalary ?: false
    }

    private fun selectedAreaFromFilter(filterModel: FilterModel?) {
        areaList.forEach {
            if (it.id == filterModel?.country?.id) {
                savedCountry = it
                selectedCountry = savedCountry
            }
        }
        selectedRegionFromFilter(filterModel)
    }

    private fun selectedRegionFromFilter(filterModel: FilterModel?) {
        selectedCountry?.regions?.forEach {
            selectedCityFromFilter(it.city, filterModel)
            if (it.id == filterModel?.area?.id) {
                saveRegion = it
                selectRegion = saveRegion
            }
        }
    }

    private fun selectedCityFromFilter(city: List<CityModel>, filterModel: FilterModel?) {
        if (city.isNotEmpty()) {
            city.forEach {
                if (it.id == filterModel?.area?.id) {
                    savedCity = it
                    selectCity = savedCity
                }
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
                if (res !is Resource.Error) {
                    areaList = (res as Resource.Success).data
                    _areaLiveData.postValue(AreasScreenState.Content(res.data))
                } else {
                    _areaLiveData.postValue(AreasScreenState.Error)
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
        return if (industry != null) mapperIndustry.map(industry) else null
    }

    fun saveCheckSalary(onlyWithSalary: Boolean) {
        viewModelScope.launch {
            filterInteractor.saveCheckSalary(onlyWithSalary)
        }
    }
}
