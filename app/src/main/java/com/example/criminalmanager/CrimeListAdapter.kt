package com.example.criminalmanager

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.criminalmanager.databinding.CriminalItemBinding

class CrimeListAdapter(private val items: MutableList<Crime>) :
    RecyclerView.Adapter<CrimeListAdapter.CrimeListViewHolder>() {

    class CrimeListViewHolder(val binding: CriminalItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CrimeListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CriminalItemBinding.inflate(inflater, parent, false)
        return CrimeListViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: CrimeListViewHolder, position: Int) {
        val item = items[position]

        with(holder.binding) {
            tv.text = item.getTitle()

            root.setOnClickListener {
                Log.d("TEST", item.getTitle() + " was clicked");
            }
        }
    }
}
