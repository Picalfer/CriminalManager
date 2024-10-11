package com.example.criminalmanager

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.criminalmanager.databinding.ActivityCrimeListBinding

class CrimeListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCrimeListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCrimeListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.beginTransaction()
            .add(R.id.fragmentContainer, CrimesListFragment.newInstance())
            .commit()
    }
}