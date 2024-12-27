package com.example.criminalmanager.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.criminalmanager.Utils
import com.example.criminalmanager.databinding.CriminalItemBinding
import com.example.criminalmanager.model.Crime

class CrimeListAdapter(
    private var crimes: MutableList<Crime>,
    private val onItemClick: (String) -> Unit,
    private val onLongItemClick: (Int) -> Unit,
) :
    RecyclerView.Adapter<CrimeListAdapter.CrimeListViewHolder>() {

    class CrimeListViewHolder(val binding: CriminalItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CrimeListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CriminalItemBinding.inflate(inflater, parent, false)
        return CrimeListViewHolder(binding)
    }

    override fun getItemCount(): Int = crimes.size

    override fun onBindViewHolder(holder: CrimeListViewHolder, position: Int) {
        val crime = crimes[position]

        with(holder.binding) {
            title.text = crime.getTitle()
            solved.isChecked = crime.isSolved()
            date.text = Utils.getFullDateOfCrime(crime)

            root.setOnClickListener(null)
            root.setOnLongClickListener(null)

            root.setOnClickListener {
                onItemClick(crime.getId().toString())
            }

            root.setOnLongClickListener {
                onLongItemClick(position)
                true
            }
        }
    }

    fun updateCrimes(newCrimes: MutableList<Crime>) {
        crimes.clear()
        crimes.addAll(newCrimes)
        notifyDataSetChanged()
    }
}
