package ru.practicum.android.diploma.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.practicum.android.diploma.presentation.viewmodel.JobSearchViewModel
import ru.practicum.android.diploma.presentation.viewmodel.VacancyDetailsViewModel

val viewModelModule = module {

    viewModel {
        JobSearchViewModel(get())
    }
    viewModel {
        VacancyDetailsViewModel(get())
    }
}
