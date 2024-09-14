package ru.practicum.android.diploma.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.practicum.android.diploma.presentation.models.SearchUiState
import ru.practicum.android.diploma.util.SingleLiveEvent

class JobSearchViewModel : ViewModel() {

    private var searchUiState: SearchUiState = SearchUiState.Empty()

    private val toastLiveData = SingleLiveEvent<Int>()
    private val screenLiveData = MutableLiveData<SearchUiState>(SearchUiState.Empty())

    fun getToastLiveData(): LiveData<Int> = toastLiveData
    fun getScreenLiveData(): LiveData<SearchUiState> = screenLiveData
}
