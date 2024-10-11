package com.example.criminalmanager

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.criminalmanager.databinding.CriminalItemBinding

class CrimeListAdapter : RecyclerView.Adapter<CrimeListAdapter.CrimeListViewHolder>() {
    val items = createItems()

    fun createItems(): ArrayList<Crime> {
        val crimes = ArrayList<Crime>()
        for (i in 0..10) {
            val crime: Crime = Crime("Crime #" + i)
            crimes.add(crime);
        }
        return crimes
    }


    class CrimeListViewHolder(val binding: CriminalItemBinding) : RecyclerView.ViewHolder(binding.root)

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
                val intent = Intent(root.context, CrimeDetailsActivity::class.java)
                intent.putExtra("criminal", item.getId().toString())
                root.context.startActivity(intent)
            }
        }

    }
}
