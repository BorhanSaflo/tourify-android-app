package com.tourify.ui.home.destination

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tourify.ImageAdapter
import com.tourify.ImageFetcher
import com.tourify.databinding.FragmentDestinationBinding

class DestinationFragment : Fragment() {

    private var _binding: FragmentDestinationBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val destinationViewModel =
            ViewModelProvider(this)[DestinationViewModel::class.java]

        _binding = FragmentDestinationBinding.inflate(inflater, container, false)
        val root: View = binding.root


        // Temporary list of image URLs
        // Note: Images are loaded asynchronously meaning they may not display in this order
        val urls = listOf(
            "https://www.gstatic.com/webp/gallery/1.jpg",
            "https://www.gstatic.com/webp/gallery/2.jpg",
            "https://www.gstatic.com/webp/gallery/3.jpg",
            "https://www.gstatic.com/webp/gallery/4.jpg",
            "https://www.gstatic.com/webp/gallery/5.jpg"
        )
        displayImages(urls)


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun displayImages(urls: List<String>){
        binding.imageProgressBar.visibility = View.VISIBLE

        val images = mutableListOf<Bitmap>()
        val imageFetchListener = ImageFetcher.ImageFetchListener { bitmap ->
            if (bitmap == null) {
                // The following toast will popup for *every* image that fails to load
                // TODO: Show only one toast even if multiple images fail to load
                Toast.makeText(requireContext(), "Failed to load images", Toast.LENGTH_SHORT).show()
                return@ImageFetchListener
            }
            images.add(bitmap)
            if (images.size == urls.size) {
                binding.destinationViewPager.adapter = ImageAdapter(images)
                binding.imageProgressBar.visibility = View.INVISIBLE
            }
        }
        ImageFetcher.fetchImages(urls, imageFetchListener)
    }
}