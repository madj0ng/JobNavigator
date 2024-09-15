package ru.practicum.android.diploma.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.data.network.RetrofitNetworkClient
import ru.practicum.android.diploma.domain.vacancydetails.VacancyDetailsInteractor
import ru.practicum.android.diploma.presentation.models.VacancyDetailsScreenState

class VacancyDetailsViewModel(private val vacancyDetailsInteractor: VacancyDetailsInteractor) : ViewModel() {

    private val stateLiveData: MutableLiveData<VacancyDetailsScreenState> = MutableLiveData()
    fun getVacancy(vacancyId: String) {
        stateLiveData.value = VacancyDetailsScreenState.Loading
        viewModelScope.launch {
            vacancyDetailsInteractor.getVacancy(vacancyId).collect { res ->
                if (res.error == RetrofitNetworkClient.ERROR_CODE_SERVER) {
                    stateLiveData.value = VacancyDetailsScreenState.ErrorServer
                }
                if (res.error == RetrofitNetworkClient.ERROR_CODE_INTERNET) {
                    stateLiveData.value = VacancyDetailsScreenState.ErrorNoInternet
                }
                if (res.error == RetrofitNetworkClient.RESULT_CODE_SUCCESS) {
                    stateLiveData.value = res.vacancyDetailsModel?.let { VacancyDetailsScreenState.Content(it) }
                }
            }
        }
    }

    fun getScreenLiveData(): LiveData<VacancyDetailsScreenState> {
        return stateLiveData
    }

}
