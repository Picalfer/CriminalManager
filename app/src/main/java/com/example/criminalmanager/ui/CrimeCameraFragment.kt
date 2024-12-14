package com.example.criminalmanager.ui

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.drawable.Drawable
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

class CrimeCameraFragment : Fragment() {
    private lateinit var binding: FragmentCrimeCameraBinding
    private lateinit var captureIV: ImageView
    private lateinit var imageUrl: Uri

    private val contract = registerForActivityResult(ActivityResultContracts.TakePicture()) {
        captureIV.setImageURI(null)
        captureIV.setImageURI(imageUrl)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCrimeCameraBinding.inflate(inflater)
        imageUrl = createImageUri()

        with(binding) {
            captureIV = iv

            crimeCameraBackPictureBtn.setOnClickListener {
                requireActivity().finish()
            }

            crimeCameraTakePictureBtn.setOnClickListener {
                contract.launch(imageUrl)
            }

            crimeCameraSavePictureBtn.setOnClickListener {
                Log.d("TEST", imageUrl.toString())
                val i = Intent()
                i.putExtra("filepath", imageUrl.toString())
                requireActivity().setResult(RESULT_OK, i)
                requireActivity().finish()
            }
        }

        return binding.root
    }

    private fun createImageUri(): Uri {
        val image = File(requireActivity().filesDir, "camera_photos.png")
        return FileProvider.getUriForFile(
            requireActivity(),
            "com.example.criminalmanager.ui.FileProvider",
            image
        )
    }
}