package com.tourify

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.tourify.models.Auth
import com.tourify.utils.ApiResponse
import com.tourify.viewmodels.AuthViewModel
import com.tourify.viewmodels.CoroutinesErrorHandler
import com.tourify.viewmodels.TokenViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var rememberMeCheckBox: CheckBox
    private lateinit var loginButton: Button
    private lateinit var signupTextView: TextView

    private val viewModel: AuthViewModel by viewModels()
    private val tokenViewModel: TokenViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize UI components
        emailEditText = findViewById(R.id.email_edit_text)
        passwordEditText = findViewById(R.id.password_edit_text)
        rememberMeCheckBox = findViewById(R.id.remember_me_check_box)
        loginButton = findViewById(R.id.login_button)
        signupTextView = findViewById(R.id.signup_text_view)

        // Load saved credentials if "Remember Me" is checked
        Log.w("Remember", FileHandler.readData("rememberMe", this))
        if (FileHandler.readData("rememberMe", this) == "true") {
            emailEditText.setText(FileHandler.readData("email", this))
            passwordEditText.setText(FileHandler.readData("password", this))
            rememberMeCheckBox.isChecked = true
        } else {
            emailEditText.setText("")
            passwordEditText.setText("")
            rememberMeCheckBox.isChecked = false
        }

        rememberMeCheckBox.setOnCheckedChangeListener { _, isChecked ->
            if (!isChecked) {
                FileHandler.writeData("email", "", this)
                FileHandler.writeData("password", "", this)
                FileHandler.writeData("rememberMe", "false", this)
            }
        }

        // Setup Login button click listener
        loginButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (validateCredentials(email, password)) {
                viewModel.login(
                    Auth(email, password),
                    object: CoroutinesErrorHandler {
                        override fun onError(message: String) {
                            Toast.makeText(this@LoginActivity, "Error! $message", Toast.LENGTH_SHORT).show()
                        }
                    }
                )
            }

            if (rememberMeCheckBox.isChecked) {
                FileHandler.writeData("email", email, this)
                FileHandler.writeData("password", password, this)
                FileHandler.writeData("rememberMe", "true", this)
            }
        }

        // Setup Sign Up TextView click listener
        signupTextView.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }


        viewModel.loginResponse.observe(this) {
            when(it) {
                is ApiResponse.Failure -> {
                    Toast.makeText(this, it.errorMessage, Toast.LENGTH_SHORT).show()
                }
                ApiResponse.Loading -> Log.w("LoginActivity", "Loading...")
                is ApiResponse.Success -> {
                    tokenViewModel.saveToken(it.data.token)
                }
            }
        }

        // Observe the token LiveData from tokenViewModel
        tokenViewModel.token.observe(this) { token ->
            if (!token.isNullOrEmpty()) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }
    }

    private fun validateCredentials(email: String, password: String): Boolean = email.isNotEmpty() && password.isNotEmpty()
}