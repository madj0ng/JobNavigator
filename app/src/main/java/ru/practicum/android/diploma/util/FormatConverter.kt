package ru.practicum.android.diploma.util

import android.content.Context
import android.util.TypedValue
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.data.dto.Salary
import ru.practicum.android.diploma.domain.models.SalaryModel

class FormatConverter(private val context: Context) {
    private val cur: MutableMap<String, String> = mutableMapOf(
        Pair("RUR", "₽"),
        Pair("USD", "$"),
        Pair("BYR", "Br"),
        Pair("EUR", "€"),
        Pair("KZT", "₸"),
        Pair("UAH", "₴"),
        Pair("AZN", "₼"),
        Pair("UZS", "\u20C0"),
        Pair("GEL", "₾"),
        Pair("KGT", "\u20C0"),
    )

    fun dpToPx(dp: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics
        ).toInt()
    }

    fun toSalaryString(salary: Salary?): String {
        if (salary == null) {
            return context.getString(R.string.salary_not_specified)
        }
        val space = " "
        val list = mutableListOf<String>()
        list.add(if (salary.from != null) context.getString(R.string.salary_from, salary.from.toString()) else "")
        list.add(if (salary.to != null) context.getString(R.string.salary_to, salary.to.toString()) else "")
        list.add(getCurrencyWord(salary.currency))
        return list.joinToString(separator = space)
    }

    fun toSalaryString(salary: SalaryModel?): String {
        if (salary == null) {
            return context.getString(R.string.salary_not_specified)
        }

        return toSalaryString(Salary(salary.currency ?: "", salary.from, salary.to))
    }

    private fun getCurrencyWord(currency: String): String {
        return cur.get(currency) ?: ""
    }
}
