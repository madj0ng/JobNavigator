package ru.practicum.android.diploma.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.data.network.RetrofitNetworkClient
import ru.practicum.android.diploma.domain.favoritejobs.FavoriteJobsInteractor
import ru.practicum.android.diploma.domain.models.Resource
import ru.practicum.android.diploma.domain.models.VacancyDetailsModel
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
                renderState(vacancyId, res)
            }
        }
    }

    private fun renderState(id: String, res: Resource<VacancyDetailsModel>) {
        when (res) {
            is Resource.Error -> {
                when (res.resultCode) {
                    RetrofitNetworkClient.ERROR_CODE_SERVER -> stateLiveData.postValue(
                        VacancyDetailsScreenState.ErrorServer
                    )

                    RetrofitNetworkClient.ERROR_CODE_INTERNET -> getVacancyFromStorage(id)
                }
            }

            is Resource.Success -> stateLiveData.postValue(VacancyDetailsScreenState.Content(res.data))
        }
    }

    fun getScreenLiveData(): LiveData<VacancyDetailsScreenState> {
        return stateLiveData
    }

    private var _likeLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val likeLiveData: LiveData<Boolean> get() = _likeLiveData

    fun addVacansyAtFavorite(vacancyInfo: VacancyInfo, vacancyDetailsModel: VacancyDetailsModel) {
        viewModelScope.launch(Dispatchers.IO) {
            favoriteJobsInteractor.insertJob(vacancyInfo)
            favoriteJobsInteractor.insertVacancy(vacancyInfo.id, vacancyDetailsModel)
            _likeLiveData.postValue(true)
        }
    }

    fun deleteVcancyFromFavorite(vacancyInfo: VacancyInfo, vacancyDetailsModel: VacancyDetailsModel) {
        viewModelScope.launch(Dispatchers.IO) {
            favoriteJobsInteractor.deleteJob(vacancyInfo)
            favoriteJobsInteractor.deleteVacancy(vacancyInfo.id, vacancyDetailsModel)
            _likeLiveData.postValue(false)
        }
    }

    fun sharingVacancy(vacancyUrl: String) {
        sharingInteractor.sharingVacancy(vacancyUrl)
    }

    private fun getVacancyFromStorage(id: String) {
        viewModelScope.launch {
            favoriteJobsInteractor.getVacancy(id).collect { vacancy ->
                if (vacancy != null) {
                    stateLiveData.postValue(VacancyDetailsScreenState.Content(vacancy))
                } else {
                    stateLiveData.postValue(VacancyDetailsScreenState.ErrorNoInternet)
                }
            }
        }
    }
}
