package ru.practicum.android.diploma.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.favoritejobs.FavoriteJobsInteractor
import ru.practicum.android.diploma.presentation.models.FavoriteJobsScreenState

class FavoriteJobsViewModel(
    private val favoriteJobsInteractor: FavoriteJobsInteractor
) : ViewModel() {

    private val _screenLiveData = MutableLiveData<FavoriteJobsScreenState>()
    val screenLiveData: LiveData<FavoriteJobsScreenState> get() = _screenLiveData

    fun getFavoriteJobs() {
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching { favoriteJobsInteractor.getJobs() }
                .onFailure {
                    _screenLiveData.postValue(FavoriteJobsScreenState.Error)
                }
                .onSuccess { flow ->
                    flow.collect { list ->
                        if (list.isEmpty()) {
                            _screenLiveData.postValue(FavoriteJobsScreenState.Empty)
                        } else {
                            _screenLiveData.postValue(FavoriteJobsScreenState.Content(list))
                        }
                    }
                }
        }
    }
}
