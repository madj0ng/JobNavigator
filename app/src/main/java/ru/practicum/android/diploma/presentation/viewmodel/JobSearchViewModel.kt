package ru.practicum.android.diploma.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.practicum.android.diploma.presentation.models.JobSearchScreenState
import ru.practicum.android.diploma.util.SingleLiveEvent

class JobSearchViewModel : ViewModel() {

    private val toastLiveData = SingleLiveEvent<String>()
    private val screenLiveData = MutableLiveData<JobSearchScreenState>(JobSearchScreenState.Empty)
}
