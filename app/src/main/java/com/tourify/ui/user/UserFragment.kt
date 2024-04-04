package com.tourify.ui.user

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.tourify.LoginActivity
import com.tourify.R
import com.tourify.databinding.FragmentUserBinding
import com.tourify.ui.user.profile.ProfileFragment
import com.tourify.ui.user.savedDestinations.SavedDestinationsFragment
import com.tourify.ui.user.settings.SettingsFragment
import com.tourify.viewmodels.TokenViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserFragment : Fragment() {
    private val tokenViewModel: TokenViewModel by activityViewModels()

    private var _binding: FragmentUserBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val userViewModel =
            ViewModelProvider(this).get(UserViewModel::class.java)

        _binding = FragmentUserBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Sign out and return to login menu if clicked
        val signOutButton: Button = binding.userSignOutButton
        signOutButton.setOnClickListener {
            signoutSequence()
        }

        // Profile button
        val profileButton: Button = binding.userProfileButton
        profileButton.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_you_to_navigation_profile)
        }

        // Settings button
        val settingsButton: Button = binding.userSettingsButton
        settingsButton.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_you_to_navigation_settings2)
        }

        // Saved destinations button
        val savedDestinationsButton: Button = binding.userSavedDestinationsButton
        savedDestinationsButton.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_you_to_navigation_savedDestinations2)
        }

        // Liked destinations button
        val likedDestinationsButton: Button = binding.userLikedDestinationsButton
        likedDestinationsButton.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_you_to_navigation_likedDestinations)
        }

        // Disliked destinations button
        val dislikedDestinationsButton: Button = binding.userDislikedDestinationsButton
        dislikedDestinationsButton.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_you_to_navigation_dislikedDestinations)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun signoutSequence() {
        val builder = AlertDialog.Builder(requireContext())

        builder.apply {
            setMessage("Are you sure you want to sign out?")
            setPositiveButton("Yes") { dialog, id ->
                // before
                Log.w("Signout", "Before: ${tokenViewModel.token.value}")
                tokenViewModel.deleteToken()
                // after
                Log.w("Signout", "After: ${tokenViewModel.token.value}")
                startActivity(Intent(requireContext(), LoginActivity::class.java))
            }
            setNegativeButton("No") { dialog, id ->
                Log.w("Signout", "Token: ${tokenViewModel.token.value}")
                dialog.dismiss()
            }
        }

        builder.create().show()
    }
}