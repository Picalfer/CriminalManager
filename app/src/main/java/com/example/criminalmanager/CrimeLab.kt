package com.example.criminalmanager

import android.content.Context
import java.util.UUID

class CrimeLab(private val context: Context) {
    private val crimes = mutableListOf<Crime>()

    init {
        for (i in 0..20) {
            val crime: Crime = Crime("Crime #$i", i % 2 == 0)
            crimes.add(crime);
        }
    }

    fun getCrimes(): MutableList<Crime> {
        return crimes
    }

    fun getCrime(id: UUID): Crime? {
        for (crime in crimes) {
            if (crime.getId() == id) {
                return crime
            }
        }
        return null
    }

    companion object {
        private var crimeLab: CrimeLab? = null

        @JvmStatic
        fun getInstance(context: Context): CrimeLab {
            if (crimeLab == null) {
                return CrimeLab(context)
            }
            return crimeLab!!
        }
    }
}