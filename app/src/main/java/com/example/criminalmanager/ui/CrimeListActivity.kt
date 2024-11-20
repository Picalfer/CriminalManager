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
    private var subtitleVisibility: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCrimeListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)

        if (savedInstanceState != null) {
            subtitleVisibility = savedInstanceState.getBoolean(SUBTITLE_VISIBILITY, false)
        }

        if (subtitleVisibility == true) {
            supportActionBar?.subtitle = getString(R.string.subtitle)
        } else {
            supportActionBar?.subtitle = null
        }

        supportFragmentManager.beginTransaction()
            .add(R.id.fragmentContainer, CrimesListFragment.newInstance())
            .commit()
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.crime_menu, menu)
        val showSubtitle = menu?.findItem(R.id.menu_item_show_subtitle)

        if (subtitleVisibility == true) {
            showSubtitle?.title = getString(R.string.hide_sub)
        } else {
            showSubtitle?.title = getString(R.string.show_sub)
        }
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
            }

            R.id.menu_item_show_subtitle -> {
                if (supportActionBar?.subtitle == null) {
                    supportActionBar?.subtitle = getString(R.string.subtitle)
                    item.title = getString(R.string.hide_sub)
                    subtitleVisibility = true
                } else {
                    supportActionBar?.subtitle = null
                    item.title = getString(R.string.show_sub)
                    subtitleVisibility = false
                }
            }
        }

        return true
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean(SUBTITLE_VISIBILITY, subtitleVisibility)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        subtitleVisibility = savedInstanceState.getBoolean(SUBTITLE_VISIBILITY, false)
    }

    companion object {
        const val SUBTITLE_VISIBILITY = "SUBTITLE_VISIBILITY"
    }
}