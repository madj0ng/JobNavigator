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
import ru.practicum.android.diploma.domain.models.RegionModel
import ru.practicum.android.diploma.domain.models.Resource
import ru.practicum.android.diploma.presentation.models.AreasScreenState
import ru.practicum.android.diploma.presentation.models.IndustryScreenState
import ru.practicum.android.diploma.presentation.models.RegionScreenState

class FilterViewModel(
    private val filterInteractor: FilterInteractor
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

    private var areaList = listOf<CountryModel>()
    private var regionsList = listOf<RegionModel>()
    private var cityList = listOf<CityModel>()
    private var industryList = listOf<IndustryModel>()
    var selectedCountry: CountryModel? = null
    private var selectIndustry: IndustryModel? = null
    var selectRegion: RegionModel? = null
    var selectCity: CityModel? = null
    var savedIndustry: IndustryModel? = null
    var saveRegion: RegionModel? = null
    var savedCity: CityModel? = null
    var savedCountry: CountryModel? = null
    var salaryBase: Int? = null
    private var doNotShowWithoutSalary: Boolean = false

    fun setDontShowWithoutSalary(show: Boolean) {
        doNotShowWithoutSalary = show
        viewModelScope.launch {
            filterInteractor.saveOnlyWithSalary(show)
        }
    }

    fun setSalary(salary: String) {
        if (salary.isEmpty()) {
            salaryBase = null
        } else {
            salaryBase = salary.toInt()
            viewModelScope.launch {
                filterInteractor.saveSalary(salaryBase!!)
            }
        }
    }

    fun getAreas() {
        viewModelScope.launch {
            filterInteractor.getAreas().collect { res ->
                if (res !is Resource.Error) {
                    areaList = (res as Resource.Success).data
                    _areaLiveData.value = AreasScreenState.Content((res as Resource.Success).data)
                } else {
                    _areaLiveData.value = AreasScreenState.Error
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
        }
    }

    fun selectCountry(country: CountryModel) {
        selectedCountry = country
        selectRegion = null
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
                _regionsLiveData.value = RegionScreenState.ErrorNoRegion
            } else {
                _regionsLiveData.value = RegionScreenState.Content(listRes)
            }
        } else {
            _regionsLiveData.value = RegionScreenState.Content(regionsList)
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
            _regionsLiveData.value = RegionScreenState.Content(regionsList)
        } else {
            regionsList = selectedCountry!!.regions
            _regionsLiveData.value = RegionScreenState.Content(regionsList)
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

    fun getIndustries() {
        viewModelScope.launch {
            filterInteractor.getIndustrias().collect() { res ->
                when (res) {
                    is Resource.Error -> _industryLiveData.value = IndustryScreenState.ErrorInternet
                    is Resource.Success -> {
                        industryList = res.data
                        _industryLiveData.value = IndustryScreenState.Content(industryList)
                    }
                }
            }
        }
    }

    fun selectIndustry(industryModel: IndustryModel?) {
        selectIndustry = industryModel
        _selectIndustryLiveData.value = industryModel
    }

    fun unSelectIndustry() {
        selectIndustry = null
        savedIndustry = null
        _selectIndustryLiveData.value = null
    }

    fun searchIndustry(strIndustry: String) {
        if (strIndustry.isNotEmpty()) {
            val listRes = industryList.filter { industry ->

                industry.name.lowercase().contains(strIndustry.lowercase())
            }

            if (listRes.isEmpty()) {
                _industryLiveData.value = IndustryScreenState.ErrorContent
            } else {
                _industryLiveData.value = IndustryScreenState.Content(listRes)
            }
        } else {
            _industryLiveData.value = IndustryScreenState.Content(industryList)
        }
    }

    fun saveIndustry() {
        savedIndustry = selectIndustry
        viewModelScope.launch {
            filterInteractor.saveIndustries(IndustriesFilterModel(savedIndustry!!.id, savedIndustry!!.name))
        }
    }

    fun saveFilter(filterModel: FilterModel?) {
        viewModelScope.launch(Dispatchers.IO) {
            filterInteractor.saveFilter(filterModel)
        }
    }

    fun getFilter() {
        viewModelScope.launch(Dispatchers.IO) {
            val filterModel = filterInteractor.getFilter()
            if (filterModel != null) {
                getAreas()
                if (filterModel.country!!.id.isNotEmpty()) {
                    areaList.forEach {
                        if (it.id == filterModel.country.id) {
                            savedCountry = it
                            selectedCountry = it
                        }
                    }
                }
                if (!filterModel.area?.id.isNullOrEmpty()) {
                    selectedCountry?.regions?.forEach {
                        if (it.city.isNotEmpty()) {
                            it.city.forEach {
                                if (it.id == filterModel.area?.id) {
                                    savedCity = it
                                    selectCity = it
                                }
                            }
                        }
                        if (it.id == filterModel.area?.id) {
                            saveRegion = it
                            selectRegion = it
                        }
                    }
                }
                if (filterModel.industries!!.id.isNotEmpty()) {
                    savedIndustry = IndustryModel(filterModel.industries.id, filterModel.industries.name)
                }
                salaryBase = filterModel.salary
                doNotShowWithoutSalary = filterModel.onlyWithSalary ?: false
                viewModelScope.launch(Dispatchers.Main) {
                    _searchFilterLiveData.value = filterModel
                }
                selectRegion = saveRegion
                selectCity = savedCity
                selectIndustry = savedIndustry
            } else {
                viewModelScope.launch(Dispatchers.Main) {
                    _searchFilterLiveData.value = null
                }
            }
        }
    }
}
