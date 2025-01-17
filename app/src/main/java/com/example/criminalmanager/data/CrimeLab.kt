package com.example.criminalmanager.data

import android.content.Context
import android.util.Log
import com.example.criminalmanager.model.Crime
import java.util.UUID

class CrimeLab(context: Context) {
    private val crimes = arrayListOf<Crime>()
    private val serializer = CriminalManagerJSONSerializer(context, FILENAME)

    init {
        try {
            crimes.addAll(serializer.loadCrimes())
        } catch (e: Exception) {
            Log.e(TAG, "Error loading crimes: ", e)
        }
    }

    fun getCrimes(): ArrayList<Crime> {
        return crimes
    }

    fun addCrime(crime: Crime) {
        crimes.add(crime)
    }

    fun deleteCrime(crime: Crime) {
        crimes.remove(crime)
    }

    fun getCrime(id: UUID): Crime? {
        for (crime in crimes) {
            if (crime.getId() == id) {
                return crime
            }
        }
        return null
    }

    fun saveCrimes(): Boolean {
        try {
            serializer.saveCrimes(crimes)
            Log.d(TAG, "Crimes saved to file")
            return true
        } catch (e: Exception) {
            Log.d(TAG, "Error saving crimes: ", e)
            return false
        }
    }

    companion object {
        const val FILENAME = "crimes.json"
        const val TAG = "CrimeLab"

        private var crimeLab: CrimeLab? = null

        @JvmStatic
        fun getInstance(context: Context): CrimeLab {
            if (crimeLab == null) {
                crimeLab = CrimeLab(context)
                return crimeLab as CrimeLab
            }
            return crimeLab!!
        }
    }
}