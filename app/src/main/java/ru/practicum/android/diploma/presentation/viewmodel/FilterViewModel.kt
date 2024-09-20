package ru.practicum.android.diploma.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.filters.FilterInteractor
import ru.practicum.android.diploma.domain.models.CountryModel
import ru.practicum.android.diploma.domain.models.IndustryModel
import ru.practicum.android.diploma.domain.models.RegionModel
import ru.practicum.android.diploma.domain.models.Resource
import ru.practicum.android.diploma.presentation.models.AreasScreenState

class FilterViewModel(private val filterInteractor: FilterInteractor) : ViewModel() {
    private val areaLiveData = MutableLiveData<AreasScreenState>()
    private var areaList = listOf<CountryModel>()
    private val regionsLiveData = MutableLiveData<List<RegionModel>>()
    private var regionsList = listOf<RegionModel>()
    private var selectedCountry: CountryModel? = null
    private var selectIndustry: IndustryModel? = null
    private var selectRegion: RegionModel? = null
    private var selectIndustryLiveData = MutableLiveData<IndustryModel?>()
    private val industryLiveData = MutableLiveData<Resource<List<IndustryModel>>>()
    private var industryList = listOf<IndustryModel>()
    private var savedIndustry: IndustryModel? = null
    private var savedRegion: RegionModel? = null
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
        savedRegion = selectRegion
    }

    fun getRegionSaved(): RegionModel? {
        return savedRegion
    }

    fun getCountrySaved(): CountryModel? {
        return savedCountry
    }

    fun getRegionLiveData(): LiveData<List<RegionModel>> {
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

    fun getSelectedRegion(): RegionModel? {
        return selectRegion
    }

    fun selectRegion(regionModel: RegionModel) {
        selectRegion = regionModel
    }

    fun unSelectCountry() {
        selectedCountry = null
    }

    fun searchRegion(strRegion: String) {
        val listRes = regionsList.filter { area ->
            area.name.lowercase().contains(strRegion.lowercase())
        }
        regionsLiveData.value = listRes
    }

    fun getRegions() {
        if (areaList.isEmpty()) {
            viewModelScope.launch {
                filterInteractor.getAreas().collect { res ->
                    if (res !is Resource.Error) {
                        areaList = (res as Resource.Success).data
                        if (selectedCountry != null) {
                            regionsLiveData.value = selectedCountry!!.regions
                            regionsList = selectedCountry!!.regions
                        } else {
                            val result = mutableListOf<RegionModel>()
                            for (country in areaList) {
                                result.addAll(country.regions)
                            }
                            regionsList = result
                            regionsLiveData.value = result
                        }
                    } else {
//TODO
                    }
                }
            }

        }
        if (selectedCountry != null) {
            regionsLiveData.value = selectedCountry!!.regions
            regionsList = selectedCountry!!.regions
        } else {
            val result = mutableListOf<RegionModel>()
            for (country in areaList) {
                result.addAll(country.regions)
            }
            regionsList = result
            regionsLiveData.value = result
        }
    }

    fun getIndustries() {
        viewModelScope.launch {
            filterInteractor.getIndustrias().collect() { res ->
                industryLiveData.value = res
                industryList = (res as Resource.Success).data
            }
        }
    }

    fun selectIndustry(industryModel: IndustryModel) {
        selectIndustry = industryModel
        selectIndustryLiveData.value = industryModel
    }

    fun unSelectIndustry() {
        selectIndustry = null
        selectIndustryLiveData.value = null
    }

    fun searchIndustry(strIndustry: String) {
        if (!strIndustry.isEmpty()) {
            Log.d("AAAAAA", strIndustry.toString())
            val listRes = industryList.filter { industry ->

                industry.name.lowercase().contains(strIndustry.lowercase())
            }
            Log.d("listRes", industryList.toString())

            Log.d("listRes", listRes.toString())

            industryLiveData.value = Resource.Success(listRes)
        } else {
            industryLiveData.value = Resource.Success(industryList)
        }
    }

    fun saveIndustry() {
        savedIndustry = selectIndustry
        Log.d("savedIndustry", savedIndustry.toString())
    }

    fun getSavedIndustry(): IndustryModel? {
        return savedIndustry
    }

    fun getSelectedIndustryLiveData(): LiveData<IndustryModel?> {
        return selectIndustryLiveData
    }

    fun getIndustryLiveData(): LiveData<Resource<List<IndustryModel>>> {
        return industryLiveData
    }
}
