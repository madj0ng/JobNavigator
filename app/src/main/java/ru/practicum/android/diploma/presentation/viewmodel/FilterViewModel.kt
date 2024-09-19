package ru.practicum.android.diploma.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.filters.FilterInteractor
import ru.practicum.android.diploma.domain.models.CountryModel
import ru.practicum.android.diploma.domain.models.IndustryModel
import ru.practicum.android.diploma.domain.models.Resource
import ru.practicum.android.diploma.domain.models.VacancyModel
import ru.practicum.android.diploma.presentation.models.JobSearchScreenState

class FilterViewModel(val filterInteractor: FilterInteractor) : ViewModel() {
    val areaLiveData = MutableLiveData<Resource<List<CountryModel>>>()
    val industryLiveData = MutableLiveData<Resource<List<IndustryModel>>>()

    fun getAreaLiveData(): Resource<List<IndustryModel>> {
        viewModelScope.launch {
            filterInteractor.getAreas().collect {res ->
                renderState(res)
            }
        }
    }


}
