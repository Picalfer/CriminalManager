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
            title.text = item.getTitle()
            solved.isChecked = item.isSolved()
            date.setText(item.getDate().toString())

            root.setOnClickListener {
                val intent = Intent(root.context, CrimeDetailsActivity::class.java)
                intent.putExtra(Constants.CRIMINAL_KEY, item.getId().toString())
                root.context.startActivity(intent)
            }
        }
    }
}
