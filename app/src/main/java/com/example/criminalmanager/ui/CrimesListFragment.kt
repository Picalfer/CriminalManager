package com.example.criminalmanager.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.example.criminalmanager.Constants
import com.example.criminalmanager.R
import com.example.criminalmanager.data.CrimeLab
import com.example.criminalmanager.databinding.FragmentCrimesListBinding
import com.example.criminalmanager.model.Crime

class CrimesListFragment : Fragment() {
    private var crimes: ArrayList<Crime> = arrayListOf()
    private lateinit var adapter: CrimeListAdapter
    private lateinit var binding: FragmentCrimesListBinding
    private var selectedPosition: Int? = null
    lateinit var detailsFragment: CrimeDetailsFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        crimes = CrimeLab.getInstance(requireContext()).getCrimes()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentCrimesListBinding.inflate(inflater)

        adapter = CrimeListAdapter(
            crimes = crimes,
            onLongItemClick = { position ->
                selectedPosition = position
                requireActivity().openContextMenu(binding.rv)
                Log.d("TEST", "Selected position is: $selectedPosition")
            },
            onItemClick = { crimeIdString ->
                if (requireActivity().findViewById<ConstraintLayout>(R.id.detailedFragmentContainer) != null) {
                    detailsFragment = CrimeDetailsFragment.newInstance(
                        crimeId = crimeIdString,
                        onDataChanged = {
                            updateRecyclerView()
                        })

                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.detailedFragmentContainer,
                            detailsFragment
                        )
                        .commit()
                } else {
                    val intent = Intent(requireActivity(), CrimeDetailsActivity::class.java)
                    intent.putExtra(Constants.CRIMINAL_KEY, crimeIdString)
                    requireActivity().startActivity(intent)
                }
            }
        )

        binding.rv.adapter = adapter
        registerForContextMenu(binding.rv)

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        updateRecyclerView()
    }

    // TODO Optimize updating of RecyclerView, use diffUtils etc.
    fun updateRecyclerView() {
        adapter.notifyDataSetChanged()
    }

    override fun onPause() {
        super.onPause()
        CrimeLab.getInstance(requireContext()).saveCrimes()
    }

    override fun onCreateContextMenu(
        menu: ContextMenu, v: View, menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        requireActivity().menuInflater.inflate(R.menu.crime_list_item_context, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val crimes = CrimeLab.getInstance(requireContext()).getCrimes()
        if (selectedPosition != null && selectedPosition!! >= 0 && selectedPosition!! < crimes.size) {
            val selectedCrime = crimes[selectedPosition!!]

            return when (item.itemId) {
                R.id.menu_item_delete_crime -> {
                    CrimeLab.getInstance(requireContext()).deleteCrime(selectedCrime)

                    adapter.notifyItemRemoved(selectedPosition!!)
                    adapter.notifyItemRangeChanged(selectedPosition!!, crimes.size)

                    selectedPosition = null

                    if (::detailsFragment.isInitialized) {
                        requireActivity().supportFragmentManager.beginTransaction()
                            .remove(detailsFragment)
                            .commit()
                    }

                    true
                }

                else -> super.onContextItemSelected(item)
            }
        } else {
            Log.e("TEST", "Selected position is invalid: $selectedPosition")
            return super.onContextItemSelected(item)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = CrimesListFragment()
    }
}