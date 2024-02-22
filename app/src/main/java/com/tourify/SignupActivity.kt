package com.tourify

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class SignupActivity : AppCompatActivity(){

    private lateinit var closeButton: ImageButton
    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var phoneEditText: EditText
    private lateinit var signupButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        closeButton = findViewById(R.id.close_button)
        usernameEditText = findViewById(R.id.signup_username_edit_text)
        passwordEditText = findViewById(R.id.signup_password_edit_text)
        emailEditText = findViewById(R.id.signup_email_edit_text)
        phoneEditText = findViewById(R.id.signup_phone_edit_text)

        closeButton.setOnClickListener {
            finish()
        }
    }
}