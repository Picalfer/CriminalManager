package com.example.criminalmanager

import com.example.criminalmanager.model.Crime
import java.util.Calendar
import java.util.Locale

object Utils {

    fun getStringDateOfCrime(crime: Crime): String {
        val year = crime.getDate().get(Calendar.YEAR).toString()
        val month = crime.getDate().get(Calendar.MONTH).toString()
        val day = crime.getDate().get(Calendar.DAY_OF_MONTH).toString()

        return "$year/$month/$day"
    }

    fun getFullDateOfCrime(crime: Crime): String {
        val date = getStringDateOfCrime(crime)
        val time = getStringTimeOfCrime(crime)

        return "$date ($time)"
    }

    fun getStringTimeOfCrime(crime: Crime): String {
        val hour =
            crime.getDate().get(Calendar.HOUR_OF_DAY).toString()
        var minute =
            crime.getDate().get(Calendar.MINUTE).toString()

        if (minute.toInt() < 10) minute = String.format(Locale.getDefault(), "0%s", minute)

        return "$hour:$minute"
    }
}