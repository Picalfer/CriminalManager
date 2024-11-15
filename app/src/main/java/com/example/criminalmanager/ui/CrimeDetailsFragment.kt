package com.example.criminalmanager.ui

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.criminalmanager.Constants
import com.example.criminalmanager.Utils
import com.example.criminalmanager.data.CrimeLab
import com.example.criminalmanager.databinding.FragmentCrimeDetailsBinding
import com.example.criminalmanager.model.Crime
import java.util.Calendar
import java.util.UUID

class CrimeDetailsFragment : Fragment() {
    private var crime: Crime = Crime()
    private lateinit var binding: FragmentCrimeDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val crimeId = arguments?.getString(Constants.CRIMINAL_KEY)
        crime =
            CrimeLab.getInstance(requireActivity()).getCrime(UUID.fromString(crimeId)) ?: Crime()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCrimeDetailsBinding.inflate(inflater)
        updateScreenData()

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

        binding.crimeDate.setOnClickListener {

            DatePickerDialog(
                requireActivity(), dateSetListener,
                crime.getDate().get(Calendar.YEAR),
                crime.getDate().get(Calendar.MONTH),
                crime.getDate().get(Calendar.DAY_OF_MONTH)
            )
                .show()
        }

        binding.crimeTime.setOnClickListener {

            TimePickerDialog(
                requireActivity(), timeSetListener,
                crime.getDate().get(Calendar.HOUR_OF_DAY),
                crime.getDate().get(Calendar.MINUTE),
                true
            )
                .show()
        }

        return binding.root
    }

    private fun updateScreenData() {
        binding.crimeTitle.setText(crime.getTitle())
        binding.crimeDate.text = Utils.getStringDateOfCrime(crime)
        binding.crimeSolved.isChecked = crime.isSolved()
        binding.crimeTime.text = Utils.getStringTimeOfCrime(crime)
    }

    private var dateSetListener: OnDateSetListener =
        OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            updateCrimeDate(year, monthOfYear, dayOfMonth)
        }

    private var timeSetListener: OnTimeSetListener =
        OnTimeSetListener { view, hour, minute ->
            updateCrimeTime(hour, minute)
        }

    private fun updateCrimeDate(year: Int, month: Int, day: Int) {
        val newCrimeDate = crime.getDate()
        newCrimeDate.set(Calendar.YEAR, year)
        newCrimeDate.set(Calendar.MONTH, month)
        newCrimeDate.set(Calendar.DAY_OF_MONTH, day)

        crime.setDate(newCrimeDate)
        updateScreenData()
    }

    private fun updateCrimeTime(hour: Int, minute: Int) {
        val newCrimeTime = crime.getDate()
        newCrimeTime.set(Calendar.HOUR_OF_DAY, hour)
        newCrimeTime.set(Calendar.MINUTE, minute)

        crime.setDate(newCrimeTime)
        updateScreenData()
    }

    companion object {
        fun newInstance(crimeId: String): CrimeDetailsFragment {
            val args = Bundle().apply {
                putString(Constants.CRIMINAL_KEY, crimeId)
            }
            val fragment = CrimeDetailsFragment()
            fragment.arguments = args
            return fragment
        }
    }
}