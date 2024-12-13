package com.example.criminalmanager.ui

import android.net.Uri
import android.os.Bundle
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCrimeCameraBinding.inflate(inflater)
        imageUrl = createImageUri()

        with(binding) {
            captureIV = iv

            crimeCameraTakePictureBtn.setOnClickListener {
                contract.launch(imageUrl)
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

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CrimeCameraFragment()
    }
}