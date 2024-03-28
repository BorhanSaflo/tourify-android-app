package com.tourify.ui.explore

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.tourify.R
import com.tourify.utils.ApiResponse
import com.tourify.viewmodels.CoroutinesErrorHandler
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExploreResultsFragment : Fragment() {
        private val viewModel: ExploreResultsViewModel by viewModels<ExploreResultsViewModel>()

        override fun onCreateView(
                inflater: LayoutInflater, container: ViewGroup?,
                savedInstanceState: Bundle?): View {
                return inflater.inflate(R.layout.fragment_explore_results, container, false)
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
                super.onViewCreated(view, savedInstanceState)

                viewModel.exploreResultsResponse.observe(viewLifecycleOwner) { apiResponse ->
                        when(apiResponse) {
                                is ApiResponse.Success -> {
                                        // Update UI with the results
                                }
                                is ApiResponse.Failure -> {
                                        Toast.makeText(requireContext(), "Failed to load search results", Toast.LENGTH_SHORT).show()
                                }
                                ApiResponse.Loading -> {}
                        }
                }
        }
}