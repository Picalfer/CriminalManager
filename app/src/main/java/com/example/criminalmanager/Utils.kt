package com.example.criminalmanager

import java.util.Calendar

object Utils {

    fun getStringDateOfCrime(crime: Crime): String {
        val year = crime.getDate().get(Calendar.YEAR).toString()
        val month = crime.getDate().get(Calendar.MONTH).toString()
        val day = crime.getDate().get(Calendar.DAY_OF_MONTH).toString()

        return "$year, $month, $day"
    }
}