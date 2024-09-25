package ru.practicum.android.diploma.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.filters.FilterInteractor
import ru.practicum.android.diploma.domain.models.AreaFilterModel
import ru.practicum.android.diploma.domain.models.CityModel
import ru.practicum.android.diploma.domain.models.CountryModel
import ru.practicum.android.diploma.domain.models.FilterModel
import ru.practicum.android.diploma.domain.models.RegionModel
import ru.practicum.android.diploma.domain.models.Resource
import ru.practicum.android.diploma.presentation.mapper.MapperFilter
import ru.practicum.android.diploma.presentation.models.AreasScreenState
import ru.practicum.android.diploma.presentation.models.RegionScreenState

class PlaceOfWorkViewModel(
    private val filterInteractor: FilterInteractor,
    private val mapperFilter: MapperFilter
) : ViewModel() {
    private var _areaLiveData = MutableLiveData<AreasScreenState>()
    val areaLiveData: LiveData<AreasScreenState> get() = _areaLiveData
    private var _regionsLiveData = MutableLiveData<RegionScreenState>()
    val regionsLiveData: LiveData<RegionScreenState> get() = _regionsLiveData
    private var _cityLiveData = MutableLiveData<List<CityModel>>()
    val cityLiveData: LiveData<List<CityModel>> get() = _cityLiveData
    private var selectRegionLiveData = MutableLiveData<RegionModel?>()
    fun getSelectRegionLiveData(): LiveData<RegionModel?> = selectRegionLiveData
    private var selectCountryLiveData = MutableLiveData<CountryModel?>()
    fun getSelectCountryLiveData(): LiveData<CountryModel?> = selectCountryLiveData
    private var selectCityLiveData = MutableLiveData<CityModel?>()
    fun getSelectCityLiveData(): LiveData<CityModel?> = selectCityLiveData

    private var areaList = listOf<CountryModel>()
    private var regionsList = listOf<RegionModel>()
    private var cityList = listOf<CityModel>()

    fun getAreas() {
        viewModelScope.launch {
            filterInteractor.getAreas().collect { res ->
                when (res) {
                    is Resource.Error -> _areaLiveData.postValue(AreasScreenState.Error)
                    else -> {
                        areaList = (res as Resource.Success).data
                        _areaLiveData.postValue(AreasScreenState.Content(res.data))
                    }
                }
            }
        }
    }

    private fun setCountryModel(model: CountryModel?) { selectCountryLiveData.postValue(model) }
    fun setRegionModel(model: RegionModel?) { selectRegionLiveData.postValue(model) }
    private fun setCityModel(model: CityModel?) { selectCityLiveData.postValue(model) }
    fun saveArea() {
        var area: AreaFilterModel? = null
        val country = if (selectCountryLiveData.value != null) {
            mapperFilter.map(selectCountryLiveData.value!!)
        } else {
            null
        }
        if (selectRegionLiveData.value != null) area = mapperFilter.mapRegion(selectRegionLiveData.value!!)
        if (selectCityLiveData.value != null) area = mapperFilter.mapCity(selectCityLiveData.value!!)
        viewModelScope.launch { filterInteractor.savePlaceOfWork(country, area) }
    }
    fun selectPlaceOfWork(country: CountryModel? = null, region: RegionModel? = null, sity: CityModel? = null) {
        setCityModel(sity)
        setRegionModel(region)
        setCountryModel(country)
    }
    fun selectRegion(regionModel: RegionModel) {
        var country: CountryModel? = null
        areaList.forEach { if (it.regions.contains(regionModel)) country = it }
        setCountryModel(country)
        setRegionModel(regionModel)
    }
    fun selectCity(cityModel: CityModel) { setCityModel(cityModel) }
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
        _cityLiveData.value = cityList.filter { it.name.lowercase().contains(strCity.lowercase()) }
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

    fun saveSelectedFromFilter(filterModel: FilterModel?) {
        selectedAreaFromFilter(filterModel)
    }
    private fun selectedAreaFromFilter(filterModel: FilterModel?) {
        areaList.forEach { if (it.id == filterModel?.country?.id) setCountryModel(it) }
        selectedRegionFromFilter(filterModel)
    }
    private fun selectedRegionFromFilter(filterModel: FilterModel?) {
        selectCountryLiveData.value?.regions?.forEach {
            selectedCityFromFilter(it.city, filterModel)
            if (it.id == filterModel?.area?.id) setRegionModel(it)
        }
    }
    private fun selectedCityFromFilter(city: List<CityModel>, filterModel: FilterModel?) {
        if (city.isNotEmpty()) city.forEach { if (it.id == filterModel?.area?.id) setCityModel(it) }
    }

}
