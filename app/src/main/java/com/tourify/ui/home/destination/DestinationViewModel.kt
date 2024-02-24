package com.tourify.ui.home.destination

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DestinationViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is destination Fragment"
    }
    val text: LiveData<String> = _text
}