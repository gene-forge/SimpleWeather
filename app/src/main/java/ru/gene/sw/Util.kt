package ru.gene.sw

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

object Util {
    private val dateTimeInFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
    private val dateOnlyInFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    private val timeOut = DateTimeFormatter.ofPattern("HH:mm")

    private var appLocale: Locale = if (Locale.getDefault().language == "ru") {
        Locale("ru")
    } else {
        Locale.ENGLISH
    }


    fun formatTime(localtime: String): String =
        LocalDateTime.parse(localtime, dateTimeInFormatter).format(timeOut)

    fun formatDate(localtime: String): String {
        val dt = LocalDateTime.parse(localtime, dateTimeInFormatter)
        val dow = dt.dayOfWeek.getDisplayName(TextStyle.FULL, appLocale)
        val month = dt.month.getDisplayName(TextStyle.FULL, appLocale)
        return "$dow, ${dt.dayOfMonth} $month"
    }

    fun formatDayLabel(date: String): String {
        val d = LocalDate.parse(date, dateOnlyInFormatter)
        val dow = d.dayOfWeek.getDisplayName(TextStyle.FULL, appLocale)
        val month = d.month.getDisplayName(TextStyle.FULL, appLocale)
        return "$dow, ${d.dayOfMonth} $month"
    }

    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false

        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}