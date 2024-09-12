package ru.practicum.android.diploma.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.practicum.android.diploma.presentation.viewModel.JobSearchViewModel

val viewModelModule = module {

    viewModel {
        JobSearchViewModel()
    }
}
