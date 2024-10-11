package com.example.criminalmanager

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.criminalmanager.databinding.ActivityCrimeDetailsBinding

class CrimeDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCrimeDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCrimeDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.beginTransaction()
            .add(R.id.fragmentContainer, CrimeDetailsFragment.newInstance())
            .commit()

        val name = intent?.getStringExtra(Constants.CRIMINAL_KEY)
    }
}