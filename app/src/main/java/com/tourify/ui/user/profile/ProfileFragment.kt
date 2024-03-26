package com.tourify.ui.user.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.tourify.databinding.FragmentProfileBinding
import com.tourify.api.RetrofitClient
import com.tourify.models.UserInfoResponse
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var emailTextView: TextView
    private lateinit var usernameTextView: TextView
    private lateinit var passwordTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val profileViewModel =
            ViewModelProvider(this)[ProfileViewModel::class.java]

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        emailTextView = binding.userInfoEmail
        usernameTextView = binding.userInfoUsername
        passwordTextView = binding.userInfoPassword

        return root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            val response = RetrofitClient.apiService.getUserInfo()
            if(response.isSuccessful && response.body() != null) {
                val userInfo = response.body()!!
                emailTextView.text = userInfo.userInfo.email
                usernameTextView.text = userInfo.userInfo.name
            } else {
                Log.d("ProfileFragment", "Failed to get user info")
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}