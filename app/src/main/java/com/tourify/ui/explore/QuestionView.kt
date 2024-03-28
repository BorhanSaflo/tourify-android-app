package com.tourify.ui.explore

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import com.tourify.R

class QuestionView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var questionTextView: TextView
    var optionButtons: List<Button>

    init {
        orientation = VERTICAL
        LayoutInflater.from(context).inflate(R.layout.layout_question, this, true)

        questionTextView = findViewById(R.id.question_text_view)
        optionButtons = listOf(
            findViewById(R.id.option_button_1),
            findViewById(R.id.option_button_2),
            findViewById(R.id.option_button_3)
        )
    }

    fun setQuestion(question: String) {
        questionTextView.text = question
    }

    fun setOptions(options: List<String>) {
        options.forEachIndexed { index, option ->
            if (index < optionButtons.size) {
                optionButtons[index].text = option
            }
        }
    }

    fun setOptionClickListeners(listener: (Int) -> Unit) {
        optionButtons.forEachIndexed { index, button ->
            button.setOnClickListener {
                // Call the listener to notify the selected index
                listener(index)

                // Update button backgrounds based on selection
                updateButtonBackgrounds(index)
            }
        }
    }

    private fun updateButtonBackgrounds(selectedIndex: Int) {
        // Iterate through all buttons and update their selected state
        optionButtons.forEachIndexed { index, button ->
            button.isSelected = index == selectedIndex
        }
    }
}