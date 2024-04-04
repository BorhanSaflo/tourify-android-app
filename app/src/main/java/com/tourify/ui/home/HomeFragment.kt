package com.tourify.ui.home

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.tourify.R
import com.tourify.ui.destination.DestinationFragment
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.tourify.ImageFetcher
import com.tourify.models.DestinationResult
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
            val time = java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY)
            when (time) {
                in 0..5 -> textView.text = "Good evening, $name"
                in 6..11 -> textView.text = "Good morning, $name"
                in 12..16 -> textView.text = "Good afternoon, $name"
                else -> textView.text = "Good evening, $name"
            }
        }

        viewModel.getUserInfo(object: CoroutinesErrorHandler {
            override fun onError(message: String) {
                Log.e("HomeFragment", message)
            }
        })

        // Trending destinations
        val trendingLayout: ViewGroup = view.findViewById(R.id.trending_linear_layout)
        viewModel.trendingDestinationsResponse.observe(viewLifecycleOwner) {
            when(it) {
                is ApiResponse.Success -> updateSection(it.data, trendingLayout)
                is ApiResponse.Failure -> Log.e("HomeFragment", "Failed to load trending destinations")
                ApiResponse.Loading -> {}
            }
        }

        viewModel.getTrendingDestinations(object: CoroutinesErrorHandler {
            override fun onError(message: String) {
                Log.e("HomeFragment", message)
            }
        })

        // Most liked destinations
        val mostLikedLayout: ViewGroup = view.findViewById(R.id.most_liked_linear_layout)
        viewModel.mostLikedDestinationsResponse.observe(viewLifecycleOwner) {
            when(it) {
                is ApiResponse.Success -> updateSection(it.data, mostLikedLayout)
                is ApiResponse.Failure -> Log.e("HomeFragment", "Failed to load most liked destinations")
                ApiResponse.Loading -> {}
            }
        }

        viewModel.getMostLikedDestinations(object: CoroutinesErrorHandler {
            override fun onError(message: String) {
                Log.e("HomeFragment", message)
            }
        })

        val mostViewedLayout: ViewGroup = view.findViewById(R.id.most_viewed_linear_layout)
        viewModel.mostViewedDestinationsResponse.observe(viewLifecycleOwner) {
            when(it) {
                is ApiResponse.Success -> updateSection(it.data, mostViewedLayout)
                is ApiResponse.Failure -> Log.e("HomeFragment", "Failed to load most viewed destinations")
                ApiResponse.Loading -> {}
            }
        }

        viewModel.getMostViewedDestinations(object: CoroutinesErrorHandler {
            override fun onError(message: String) {
                Log.e("HomeFragment", message)
            }
        })

        val featuredLayout: ViewGroup = view.findViewById(R.id.featured_destination_linear_layout)
        viewModel.featuredDestinationResponse.observe(viewLifecycleOwner) {
            when(it) {
                is ApiResponse.Success -> updateFeaturedDestination(it.data, featuredLayout)
                is ApiResponse.Failure -> Log.e("HomeFragment", "Failed to load featured destination")
                ApiResponse.Loading -> {}
            }
        }

        viewModel.getFeaturedDestinations(object: CoroutinesErrorHandler {
            override fun onError(message: String) {
                Log.e("HomeFragment", message)
            }
        })
    }

    private fun updateFeaturedDestination(destination: DestinationResult, layout: ViewGroup) {
        layout.removeAllViews()
        val placeLayout = LayoutInflater.from(context).inflate(R.layout.item_featured_destination_card, layout, false)
        val imageView = placeLayout.findViewById<ImageView>(R.id.image_view_destination)
        val textView = placeLayout.findViewById<TextView>(R.id.text_view_destination_name)
        val loadingIcon = placeLayout.findViewById<ProgressBar
        >(R.id.progress_bar_destination)

        textView.text = destination.name + ", " + destination.country
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

            findNavController().navigate(R.id.action_navigation_home_to_navigation_destination, args)
        }
    }
}