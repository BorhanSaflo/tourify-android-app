package com.tourify.ui.search

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.tourify.ImageFetcher
import com.tourify.R
import com.tourify.models.DestinationResult
import com.tourify.ui.destination.DestinationFragment
import com.tourify.utils.ApiResponse
import com.tourify.viewmodels.CoroutinesErrorHandler
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment() {
    private val viewModel: SearchViewModel by viewModels()
    private val searchHandler = Handler(Looper.getMainLooper())
    private var searchRunnable: Runnable? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val searchText: EditText = view.findViewById(androidx.appcompat.R.id.search_src_text)
        searchText.setTextColor(Color.WHITE)
        searchText.setHintTextColor(Color.WHITE)
        val searchCloseButton: ImageView = view.findViewById(androidx.appcompat.R.id.search_close_btn)
        searchCloseButton.setColorFilter(Color.WHITE)

        @SuppressLint("MissingInflatedId")
        fun updateSearchResults(results: List<DestinationResult>) {
            val resultsLayout: ViewGroup = view.findViewById(R.id.results_linear_layout)
            resultsLayout.removeAllViews()
            results.forEach { destination ->
                val resultLayout = LayoutInflater.from(context).inflate(R.layout.item_search_destination_card, resultsLayout, false)
                val imageView = resultLayout.findViewById<ImageView>(R.id.image_view_destination)
                val cityView = resultLayout.findViewById<TextView>(R.id.text_view_destination_name)
                val countryView = resultLayout.findViewById<TextView>(R.id.text_view_destination_country)
                val descriptionView = resultLayout.findViewById<TextView>(R.id.text_view_destination_description)
                val loadingIcon = resultLayout.findViewById<ProgressBar>(R.id.progress_bar_destination)

                // Setting the text views with destination data
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

        fun triggerSearch(query: String) {
            if (query.isEmpty()) {
                return
            }

            viewModel.searchDestinations(query, object : CoroutinesErrorHandler {
                override fun onError(message: String) {
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                }
            })

            viewModel.searchResultsResponse.observe(viewLifecycleOwner) { apiResponse ->
                when(apiResponse) {
                    is ApiResponse.Success -> {
                        updateSearchResults(apiResponse.data)
                    }
                    is ApiResponse.Failure -> {
                        Toast.makeText(requireContext(), "Failed to load search results", Toast.LENGTH_SHORT).show()
                    }
                    ApiResponse.Loading -> {}
                }
            }
        }

        val searchEditText: SearchView = view.findViewById(R.id.search_view)
        searchEditText.setIconifiedByDefault(false)
        searchEditText.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    triggerSearch(it)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                searchRunnable?.let { searchHandler.removeCallbacks(it) }
                searchRunnable = Runnable {
                    newText?.let {
                        triggerSearch(it)
                    }
                }.also {
                    searchHandler.postDelayed(it, 1000)
                }
                return false
            }
        })
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
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.search_frame_layout, fragment)
                .addToBackStack(null)
                .commit()

            hideKeyboard()
        }
    }


    private fun hideKeyboard() {
        val inputMethodManager = view?.context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(requireView().windowToken, 0)
    }
}
