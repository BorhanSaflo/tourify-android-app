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

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val viewModel: ProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val emailTextView: TextView = binding.userInfoEmail
        val usernameTextView: TextView = binding.userInfoUsername

        viewModel.userInfoResponse.observe(viewLifecycleOwner) {
            val profileInfo = when (it) {
                is ApiResponse.Success -> it.data.userInfo
                else -> null
            }

            if (profileInfo != null) {
                emailTextView.text = profileInfo.email
                usernameTextView.text = profileInfo.name
            }
        }

        viewModel.getUserInfo(object: CoroutinesErrorHandler {
            override fun onError(message: String) {
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}