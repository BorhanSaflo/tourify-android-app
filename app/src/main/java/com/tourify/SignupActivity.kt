package com.tourify

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.tourify.models.Registration
import com.tourify.utils.ApiResponse
import com.tourify.viewmodels.AuthViewModel
import com.tourify.viewmodels.CoroutinesErrorHandler
import com.tourify.viewmodels.TokenViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignupActivity : AppCompatActivity() {

    private lateinit var closeButton: ImageButton
    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var signupButton: Button

    private val viewModel: AuthViewModel by viewModels()
    private val tokenViewModel: TokenViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        closeButton = findViewById(R.id.close_button)
        usernameEditText = findViewById(R.id.signup_username_edit_text)
        passwordEditText = findViewById(R.id.signup_password_edit_text)
        emailEditText = findViewById(R.id.signup_email_edit_text)
        signupButton = findViewById(R.id.signup_button)

        signupButton.setOnClickListener {
            val username = usernameEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()
            val email = emailEditText.text.toString().trim()

            if (validateCredentials(username, password, email)) {
                viewModel.register(
                    Registration(email, password, username),
                    object : CoroutinesErrorHandler {
                        override fun onError(message: String) {
                            Toast.makeText(this@SignupActivity, "Error! $message", Toast.LENGTH_SHORT).show()
                        }
                    }
                )
            }
        }

        closeButton.setOnClickListener {
            finish()
        }

        viewModel.loginResponse.observe(this) { response ->
            when(response) {
                is ApiResponse.Failure -> {
                    Toast.makeText(this, response.errorMessage, Toast.LENGTH_SHORT).show()
                }
                ApiResponse.Loading -> Log.w("SignupActivity", "Loading...")
                is ApiResponse.Success -> {
                    tokenViewModel.saveToken(response.data.token) // Save the token received from registration.
                    navigateToMainActivity() // Navigate to the Main Activity
                }
            }
        }
    }

    private fun validateCredentials(username: String, password: String, email: String): Boolean {
        if (username.isEmpty() || password.isEmpty() || email.isEmpty()) {
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

        return true
    }

    private fun navigateToMainActivity() {
        // Assuming you have a MainActivity that serves as the entry point after login/registration.
        startActivity(Intent(this, MainActivity::class.java))
        finish() // Finish SignupActivity to remove it from the activity stack.
    }
}
