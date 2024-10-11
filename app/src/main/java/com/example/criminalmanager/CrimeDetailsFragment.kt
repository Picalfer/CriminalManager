package com.example.criminalmanager

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import com.example.criminalmanager.databinding.FragmentCrimeDetailsBinding
import com.google.android.material.button.MaterialButton
import com.google.android.material.button.MaterialButton.OnCheckedChangeListener
import com.google.android.material.internal.MaterialCheckable
import java.util.UUID

class CrimeDetailsFragment : Fragment() {
    private var crime: Crime = Crime()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val crimeId = requireActivity().intent?.getStringExtra(Constants.CRIMINAL_KEY)
        Log.d("TEST", crimeId.toString())
        crime = CrimeLab.getInstance(requireActivity()).getCrime(UUID.fromString(crimeId)) ?: Crime()
        Log.d("TEST", crime.getTitle())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentCrimeDetailsBinding.inflate(inflater)

        binding.crimeTitle.setText(crime.getTitle())
        binding.crimeDate.text = crime.getDate().toString()
        binding.crimeSolved.isChecked = crime.isSolved()

        binding.crimeTitle.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                crime.setTitle(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        binding.crimeSolved.setOnCheckedChangeListener { buttonView, isChecked ->
            crime.setSolved(isChecked)
        }

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            CrimeDetailsFragment()
    }
}