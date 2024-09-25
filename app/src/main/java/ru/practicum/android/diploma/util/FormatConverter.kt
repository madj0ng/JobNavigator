package ru.practicum.android.diploma.util

import android.content.Context
import android.util.TypedValue
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.data.dto.Salary
import ru.practicum.android.diploma.domain.models.SalaryModel
import java.text.DecimalFormat

class FormatConverter(private val context: Context) {
    private val patern = "###,###,###,###,###"
    private val fdigit = 0
    private val space = " "

    private val cur: MutableMap<String, String> = mutableMapOf(
        Pair("RUR", "₽"),
        Pair("USD", "$"),
        Pair("BYR", "Br"),
        Pair("EUR", "€"),
        Pair("KZT", "₸"),
        Pair("UAH", "₴"),
        Pair("AZN", "₼"),
        Pair("UZS", "сўм"),
        Pair("GEL", "₾"),
        Pair("KGT", "с"),
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
        val list = mutableListOf<String>()
        list.add(if (salary.from != null) context.getString(R.string.salary_from, formateDecimal(salary.from)) else "")
        list.add(if (salary.to != null) context.getString(R.string.salary_to, formateDecimal(salary.to)) else "")
        list.add(getCurrencyWord(salary.currency))
        return list.joinToString(separator = space)
    }

    fun toSalaryString(salary: SalaryModel?): String {
        if (salary == null) {
            return context.getString(R.string.salary_not_specified)
        }

        return toSalaryString(Salary(salary.currency ?: "", salary.from, salary.to))
    }

    fun clearIfNotInt(str: String): String {
        try {
            return if (str.toInt() > 0) {
                str
            } else {
                ""
            }
        } catch (e: NumberFormatException) {
            return ""
        }
    }

    private fun getCurrencyWord(currency: String): String {
        return cur[currency] ?: currency
    }

    private fun formateDecimal(number: Int): String {
        val format = DecimalFormat(patern)
        format.maximumFractionDigits = fdigit
        return format.format(number).replace(",", " ")
    }
}
