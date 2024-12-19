package com.example.criminalmanager.ui

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.DialogFragment

class ImageFragment : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val imageUri = arguments?.getString("imagePath")
        val image = ImageView(requireActivity())
        image.setImageURI(Uri.parse(imageUri.toString()))

        return image
    }

    companion object {
        @JvmStatic
        fun newInstance(imagePath: String) = ImageFragment().also {
            it.arguments = Bundle().also { args ->
                args.putString("imagePath", imagePath)
            }
            it.setStyle(STYLE_NO_TITLE, 0)
        }
    }
}