package com.tourify.ui.home

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tourify.R
import com.tourify.api.RetrofitClient
import com.tourify.databinding.FragmentHomeBinding
import com.tourify.ui.home.destination.DestinationFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.util.Log
import android.widget.Toast
import com.tourify.ImageFetcher

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this)[HomeViewModel::class.java]

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Greeting text
        val textView: TextView = binding.greetingTextView
        textView.text = "Hi, %s!".format(activity?.intent?.getStringExtra("username"))

        // Load data for Trending and Near You sections
        loadTrendingDestinations()
        loadMostLikedDestinations()

        val destinationTestButton = binding.destinationTestButton
        destinationTestButton.setOnClickListener {
            val fragment = DestinationFragment()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.home_frame_layout, fragment)
                .addToBackStack(null)
                .commit()
        }

        return root
    }

    private fun loadTrendingDestinations() {
        RetrofitClient.apiService.getTrendingDestinations().enqueue(object : Callback<List<Destination>> {
            override fun onResponse(call: Call<List<Destination>>, response: Response<List<Destination>>) {
                if (response.isSuccessful) {
                    response.body()?.let { places ->
                        Log.d("HomeFragment", "Trending Destinations: $places")
                        updateTrendingSection(places)
                    } ?: Log.d("HomeFragment", "No Trending Destinations Found")
                } else {
                    Log.d("HomeFragment", "Failed to get trending destinations: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<List<Destination>>, t: Throwable) {
                Log.d("HomeFragment", "Error fetching trending destinations: ${t.message}")
            }
        })
    }

    private fun loadMostLikedDestinations() {
        RetrofitClient.apiService.getMostLikedDestinations().enqueue(object : Callback<List<Destination>> {
            override fun onResponse(call: Call<List<Destination>>, response: Response<List<Destination>>) {
                if (response.isSuccessful) {
                    response.body()?.let { places ->
                        Log.d("HomeFragment", "Most Liked Destinations: $places")
                        updateMostLikedSection(places)
                    } ?: Log.d("HomeFragment", "Couldn't find a list of most liked destinations")
                } else {
                    Log.d("HomeFragment", "Failed to get most liked destinations: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<List<Destination>>, t: Throwable) {
                Log.d("HomeFragment", "Error fetching most liked destinations: ${t.message}")
            }
        })
    }


    private fun updateTrendingSection(destinations: List<Destination>) {
        binding.trendingLinearLayout.removeAllViews()
        for (destination in destinations) {
            val placeLayout = LayoutInflater.from(context).inflate(R.layout.item_destination_card, binding.trendingLinearLayout, false)
            val imageView = placeLayout.findViewById<ImageView>(R.id.image_view_destination)
            val textView = placeLayout.findViewById<TextView>(R.id.text_view_destination_name)

            getImage(destination.thumbnail) { bitmap -> imageView.setImageBitmap(bitmap) }
            textView.text = destination.name

            binding.trendingLinearLayout.addView(placeLayout)
        }
    }

    private fun updateMostLikedSection(destinations: List<Destination>) {
        binding.mostLikedLinearLayout.removeAllViews()
        for (destination in destinations) {
            val placeLayout = LayoutInflater.from(context).inflate(R.layout.item_destination_card, binding.mostLikedLinearLayout, false)
            val imageView = placeLayout.findViewById<ImageView>(R.id.image_view_destination)
            val textView = placeLayout.findViewById<TextView>(R.id.text_view_destination_name)

            // Replace link with `destination.imageUrl` once the database is updated
            getImage(destination.thumbnail) { bitmap -> imageView.setImageBitmap(bitmap) }
            textView.text = destination.name

            binding.mostLikedLinearLayout.addView(placeLayout)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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

}
