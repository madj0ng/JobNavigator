package ru.practicum.android.diploma.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.filters.FilterInteractor
import ru.practicum.android.diploma.domain.models.CityModel
import ru.practicum.android.diploma.domain.models.CountryModel
import ru.practicum.android.diploma.domain.models.IndustryModel
import ru.practicum.android.diploma.domain.models.RegionModel
import ru.practicum.android.diploma.domain.models.Resource
import ru.practicum.android.diploma.presentation.models.AreasScreenState
import ru.practicum.android.diploma.presentation.models.IndustryScreenState
import ru.practicum.android.diploma.presentation.models.RegionScreenState

class FilterViewModel(private val filterInteractor: FilterInteractor) : ViewModel() {
    private val areaLiveData = MutableLiveData<AreasScreenState>()
    private var areaList = listOf<CountryModel>()
    private val regionsLiveData = MutableLiveData<RegionScreenState>()
    private var regionsList = listOf<RegionModel>()
    private val cityLiveData = MutableLiveData<List<CityModel>>()
    private var cityList = listOf<CityModel>()
    private var selectedCountry: CountryModel? = null
    private var selectIndustry: IndustryModel? = null
    private var selectRegion: RegionModel? = null
    private var selectCity: CityModel? = null
    private var selectIndustryLiveData = MutableLiveData<IndustryModel?>()
    private val industryLiveData = MutableLiveData<IndustryScreenState>()
    private var industryList = listOf<IndustryModel>()
    private var savedIndustry: IndustryModel? = null
    private var savedCity: CityModel? = null
    private var savedCountry: CountryModel? = null
    private var salarybase: Int? = null
    private var dontShowWithoutSalary: Boolean = false

    fun setDontShowWithoutSalary(show: Boolean) {
        dontShowWithoutSalary = show
    }

    fun getDontShowWithoutSalary(): Boolean {
        return dontShowWithoutSalary
    }

    fun setSalary(salary: String) {
        if (salary.isEmpty()) {
            salarybase = null
        } else {
            salarybase = salary.toInt()
        }
    }

    fun getSalary(): Int? {
        return salarybase
    }

    fun getAreas() {
        viewModelScope.launch {
            filterInteractor.getAreas().collect { res ->
                if (res !is Resource.Error) {
                    areaList = (res as Resource.Success).data
                    areaLiveData.value = AreasScreenState.Content((res as Resource.Success).data)
                } else {
                    areaLiveData.value = AreasScreenState.Error
                }
            }
        }
    }

    fun saveArea() {
        savedCountry = selectedCountry
        savedCity = selectCity
    }

    fun getCitySaved(): CityModel? {
        return savedCity
    }

    fun getCountrySaved(): CountryModel? {
        return savedCountry
    }

    fun getRegionLiveData(): LiveData<RegionScreenState> {
        return regionsLiveData
    }

    fun getAreaLiveData(): LiveData<AreasScreenState> {
        return areaLiveData
    }

    fun selectCountry(country: CountryModel) {
        selectedCountry = country
        selectRegion = null
    }

    fun getSelectedCountry(): CountryModel? {
        return selectedCountry
    }

    fun getSelectedCity(): CityModel? {
        return selectCity
    }

    fun selectRegion(regionModel: RegionModel) {
        selectRegion = regionModel
    }

    fun selectCity(cityModel: CityModel) {
        selectCity = cityModel
    }

    fun unSelectCountry() {
        selectedCountry = null
        savedCountry = null
        selectCity = null
        savedCity = null
    }

    fun searchRegion(strRegion: String) {
        if (strRegion.isNotEmpty()) {
            val listRes = regionsList.filter { area ->
                area.name.lowercase().contains(strRegion.lowercase())
            }
            if (listRes.isEmpty()) {
                regionsLiveData.value = RegionScreenState.ErrorNoRegion
            } else {
                regionsLiveData.value = RegionScreenState.Content(listRes)
            }
        } else {
            regionsLiveData.value = RegionScreenState.Content(regionsList)
        }
    }


    fun searchCity(strCity: String) {
        val listRes = cityList.filter { city ->
            city.name.lowercase().contains(strCity.lowercase())
        }
        cityLiveData.value = listRes
    }

    fun getRegions() {
        if (selectedCountry == null) {
            regionsLiveData.value = RegionScreenState.ErrorNoList
        } else {
            regionsList = selectedCountry!!.regions
            regionsLiveData.value = RegionScreenState.Content(regionsList)
        }
    }

    fun getCity() {
        regionsList.forEach {
            if (it.name == selectRegion?.name) {
                cityList = it.city
                cityLiveData.value = it.city
            }
        }
    }

    fun getCityLiveData(): LiveData<List<CityModel>> {
        return cityLiveData
    }

    fun getIndustries() {
        viewModelScope.launch {
            filterInteractor.getIndustrias().collect() { res ->
                when (res) {
                    is Resource.Error -> industryLiveData.value = IndustryScreenState.ErrorInternet
                    is Resource.Success -> {
                        industryList = res.data
                        industryLiveData.value = IndustryScreenState.Content(industryList)
                    }
                }
            }
        }
    }

    fun selectIndustry(industryModel: IndustryModel) {
        selectIndustry = industryModel
        selectIndustryLiveData.value = industryModel
    }

    fun unSelectIndustry() {
        selectIndustry = null
        savedIndustry = null
        selectIndustryLiveData.value = null
    }

    fun searchIndustry(strIndustry: String) {
        if (strIndustry.isNotEmpty()) {
            val listRes = industryList.filter { industry ->

                industry.name.lowercase().contains(strIndustry.lowercase())
            }

            if (listRes.isEmpty()) {
                industryLiveData.value = IndustryScreenState.ErrorContent
            } else {
                industryLiveData.value = IndustryScreenState.Content(listRes)
            }
        } else {
            industryLiveData.value = IndustryScreenState.Content(industryList)
        }
    }

    fun saveIndustry() {
        savedIndustry = selectIndustry
    }

    fun getSavedIndustry(): IndustryModel? {
        return savedIndustry
    }

    fun getSelectedIndustryLiveData(): LiveData<IndustryModel?> {
        return selectIndustryLiveData
    }

    fun getIndustryLiveData(): LiveData<IndustryScreenState> {
        return industryLiveData
    }

    companion object {
        const val ERROR_EMPTY = -1
        const val MESSAGE_EMPTY = ""
    }
}
