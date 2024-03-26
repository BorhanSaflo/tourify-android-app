package com.tourify.ui.user.settings.theme

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ThemeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is theme Fragment"
    }
    val text: LiveData<String> = _text
}