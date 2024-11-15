package com.example.criminalmanager.ui

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NavUtils
import com.example.criminalmanager.Constants
import com.example.criminalmanager.data.CrimeLab
import com.example.criminalmanager.databinding.ActivityCrimeDetailsBinding
import java.util.UUID

class CrimeDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCrimeDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCrimeDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        if (NavUtils.getParentActivityName(this) != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        val crimes = CrimeLab.getInstance(this).getCrimes()

        val crimePagerAdapter = CrimePagerAdapter(this, crimes)
        binding.viewPager.adapter = crimePagerAdapter
        val crimeId = UUID.fromString(intent.getStringExtra(Constants.CRIMINAL_KEY))

        crimeId?.let {
            val crimePosition = crimes.indexOfFirst { it.getId() == crimeId }
            binding.viewPager.setCurrentItem(crimePosition, false)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                if (NavUtils.getParentActivityName(this) != null) {
                    NavUtils.navigateUpFromSameTask(this)
                }
                return true
            }

            else -> return false
        }
    }
}