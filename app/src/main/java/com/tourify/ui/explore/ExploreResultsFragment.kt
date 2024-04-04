package com.tourify.ui.explore

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.tourify.ImageFetcher
import com.tourify.R
import com.tourify.models.DestinationResult
import com.tourify.ui.destination.DestinationFragment
import com.tourify.utils.ApiResponse
import com.tourify.viewmodels.CoroutinesErrorHandler
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExploreResultsFragment : Fragment() {
        private val viewModel: ExploreResultsViewModel by viewModels<ExploreResultsViewModel>()
        private lateinit var selectedOptions: String

        override fun onCreateView(
                inflater: LayoutInflater, container: ViewGroup?,
                savedInstanceState: Bundle?): View {
                return inflater.inflate(R.layout.fragment_explore_results, container, false)
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
                super.onViewCreated(view, savedInstanceState)
                selectedOptions = arguments?.getString("selectedOptions") ?: ""

                fun updateExploreResults(results: List<DestinationResult>) {
                        val resultsLayout: ViewGroup = view.findViewById(R.id.explore_results_linear_layout)
                        resultsLayout.removeAllViews()
                        results.forEach { destination ->
                                val resultLayout = LayoutInflater.from(context).inflate(R.layout.item_search_destination_card, resultsLayout, false)
                                val imageView = resultLayout.findViewById<ImageView>(R.id.image_view_destination)
                                val cityView = resultLayout.findViewById<TextView>(R.id.text_view_destination_name)
                                val countryView = resultLayout.findViewById<TextView>(R.id.text_view_destination_country)
                                val descriptionView = resultLayout.findViewById<TextView>(R.id.text_view_destination_description)
                                val loadingIcon = resultLayout.findViewById<ProgressBar>(R.id.progress_bar_destination)

                                cityView.text = destination.name
                                countryView.text = buildString {
                                        append(", ")
                                        append(destination.country)
                                }
                                descriptionView.text = destination.description
                                loadingIcon.visibility = View.VISIBLE
                                imageView.visibility = View.GONE
                                getImage(destination.thumbnail) { bitmap ->
                                        activity?.runOnUiThread {
                                                if (bitmap != null) {
                                                        imageView.setImageBitmap(bitmap)
                                                        loadingIcon.visibility = View.GONE
                                                        imageView.visibility = View.VISIBLE
                                                }
                                        }
                                }
                                setupCardClickListener(resultLayout, destination.id.toInt())
                                resultsLayout.addView(resultLayout)
                        }
                }

                Log.d("ExploreResultsFragment", "Selected options: $selectedOptions")
                viewModel.exploreDestinations(selectedOptions, object: CoroutinesErrorHandler {
                        override fun onError(message: String) {
                                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                        }
                })

                viewModel.exploreDestinationsResponse.observe(viewLifecycleOwner) { apiResponse ->
                        when(apiResponse) {
                                is ApiResponse.Success -> updateExploreResults(apiResponse.data)
                                is ApiResponse.Failure -> {
                                        Toast.makeText(requireContext(), "Failed to load search results", Toast.LENGTH_SHORT).show()
                                }
                                ApiResponse.Loading -> {}
                        }
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

        private fun setupCardClickListener(cardView: View, id: Int) {
                cardView.setOnClickListener {
                        val args = Bundle().apply {
                                putInt("id", id)
                        }
                        val fragment = DestinationFragment().apply {
                                arguments = args
                        }
                        findNavController().navigate(R.id.action_navigation_explore_results_to_navigation_destination, args)

                        hideKeyboard()
                }
        }

        private fun hideKeyboard() {
                val inputMethodManager = view?.context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(requireView().windowToken, 0)
        }
}