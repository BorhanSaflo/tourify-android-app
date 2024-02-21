package com.tourify

import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class SignupActivity : AppCompatActivity(){

    private lateinit var closeButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        closeButton = findViewById(R.id.close_button)

        closeButton.setOnClickListener {
            finish()
        }
    }
}