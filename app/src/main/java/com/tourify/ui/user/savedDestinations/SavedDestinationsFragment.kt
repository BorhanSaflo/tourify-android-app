package com.tourify.ui.user.savedDestinations

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.tourify.ImageFetcher
import com.tourify.R
import com.tourify.databinding.FragmentSavedDestinationsBinding
import com.tourify.models.Destination
import com.tourify.models.DestinationResult
import com.tourify.ui.destination.DestinationFragment
import com.tourify.ui.home.HomeViewModel
import com.tourify.utils.ApiResponse
import com.tourify.viewmodels.CoroutinesErrorHandler
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SavedDestinationsFragment : Fragment() {
    private val viewModel: SavedDestinationsViewModel by viewModels()

    private var _binding: FragmentSavedDestinationsBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        getSavedDestinations()

    }

//    private fun getSavedDestinations() {
//        viewModel.getSavedDestinations(CoroutinesErrorHandler {
//            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
//        }).observe(viewLifecycleOwner) { response ->
//            when (response) {
//                is ApiResponse.Success -> {
//                    updateUI(response.data)
//                }
//                is ApiResponse.Error -> {
//                    Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()
//                }
//            }
//        }
//    }

    private fun updateSection(destinations: List<DestinationResult>, layout: ViewGroup) {
        layout.removeAllViews()
        for (destination in destinations) {
            val placeLayout = LayoutInflater.from(context).inflate(R.layout.item_destination_card, layout, false)
            val imageView = placeLayout.findViewById<ImageView>(R.id.image_view_destination)
            val textView = placeLayout.findViewById<TextView>(R.id.text_view_destination_name)
            val loadingIcon = placeLayout.findViewById<ProgressBar>(R.id.progress_bar_destination)

            textView.text = destination.name

            loadingIcon.visibility = View.VISIBLE
            imageView.visibility = View.GONE

            getImage(destination.thumbnail) { bitmap ->
                imageView.setImageBitmap(bitmap)
                setupImageClickListener(imageView, destination.id.toInt())
                loadingIcon.visibility = View.GONE
                imageView.visibility = View.VISIBLE
                imageView.layoutParams.width = layout.width / 3 - placeLayout.paddingLeft * 3
            }

            layout.addView(placeLayout)
        }
    }

    private fun getImage(url: String, onImageFetched: (Bitmap?) -> Unit) {
        ImageFetcher.fetchImage(url) { bitmap ->
            activity?.runOnUiThread {
                if (bitmap == null) {
                    Toast.makeText(requireContext(), "Failed to load image", Toast.LENGTH_SHORT).show()
                    onImageFetched(null)
                } else {
                    onImageFetched(bitmap)
                }
            }
        }
    }

    private fun setupImageClickListener(imageView: ImageView, id : Int) {
        imageView.setOnClickListener {
            val args = Bundle()
            args.putInt("id", id)

            val fragment = DestinationFragment()
            fragment.arguments = args

            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.saved_destinations_frame_layout, fragment)
                .addToBackStack(null)
                .commit()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}