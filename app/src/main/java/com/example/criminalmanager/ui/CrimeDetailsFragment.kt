package com.example.criminalmanager.ui

import android.Manifest
import android.app.Activity.RESULT_OK
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.text.Editable
import android.text.TextWatcher
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.criminalmanager.Constants
import com.example.criminalmanager.R
import com.example.criminalmanager.Utils
import com.example.criminalmanager.data.CrimeLab
import com.example.criminalmanager.databinding.FragmentCrimeDetailsBinding
import com.example.criminalmanager.model.Crime
import com.example.criminalmanager.model.Photo
import java.util.Calendar
import java.util.UUID

class CrimeDetailsFragment : Fragment() {
    private var crime: Crime = Crime()
    private lateinit var binding: FragmentCrimeDetailsBinding

    private var cameraLauncher: ActivityResultLauncher<Intent>? = null
    private val pickContactLauncher =
        registerForActivityResult(ActivityResultContracts.PickContact()) { contactData ->
            contactData?.let { uri ->

                val projection = arrayOf(
                    ContactsContract.Contacts.DISPLAY_NAME, // Имя контакта
                    ContactsContract.Contacts._ID,          // Уникальный идентификатор контакта
                    ContactsContract.Contacts.HAS_PHONE_NUMBER // Флаг, указывающий, есть ли у контакта номер телефона
                )

                val phoneCursor: Cursor? =
                    requireContext().contentResolver.query(uri, projection, null, null, null)
                if (phoneCursor != null && phoneCursor.moveToFirst()) {
                    val contactNameIndex =
                        phoneCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
                    val idIndex = phoneCursor.getColumnIndex(ContactsContract.Contacts._ID)
                    val hasPhoneNumberIndex =
                        phoneCursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)

                    // Проверяем, что индексы действительны
                    if (contactNameIndex >= 0 && idIndex >= 0 && hasPhoneNumberIndex >= 0) {
                        val contactName: String = phoneCursor.getString(contactNameIndex)
                        val id: String = phoneCursor.getString(idIndex)

                        if (phoneCursor.getInt(hasPhoneNumberIndex) > 0) {
                            // Запрос номеров телефонов
                            val phonesProjection = arrayOf(
                                ContactsContract.CommonDataKinds.Phone.NUMBER
                            )

                            val phonesCursor = requireContext().contentResolver.query(
                                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                phonesProjection,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                                arrayOf(id),
                                null
                            )

                            // Проверяем, что курсор не равен null и содержит данные
                            if (phonesCursor != null) {
                                while (phonesCursor.moveToNext()) {
                                    val phoneNumberIndex =
                                        phonesCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                                    if (phoneNumberIndex >= 0) {
                                        val phoneNumber = phonesCursor.getString(phoneNumberIndex)
                                        Log.d("TEST", phoneNumber)
                                    } else {
                                        Log.e("TEST Error", "Phone number column index is invalid.")
                                    }
                                }
                                phonesCursor.close()
                            } else {
                                Log.e("TEST Error", "Phones cursor is null.")
                            }
                        }

                        Log.d("TEST Name", contactName)
                        val textSuspect = getString(R.string.crime_report_suspect, contactName)
                        Log.d("TEST Name", textSuspect)
                        binding.suspectBtn.text = textSuspect
                        crime.setSuspect(contactName)
                    } else {
                        Log.e("TEST Error", "One or more column indices are invalid.")
                    }
                } else {
                    Log.e("TEST Error", "Phone cursor is null or empty.")
                }
                phoneCursor?.close()
            }
        }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                // Разрешение предоставлено, запускаем выбор контакта
                pickContactLauncher.launch(null)
            } else {
                // Разрешение не предоставлено, уведомите пользователя
                Toast.makeText(
                    requireContext(),
                    "Permission denied to read contacts",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

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

        cameraLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
                if (result.resultCode == RESULT_OK) {
                    val filePath = result.data?.getStringExtra("filepath")
                    Log.d("TEST", filePath.toString())

                    if (filePath != null) {
                        val photo = Photo(filePath)
                        crime.setPhoto(photo)
                        showPhoto()
                    }
                }
            }

        with(binding) {
            crimeTitle.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    crime.setTitle(s.toString())
                    onDataChanged()
                }

                override fun afterTextChanged(s: Editable?) {}
            })

            crimeSolved.setOnCheckedChangeListener { buttonView, isChecked ->
                crime.setSolved(isChecked)
                onDataChanged()
            }

            crimeDate.setOnClickListener {
                DatePickerDialog(
                    requireActivity(), dateSetListener,
                    crime.getDate().get(Calendar.YEAR),
                    crime.getDate().get(Calendar.MONTH),
                    crime.getDate().get(Calendar.DAY_OF_MONTH)
                )
                    .show()
            }

            crimeTime.setOnClickListener {
                TimePickerDialog(
                    requireActivity(), timeSetListener,
                    crime.getDate().get(Calendar.HOUR_OF_DAY),
                    crime.getDate().get(Calendar.MINUTE),
                    true
                )
                    .show()
            }

            crimeImageBtn.setOnClickListener {
                val i = Intent(requireActivity(), CrimeCameraActivity::class.java)
                i.putExtra("fileuri", crime.getPhoto()?.filename)
                cameraLauncher?.launch(i)
            }

            val pm = requireActivity().packageManager
            if (!pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
                binding.crimeImageBtn.isEnabled = false
            }

            crimeImage.setOnClickListener {
                val fm = requireActivity().supportFragmentManager
                val imageFragment = ImageFragment.newInstance(crime.getPhoto()?.filename.toString())
                imageFragment.show(fm, "imageFragment")
            }

            reportBtn.setOnClickListener {
                var i = Intent(Intent.ACTION_SEND)
                i.setType("text/plain")
                i.putExtra(Intent.EXTRA_TEXT, getCrimeReport())
                i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.crime_report_subject))
                i = Intent.createChooser(i, getString(R.string.send_report))
                startActivity(i)
            }

            suspectBtn.setOnClickListener {
                when {
                    ContextCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.READ_CONTACTS
                    ) == PackageManager.PERMISSION_GRANTED -> {
                        // Разрешение уже предоставлено, запускаем выбор контакта
                        pickContactLauncher.launch(null)
                    }

                    else -> {
                        // Запрашиваем разрешение
                        requestPermissionLauncher.launch(Manifest.permission.READ_CONTACTS)
                    }
                }
            }

        }
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        showPhoto()
    }

    private fun showPhoto() {
        val photo = crime.getPhoto()
        if (photo != null) {
            binding.crimeImage.setImageURI(Uri.parse(photo.filename))
        }
    }

    override fun onStart() {
        super.onStart()
        showPhoto()
    }

    override fun onPause() {
        super.onPause()
        CrimeLab.getInstance(requireContext()).saveCrimes()
    }

    private fun updateScreenData() {
        binding.crimeTitle.setText(crime.getTitle())
        binding.crimeDate.text = Utils.getStringDateOfCrime(crime)
        binding.crimeSolved.isChecked = crime.isSolved()
        binding.crimeTime.text = Utils.getStringTimeOfCrime(crime)
        if (crime.getSuspect() != null) {
            binding.suspectBtn.text = getString(R.string.crime_report_suspect, crime.getSuspect())
        } else {
            binding.suspectBtn.text = getString(R.string.suspect_text)
        }
        onDataChanged()
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

    private fun getCrimeReport(): String {
        val solvedString = if (crime.isSolved())
            getString(R.string.crime_report_solved)
        else
            getString(R.string.crime_report_unsolved)

        val dateFormat = "EEE, MMM dd"
        val dateString = DateFormat.format(dateFormat, crime.getDate()).toString()

        var suspect = crime.getSuspect()
        suspect = if (suspect == null)
            getString(R.string.crime_report_no_suspect)
        else
            getString(R.string.crime_report_suspect, crime.getSuspect())

        val report: String = getString(
            R.string.crime_report,
            crime.getTitle(),
            dateString,
            solvedString,
            suspect
        )
        return report
    }

    companion object {
        private lateinit var onDataChanged: () -> Unit

        fun newInstance(
            crimeId: String,
            onDataChanged: () -> Unit = {}
        ): CrimeDetailsFragment {
            this.onDataChanged = onDataChanged
            val args = Bundle().apply {
                putString(Constants.CRIMINAL_KEY, crimeId)
            }
            val fragment = CrimeDetailsFragment()
            fragment.arguments = args
            return fragment
        }
    }
}