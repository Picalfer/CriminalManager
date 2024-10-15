package com.example.criminalmanager.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.criminalmanager.model.Crime

class CrimePagerAdapter(fa: FragmentActivity, private val crimes: MutableList<Crime>) :
    FragmentStateAdapter(fa) {

    override fun getItemCount(): Int = crimes.size

    override fun createFragment(position: Int): Fragment {
        val crime = crimes[position]
        return CrimeDetailsFragment.newInstance(crime.getId().toString())
    }
}
