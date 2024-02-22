package com.tourify.ui.you

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tourify.LoginActivity
import com.tourify.databinding.FragmentYouBinding
import com.tourify.ui.you.YouViewModel

class YouFragment : Fragment() {

    private var _binding: FragmentYouBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val notificationsViewModel =
            ViewModelProvider(this).get(YouViewModel::class.java)

        _binding = FragmentYouBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Sign out and return to login menu if clicked
        val signOutButton: Button = binding.youSignOutButton
        signOutButton.setOnClickListener {
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

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}