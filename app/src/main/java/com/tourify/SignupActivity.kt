package com.tourify

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.tourify.api.RetrofitClient

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
        signupButton = findViewById(R.id.signup_button)


        signupButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()
            val email = emailEditText.text.toString()
            val phone = phoneEditText.text.toString()

            if(validateCredentials(username, password, email, phone)) {
                createAccount(username, password, email, phone)
                finish()
            }
        }


        closeButton.setOnClickListener {
            finish()
        }
    }

    private fun validateCredentials(username: String, password: String, email: String, phone: String): Boolean {
        if (username.isEmpty() || password.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "Please fill in all the fields", Toast.LENGTH_SHORT).show()
            return false
        }

        if (username.length < 3) {
            Toast.makeText(this, "Username must be at least 3 characters long", Toast.LENGTH_SHORT).show()
            return false
        }

        if (password.length < 8) {
            Toast.makeText(this, "Password must be at least 8 characters long", Toast.LENGTH_SHORT).show()
            return false
        }

        if (!email.contains("@")) {
            Toast.makeText(this, "Invalid email", Toast.LENGTH_SHORT).show()
            return false
        }

        if (phone.length != 10) {
            Toast.makeText(this, "Invalid phone number", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun createAccount(username: String, password: String, email: String, phone: String) {
        RetrofitClient.apiService.registerUser(username, email, password)
    }
}