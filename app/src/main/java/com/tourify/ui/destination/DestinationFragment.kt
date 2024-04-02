package com.tourify.ui.destination

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.res.ResourcesCompat
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

    private var rating: Int = 0
    private var isSaved: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_destination, container, false)
        val toolbar = view.findViewById<Toolbar>(R.id.toolbar)

        (activity as? AppCompatActivity)?.let { activity ->
            activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
            activity.supportActionBar?.setDisplayShowHomeEnabled(true)

            toolbar.setNavigationOnClickListener {
                activity.onBackPressed()
            }
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = requireArguments().getInt("id")
        val destinationDescription: TextView = view.findViewById(R.id.destination_description_text_view)
        val destinationCity = view.findViewById<TextView>(R.id.destination_city_text_view)
        val destinationCountry = view.findViewById<TextView>(R.id.destination_country_text_view)
        val likeView = view.findViewById<TextView>(R.id.like_text_view)
        val dislikeView = view.findViewById<TextView>(R.id.dislike_text_view)
        val viewsView = view.findViewById<TextView>(R.id.view_text_view)
        val savedView = view.findViewById<ImageView>(R.id.save_button)
        val likedView = view.findViewById<ImageView>(R.id.like_image_view)
        val dislikedView = view.findViewById<ImageView>(R.id.dislike_image_view)

        viewModel.destinationResponse.observe(viewLifecycleOwner) {
            when(it) {
                is ApiResponse.Success -> {
                    val destination = it.data
                    destinationCity.text = destination.name
                    destinationCountry.text = destination.country
                    destinationDescription.text = destination.description
                    likeView.text = destination.likes.toString()
                    dislikeView.text = destination.dislikes.toString()
                    viewsView.text = destination.views.toString()

                    isSaved = destination.isSaved
                    rating = if (destination.isLiked) 1 else if (destination.isDisliked) 2 else 0

                    if (isSaved)
                        savedView.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.bookmark_checked, null))
                    if (rating == 1)
                        likedView.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.thumbs_up_fill, null))
                    else if (rating == 2)
                        dislikedView.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.thumbs_down_fill, null))

                    fun displayImages(urls: List<String>){
                        val imageProgressBar = view.findViewById<View>(R.id.image_progress_bar)
                        imageProgressBar.visibility = View.VISIBLE

                        val images = mutableListOf<Bitmap>()
                        val imageFetchListener = ImageFetcher.ImageFetchListener { bitmap ->
                            if (bitmap == null) {
                                Log.e("DestinationFragment", "Failed to fetch image")
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
                else -> {}
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
                Log.e("DestinationFragment", message)
            }
        })

        viewModel.getSavedDestinations(object: CoroutinesErrorHandler {
            override fun onError(message: String) {
                Log.e("DestinationFragment", message)
            }
        })

        savedView.setOnClickListener {
            updateBookmark()

            viewModel.saveDestination(id, object: CoroutinesErrorHandler {
                override fun onError(message: String) {
                    Log.e("DestinationFragment", message)
                }
            })
        }

        likedView.setOnClickListener {
            updateLikeRating(if(rating == 1) 0 else 1)
        }

        dislikedView.setOnClickListener {
            updateLikeRating(if(rating == 2) 0 else 2)
        }
    }

    private fun updateBookmark() {
        isSaved = !isSaved
        val savedView = view?.findViewById<ImageView>(R.id.save_button)
        val newImage = if (isSaved) R.drawable.bookmark_checked else R.drawable.bookmark_unchecked
        savedView?.setImageDrawable(ResourcesCompat.getDrawable(resources, newImage, null))
    }

    private fun updateLikeRating(newVote: Int) {
        val likeImage = view?.findViewById<ImageView>(R.id.like_image_view)
        val likeText = view?.findViewById<TextView>(R.id.like_text_view)
        val dislikeImage = view?.findViewById<ImageView>(R.id.dislike_image_view)
        val dislikeText = view?.findViewById<TextView>(R.id.dislike_text_view)
        val id = requireArguments().getInt("id")

        when (newVote) {
            1 -> {
                viewModel.likeDestination(id, errorHandler())
                if (rating != 1) likeText?.increment()
                if (rating == 2) dislikeText?.decrement()
                likeImage?.setImageResource(R.drawable.thumbs_up_fill)
                dislikeImage?.setImageResource(R.drawable.thumbs_down)
            }
            2 -> {
                viewModel.dislikeDestination(id, errorHandler())
                if (rating == 1) likeText?.decrement()
                if (rating != 2) dislikeText?.increment()
                likeImage?.setImageResource(R.drawable.thumbs_up)
                dislikeImage?.setImageResource(R.drawable.thumbs_down_fill)
            }
            else -> {
                if (rating == 1) {
                    viewModel.likeDestination(id, errorHandler())
                    likeText?.decrement()
                }
                if (rating == 2) {
                    viewModel.dislikeDestination(id, errorHandler())
                    dislikeText?.decrement()
                }
                likeImage?.setImageResource(R.drawable.thumbs_up)
                dislikeImage?.setImageResource(R.drawable.thumbs_down)
            }
        }

        rating = newVote
    }

    private fun errorHandler() = object: CoroutinesErrorHandler {
        override fun onError(message: String) {
            Log.e("DestinationFragment", message)
        }
    }

    private fun TextView.increment() {
        this.text = (this.text.toString().toInt() + 1).toString()
    }

    private fun TextView.decrement() {
        this.text = (this.text.toString().toInt() - 1).toString()
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