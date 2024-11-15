package com.example.criminalmanager.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.criminalmanager.Constants
import com.example.criminalmanager.R
import com.example.criminalmanager.data.CrimeLab
import com.example.criminalmanager.databinding.ActivityCrimeListBinding
import com.example.criminalmanager.model.Crime

class CrimeListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCrimeListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCrimeListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)

        supportFragmentManager.beginTransaction()
            .add(R.id.fragmentContainer, CrimesListFragment.newInstance())
            .commit()
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.crime_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_item_new_crime -> {
                val crime = Crime()
                CrimeLab.getInstance(this).addCrime(crime)
                val intent = Intent(this, CrimeDetailsActivity::class.java)
                intent.putExtra(Constants.CRIMINAL_KEY, crime.getId().toString())
                startActivity(intent)
                return true
            }

            else -> return false
        }
    }
}