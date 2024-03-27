package com.tourify.ui.user.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.tourify.R
import com.tourify.databinding.FragmentProfileBinding
import com.tourify.utils.ApiResponse
import com.tourify.viewmodels.CoroutinesErrorHandler
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private val viewModel: ProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val emailTextView: TextView = view.findViewById(R.id.user_info_email)
        val usernameTextView: TextView = view.findViewById(R.id.user_info_username)

        viewModel.userInfoResponse.observe(viewLifecycleOwner) {
            val profileInfo = when (it) {
                is ApiResponse.Success -> it.data.userInfo
                else -> null
            }

            if (profileInfo != null) {
                emailTextView.text = "Email:    " + profileInfo.email
                usernameTextView.text = "Name:    " + profileInfo.name
            }
        }

        viewModel.getUserInfo(object: CoroutinesErrorHandler {
            override fun onError(message: String) {
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            }
        })
    }
}