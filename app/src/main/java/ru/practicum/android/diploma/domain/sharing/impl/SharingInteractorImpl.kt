package ru.practicum.android.diploma.domain.sharing.impl

import ru.practicum.android.diploma.domain.sharing.SharingInteractor
import ru.practicum.android.diploma.domain.sharing.SharingRepository

class SharingInteractorImpl(
    private val repository: SharingRepository
) : SharingInteractor {
    override fun sharingVacancy(vacancyUrl: String) {
        repository.sharingVacancy(vacancyUrl)
    }
}
