package com.tourify.ui.explore

import android.os.Bundle
import android.util.Log
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
class ExploreFragment : Fragment() {
    private val viewModel: ExploreViewModel by viewModels()
    private val currentSelectionsMap = mutableMapOf<String, String>()
    private val selections = mutableListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_explore, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupQuestionView(view, R.id.questionView1, "What type of climate do you prefer for your vacation?", listOf("Tropical", "Warm", "Cold"))
        setupQuestionView(view, R.id.questionView2, "What kind of activities do you enjoy?", listOf("Shopping", "Sports", "Dinning"))
        setupQuestionView(view, R.id.questionView3, "What is your budget for a vacation?", listOf("Low", "Medium", "High"))
        setupQuestionView(view, R.id.questionView4, "What kind of transportation do you prefer?", listOf("Car", "Bus", "Train"))
        setupQuestionView(view, R.id.questionView5, "What kind of attractions do you enjoy?", listOf("Museums", "Parks", "Beaches"))
    }

    private fun setupQuestionView(view: View, questionViewId: Int, question: String, options: List<String>) {
        val questionView = view.findViewById<QuestionView>(questionViewId)
        questionView.setQuestion(question)
        questionView.setOptions(options)
        questionView.setOptionClickListeners { index ->
            val button = questionView.optionButtons[index]
            val selectedOptionText = button.text.toString()

            currentSelectionsMap[question] = selectedOptionText
            updateSelectionsArray()

            if (currentSelectionsMap.size == 5) {
                //call api with selections
                exploreDestinations(selections.joinToString())
            }

            //Log the current selections as string of all of them with a comma separated string
            Log.d("ExploreFragment", selections.joinToString())
        }
    }

    private fun updateSelectionsArray() {
        selections.clear()
        selections.addAll(currentSelectionsMap.values)
    }

    private fun exploreDestinations(query: String) {
        viewModel.exploreDestinations(query, object: CoroutinesErrorHandler {
            override fun onError(message: String) {
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            }
        })

        viewModel.exploreDestinationsResponse.observe(viewLifecycleOwner) { apiResponse ->
            when(apiResponse) {
                is ApiResponse.Success -> {
                    Log.d("ExploreFragment", "Search results: ${apiResponse.data}")
                }
                is ApiResponse.Failure -> {
                    Toast.makeText(requireContext(), "Failed to load search results", Toast.LENGTH_SHORT).show()
                }
                ApiResponse.Loading -> {}
            }
        }
    }
}
