package com.tourify.ui.destination

import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.tourify.ImageAdapter
import com.tourify.ImageFetcher
import com.tourify.R
import com.tourify.models.Review
import com.tourify.utils.ApiResponse
import com.tourify.viewmodels.CoroutinesErrorHandler
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DestinationFragment : Fragment() {
    private val viewModel: DestinationViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_destination, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = requireArguments().getInt("id")
        val destinationDescription: TextView = view.findViewById(R.id.destination_description_text_view)
        val destinationLocation = view.findViewById<TextView>(R.id.destination_location_text_view)

        viewModel.destinationResponse.observe(viewLifecycleOwner) {
            when(it) {
                is ApiResponse.Success -> {
                    val destination = it.data
                    destinationLocation.text = destination.name + ", " + destination.country
                    destinationDescription.text = "Description should be here -> " + destination.description

                    fun displayImages(urls: List<String>){
                        val imageProgressBar = view.findViewById<View>(R.id.image_progress_bar)
                        imageProgressBar.visibility = View.VISIBLE

                        val images = mutableListOf<Bitmap>()
                        val imageFetchListener = ImageFetcher.ImageFetchListener { bitmap ->
                            if (bitmap == null) {
                                Toast.makeText(requireContext(), "Failed to load images", Toast.LENGTH_SHORT).show()
                                return@ImageFetchListener
                            }
                            images.add(bitmap)
                            if (images.size == urls.size) {
                                val destinationViewPager = view.findViewById<ViewPager2>(R.id.destination_view_pager)
                                destinationViewPager.adapter = ImageAdapter(images)
                                imageProgressBar.visibility = View.INVISIBLE
                            }
                        }
                        ImageFetcher.fetchImages(urls, imageFetchListener)
                    }

                    displayImages(destination.images)
                }
                else -> Toast.makeText(requireContext(), "Failed to load destination", Toast.LENGTH_SHORT).show()
            }

            // Reviews
            val reviewLayout: ViewGroup = view.findViewById(R.id.review_layout)
            when(it) {
                is ApiResponse.Success -> addReview(it.data.reviewsQuery, reviewLayout)
                else -> {}
            }
        }

        viewModel.getDestination(id, object: CoroutinesErrorHandler {
            override fun onError(message: String) {
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun addReview(reviews: List<Review>, layout: ViewGroup) {
        layout.removeAllViews()
        for (review in reviews) {
            val placeLayout = LayoutInflater.from(context).inflate(R.layout.item_review, layout, false)
            val imageView = placeLayout.findViewById<ImageView>(R.id.review_image)
            val nameView = placeLayout.findViewById<TextView>(R.id.review_name)
            val bodyView = placeLayout.findViewById<TextView>(R.id.review_body)
            val timestampView = placeLayout.findViewById<TextView>(R.id.review_timestamp)

            imageView.setImageResource(R.drawable.user)
            nameView.text = review.user.name
            bodyView.text = review.comment

            val relativeTime = review.timestamp?.let { viewModel.getRelativeTime(it) }
            timestampView.text = relativeTime

            layout.addView(placeLayout)
        }
    }
}