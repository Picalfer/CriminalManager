package com.example.criminalmanager

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.criminalmanager.databinding.FragmentCrimesListBinding

class CrimesListFragment : Fragment() {
    private var crimes: MutableList<Crime> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        crimes = CrimeLab.getInstance(requireContext()).getCrimes()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentCrimesListBinding.inflate(inflater)

        val adapter = CrimeListAdapter(crimes)
        binding.rv.adapter = adapter

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            CrimesListFragment()
    }
}