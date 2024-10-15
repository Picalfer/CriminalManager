package com.example.criminalmanager.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.criminalmanager.Constants
import com.example.criminalmanager.databinding.ActivityCrimeDetailsBinding
import com.example.criminalmanager.data.CrimeLab
import java.util.UUID

class CrimeDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCrimeDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCrimeDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val crimes = CrimeLab.getInstance(this).getCrimes()

        val crimePagerAdapter = CrimePagerAdapter(this, crimes)
        binding.viewPager.adapter = crimePagerAdapter
        val crimeId = UUID.fromString(intent.getStringExtra(Constants.CRIMINAL_KEY))

        crimeId?.let {
            val crimePosition = crimes.indexOfFirst { it.getId() == crimeId }
            binding.viewPager.setCurrentItem(crimePosition, false)
        }
    }
}