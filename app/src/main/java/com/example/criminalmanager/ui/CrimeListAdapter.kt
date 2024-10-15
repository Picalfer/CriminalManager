package com.example.criminalmanager.ui

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.criminalmanager.Constants
import com.example.criminalmanager.model.Crime
import com.example.criminalmanager.Utils
import com.example.criminalmanager.databinding.CriminalItemBinding

class CrimeListAdapter(private val crimes: MutableList<Crime>) :
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
            date.text = Utils.getStringDateOfCrime(crime)

            root.setOnClickListener {
                val intent = Intent(root.context, CrimeDetailsActivity::class.java)
                intent.putExtra(Constants.CRIMINAL_KEY, crime.getId().toString())
                root.context.startActivity(intent)
            }
        }
    }
}
