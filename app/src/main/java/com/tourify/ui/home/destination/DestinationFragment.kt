package com.tourify.ui.home.destination

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tourify.ImageAdapter
import com.tourify.ImageFetcher
import com.tourify.api.RetrofitClient
import com.tourify.databinding.FragmentDestinationBinding
import com.tourify.ui.home.Destination
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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

        val id = requireArguments().getInt("id")
        loadDestinationData(id)

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
                // TODO: Show only one toast even when multiple images fail to load
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

    private fun loadDestinationData(id : Int) {
        RetrofitClient.apiService.getDestinationById(id).enqueue(object : Callback<Destination> {
            override fun onResponse(call: Call<Destination>, response: Response<Destination>) {
                if (response.isSuccessful) {
                    val destination = response.body()
                    if (destination != null) {
                        binding.destinationLocationTextView.text = buildString {
                            append(destination.name)
                            append(", ")
                            append(destination.country)
                        }
                        binding.destinationDescriptionTextView.text = destination.description
                        displayImages(destination.images)
                    } else {
                        Log.d("DestinationFragment", "Couldn't find destination with id $id")
                    }
                } else {
                    Log.d("DestinationFragment", "Failed to get destination with id $id: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<Destination>, t: Throwable) {
                Log.d("DestinationFragment", "Error fetching destination with id $id: ${t.message}")
            }
        })
    }
}