package ru.practicum.android.diploma.data.sharing

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat.startActivity
import ru.practicum.android.diploma.domain.sharing.SharingRepository

class SharingRepositoryImpl(
    private val context: Context
) : SharingRepository {
    override fun sharingVacancy(vacancyUrl: String) {
        val intent = Intent()
        intent.action = Intent.ACTION_SEND
        intent.putExtra(Intent.EXTRA_TEXT, vacancyUrl)
        intent.type = "text/plain"
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }
}
