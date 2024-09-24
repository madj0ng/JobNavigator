package ru.practicum.android.diploma.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.data.network.RetrofitNetworkClient
import ru.practicum.android.diploma.domain.favoritejobs.FavoriteJobsInteractor
import ru.practicum.android.diploma.domain.sharing.SharingInteractor
import ru.practicum.android.diploma.domain.vacancydetails.VacancyDetailsInteractor
import ru.practicum.android.diploma.presentation.models.VacancyDetailsScreenState
import ru.practicum.android.diploma.presentation.models.VacancyInfo

class VacancyDetailsViewModel(
    private val vacancyDetailsInteractor: VacancyDetailsInteractor,
    private val favoriteJobsInteractor: FavoriteJobsInteractor,
    private val sharingInteractor: SharingInteractor
) : ViewModel() {

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

    private var _likeLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val likeLiveData: LiveData<Boolean> get() = _likeLiveData

    fun addVacansyAtFavorite(vacancyInfo: VacancyInfo) {
        viewModelScope.launch(Dispatchers.IO) {
            favoriteJobsInteractor.insertJob(vacancyInfo)
            _likeLiveData.postValue(true)
        }
    }

    fun deleteVcancyFromFavorite(vacancyInfo: VacancyInfo) {
        viewModelScope.launch(Dispatchers.IO) {
            favoriteJobsInteractor.deleteJob(vacancyInfo)
            _likeLiveData.postValue(false)
        }
    }

    fun sharingVacancy(vacancyUrl: String) {
        sharingInteractor.sharingVacancy(vacancyUrl)
    }
}
