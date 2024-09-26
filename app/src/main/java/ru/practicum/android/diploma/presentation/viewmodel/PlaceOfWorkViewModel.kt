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
import ru.practicum.android.diploma.domain.models.RegionModel
import ru.practicum.android.diploma.domain.models.Resource
import ru.practicum.android.diploma.presentation.mapper.MapperFilter
import ru.practicum.android.diploma.presentation.models.AreasScreenState
import ru.practicum.android.diploma.presentation.models.PlaceOfWorkModel
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
    private var _placeOfWorkLiveData = MutableLiveData<PlaceOfWorkModel>()
    val placeOfWorkLiveData: LiveData<PlaceOfWorkModel> get() = _placeOfWorkLiveData

    private var selectCountry: CountryModel? = null
    private var selectRegion: RegionModel? = null
    private var selectCity: CityModel? = null
    private var areaList = listOf<CountryModel>()
    private var regionsList = listOf<RegionModel>()
    private var cityList = listOf<CityModel>()

    fun getAreas() {
        viewModelScope.launch(Dispatchers.IO) {
            filterInteractor.getAreas().collect { res ->
                when (res) {
                    is Resource.Error -> {
                        if (res.resultCode == ERROR_INTERNET) {
                            _areaLiveData.postValue(AreasScreenState.ErrorInternet)
                        } else {
                            _areaLiveData.postValue(AreasScreenState.Error)
                        }
                    }

                    else -> {
                        areaList = (res as Resource.Success).data
                        _areaLiveData.postValue(AreasScreenState.Content((res as Resource.Success).data))
                    }
                }
            }
        }
    }

    fun saveArea() {
        var area: AreaFilterModel? = null
        val country = if (selectCountry != null) {
            mapperFilter.map(selectCountry!!)
        } else {
            null
        }
        if (selectRegion != null) area = mapperFilter.mapRegion(selectRegion!!)
        if (selectCity != null) area = mapperFilter.mapCity(selectCity!!)
        viewModelScope.launch(Dispatchers.IO) {
            filterInteractor.savePlaceOfWork(country, area)
        }
        selectCountry = null
        selectRegion = null
        selectCity = null
    }

    fun selectCountry(country: CountryModel?) {
        selectCountry = country
        selectRegion = null
        selectCity = null
        _placeOfWorkLiveData.postValue(PlaceOfWorkModel(selectCountry, null, null))
    }

    fun selectRegion(regionModel: RegionModel) {
        var country: CountryModel? = null
        areaList.forEach { if (it.regions.contains(regionModel)) country = it }
        selectCountry = country
        selectRegion = regionModel
        _placeOfWorkLiveData.postValue(PlaceOfWorkModel(selectCountry, selectRegion, null))
    }

    fun selectCity(cityModel: CityModel) {
        selectCity = cityModel
        _placeOfWorkLiveData.postValue(PlaceOfWorkModel(selectCountry, selectRegion, selectCity))
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
        _cityLiveData.value = cityList.filter { it.name.lowercase().contains(strCity.lowercase()) }
    }

    fun getRegions() {
        val country = selectCountry
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
            if (it.name == _placeOfWorkLiveData.value?.regionModel?.name) {
                cityList = it.city
                _cityLiveData.value = it.city
            }
        }
    }

    fun saveContryFromFilter(filter: FilterModel) {
        if (selectCountry == null) {
            areaList.forEach {
                if (it.id == filter.country?.id) {
                    selectCountry = it
                }
            }
        }
    }

    fun isCountrySelect(): Boolean {
        return selectCountry == null
    }

    companion object {
        private const val ERROR_INTERNET = -1
    }
}
