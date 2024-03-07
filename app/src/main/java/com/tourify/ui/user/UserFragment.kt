package com.tourify.ui.user

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tourify.LoginActivity
import com.tourify.R
import com.tourify.databinding.FragmentUserBinding
import com.tourify.ui.user.savedDestinations.SavedDestinationsFragment

class UserFragment : Fragment() {

    private var _binding: FragmentUserBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val notificationsViewModel =
            ViewModelProvider(this).get(UserViewModel::class.java)

        _binding = FragmentUserBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Sign out and return to login menu if clicked
        val signOutButton: Button = binding.userSignOutButton
        signOutButton.setOnClickListener {
            signoutSequence()
        }

        // TODO: Profile button

        // Saved destinations button
        val savedDestinationsButton: Button = binding.userSavedDestinationsButton
        savedDestinationsButton.setOnClickListener {
            changeFragment(SavedDestinationsFragment())
        }

        // TODO: Settings button

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
                val intent = Intent(activity, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
            setNegativeButton("No") { dialog, id ->
                dialog.dismiss()
            }
        }

        builder.create().show()
    }

    private fun changeFragment(newFragment: Fragment) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.user_frame_layout, newFragment)
            .addToBackStack(null)
            .commit()
    }
}