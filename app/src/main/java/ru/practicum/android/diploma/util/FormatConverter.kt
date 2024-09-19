package ru.practicum.android.diploma.util

import android.content.Context
import android.util.TypedValue
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.data.dto.Salary

class FormatConverter(private val context: Context) {
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

        val from = if (salary.from != null) context.getString(R.string.salary_from, salary.from.toString()) else ""
        val to = if (salary.to != null) context.getString(R.string.salary_to, salary.to.toString()) else ""
        val currency = salary.currency
        return if (from.isEmpty() && to.isEmpty()) {
            context.getString(R.string.salary_not_specified)
        } else {
            "$from$to $currency"
        }
    }
}
