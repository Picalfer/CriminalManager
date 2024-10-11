package com.example.criminalmanager

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import com.example.criminalmanager.databinding.FragmentCrimeDetailsBinding

class CrimeDetailsFragment : Fragment() {
    private var crime: Crime = Crime()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentCrimeDetailsBinding.inflate(inflater)

        binding.crimeTitle.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                crime.setTitle(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            CrimeDetailsFragment()
    }
}