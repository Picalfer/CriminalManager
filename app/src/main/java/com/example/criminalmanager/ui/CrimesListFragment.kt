package com.example.criminalmanager.ui

import android.os.Bundle
import android.util.Log
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.criminalmanager.R
import com.example.criminalmanager.data.CrimeLab
import com.example.criminalmanager.databinding.FragmentCrimesListBinding
import com.example.criminalmanager.model.Crime

class CrimesListFragment : Fragment() {
    private var crimes: MutableList<Crime> = mutableListOf()
    private lateinit var adapter: CrimeListAdapter
    private lateinit var binding: FragmentCrimesListBinding
    private var selectedPosition: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        crimes = CrimeLab.getInstance(requireContext()).getCrimes()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCrimesListBinding.inflate(inflater)

        adapter = CrimeListAdapter(crimes) { position ->
            Log.d("TEST", "listener function")
            selectedPosition = position
            requireActivity().openContextMenu(binding.rv)
        }
        binding.rv.adapter = adapter
        registerForContextMenu(binding.rv)

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        adapter.notifyDataSetChanged()
    }

    override fun onPause() {
        super.onPause()
        CrimeLab.getInstance(requireContext()).saveCrimes()
    }

    override fun onCreateContextMenu(
        menu: ContextMenu,
        v: View,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        requireActivity().menuInflater.inflate(R.menu.crime_list_item_context, menu)
        Log.d("TEST", "Context menu created")
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val crimes = CrimeLab.getInstance(requireContext()).getCrimes()
        val selectedCrime = crimes[selectedPosition!!]

        return when (item.itemId) {
            R.id.menu_item_delete_crime -> {
                Log.d("TEST", "Delete button clicked")
                CrimeLab.getInstance(requireContext()).deleteCrime(selectedCrime)
                adapter.notifyDataSetChanged()
                true
            }

            else -> super.onContextItemSelected(item)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            CrimesListFragment()
    }
}