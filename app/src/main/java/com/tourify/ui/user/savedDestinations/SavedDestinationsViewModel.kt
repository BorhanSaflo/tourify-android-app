package com.tourify.ui.user.savedDestinations

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SavedDestinationsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is saved destinations Fragment"
    }
    val text: LiveData<String> = _text
}