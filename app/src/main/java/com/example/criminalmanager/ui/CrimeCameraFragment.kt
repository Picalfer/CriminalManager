package com.example.criminalmanager.ui

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.example.criminalmanager.databinding.FragmentCrimeCameraBinding
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CrimeCameraFragment : Fragment() {
    private lateinit var binding: FragmentCrimeCameraBinding
    private lateinit var captureIV: ImageView
    private lateinit var imageUri: Uri

    private val contract = registerForActivityResult(ActivityResultContracts.TakePicture()) {
        captureIV.setImageURI(null)
        captureIV.setImageURI(imageUri)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCrimeCameraBinding.inflate(inflater)
        imageUri = createImageUri()

        with(binding) {
            captureIV = iv

            val uriFromIntent = requireActivity().intent?.getStringExtra("fileuri")
            if (uriFromIntent != null) {
                val imageUri = Uri.parse(uriFromIntent)
                iv.setImageURI(imageUri)
            }

            crimeCameraBackPictureBtn.setOnClickListener {
                requireActivity().finish()
            }

            crimeCameraTakePictureBtn.setOnClickListener {
                contract.launch(imageUri)
            }

            crimeCameraSavePictureBtn.setOnClickListener {
                Log.d("TEST", imageUri.toString())
                val i = Intent()
                i.putExtra("filepath", imageUri.toString())
                requireActivity().setResult(RESULT_OK, i)
                requireActivity().finish()
            }
        }

        return binding.root
    }

    private fun createImageUri(): Uri {
        // Генерация уникального имени файла
        val timeStamp: String =
            SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val imageFileName = "camera_photo_$timeStamp.png" // Уникальное имя файла
        val image =
            File(requireActivity().filesDir, imageFileName) // Создание файла с уникальным именем

        return FileProvider.getUriForFile(
            requireActivity(),
            "com.example.criminalmanager.ui.FileProvider",
            image
        )
    }
}