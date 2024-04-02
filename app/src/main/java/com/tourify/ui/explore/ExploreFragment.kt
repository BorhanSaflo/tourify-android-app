package com.tourify.ui.explore

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.tourify.R
import com.tourify.utils.ApiResponse
import com.tourify.viewmodels.CoroutinesErrorHandler
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExploreFragment : Fragment() {
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
                val bundle = Bundle().apply {
                    putString("selectedOptions", selections.joinToString())
                }
                findNavController().navigate(R.id.action_navigation_explore_to_navigation_explore_results, bundle)
            }
        }
    }

    private fun updateSelectionsArray() {
        selections.clear()
        selections.addAll(currentSelectionsMap.values)
    }
}