package com.example.criminalmanager.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.criminalmanager.model.Crime
import com.example.criminalmanager.databinding.FragmentCrimesListBinding
import com.example.criminalmanager.data.CrimeLab

class CrimesListFragment : Fragment() {
    private var crimes: MutableList<Crime> = mutableListOf()
    private lateinit var adapter: CrimeListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        crimes = CrimeLab.getInstance(requireContext()).getCrimes()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentCrimesListBinding.inflate(inflater)

        adapter = CrimeListAdapter(crimes)
        binding.rv.adapter = adapter

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        adapter.notifyDataSetChanged()
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            CrimesListFragment()
    }
}