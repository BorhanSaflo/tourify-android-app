package com.tourify.ui.you

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
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

        val textView: TextView = binding.textYou
        notificationsViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}