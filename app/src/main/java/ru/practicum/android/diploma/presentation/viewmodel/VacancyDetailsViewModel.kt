package ru.practicum.android.diploma.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.vacancy_details.VacancyDetailsInteractor
import ru.practicum.android.diploma.presentation.models.VacancyDetailsScreenState

class VacancyDetailsViewModel(val vacancyDetailsInteractor: VacancyDetailsInteractor) : ViewModel() {

    private val stateLiveData: MutableLiveData<VacancyDetailsScreenState> = MutableLiveData()
    fun getVacancy(vacancyId: String) {
        stateLiveData.value = VacancyDetailsScreenState.Loading
        viewModelScope.launch {
            val res = vacancyDetailsInteractor.getVacancy(vacancyId)
        }
    }
}
