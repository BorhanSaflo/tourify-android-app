package com.tourify.ui.home

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.tourify.R
import com.tourify.api.RetrofitClient
import com.tourify.ui.home.destination.DestinationFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.util.Log
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.tourify.ImageFetcher
import com.tourify.utils.ApiResponse
import com.tourify.viewmodels.CoroutinesErrorHandler
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Greeting text
        val textView: TextView = view.findViewById(R.id.greeting_text_view)
        viewModel.userInfoResponse.observe(viewLifecycleOwner) {
            val name = when(it) {
                is ApiResponse.Success -> it.data.userInfo.name
                else -> "User"
            }
            textView.text = "Hi, $name!"
        }

        viewModel.getUserInfo(object: CoroutinesErrorHandler {
            override fun onError(message: String) {
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            }
        })

        fun loadTrendingDestinations() {
            RetrofitClient.apiService.getTrendingDestinations().enqueue(object : Callback<List<Destination>> {
                override fun onResponse(call: Call<List<Destination>>, response: Response<List<Destination>>) {
                    if (response.isSuccessful)
                        response.body()?.let { places -> updateSection(places, view.findViewById(R.id.trending_linear_layout)) }
                }

                override fun onFailure(call: Call<List<Destination>>, t: Throwable) {
                    Log.d("HomeFragment", "Error fetching trending destinations: ${t.message}")
                }
            })
        }

        fun loadMostLikedDestinations() {
            RetrofitClient.apiService.getMostLikedDestinations().enqueue(object : Callback<List<Destination>> {
                override fun onResponse(call: Call<List<Destination>>, response: Response<List<Destination>>) {
                    if (response.isSuccessful)
                        response.body()?.let { places -> updateSection(places, view.findViewById(R.id.most_liked_linear_layout)) }
                }

                override fun onFailure(call: Call<List<Destination>>, t: Throwable) {
                    Log.d("HomeFragment", "Error fetching most liked destinations: ${t.message}")
                }
            })
        }

        // Load data for Trending and Most Liked sections
        loadTrendingDestinations()
        loadMostLikedDestinations()
    }

    private fun updateSection(destinations: List<Destination>, layout: ViewGroup) {
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
                .replace(R.id.home_frame_layout, fragment)
                .addToBackStack(null)
                .commit()
        }
    }
}