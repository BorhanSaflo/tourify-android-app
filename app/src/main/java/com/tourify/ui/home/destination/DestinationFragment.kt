package com.tourify.ui.home.destination

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.tourify.ImageAdapter
import com.tourify.R
import com.tourify.databinding.FragmentDestinationBinding

class DestinationFragment : Fragment() {

    private var _binding: FragmentDestinationBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val destinationViewModel =
            ViewModelProvider(this)[DestinationViewModel::class.java]

        _binding = FragmentDestinationBinding.inflate(inflater, container, false)
        val root: View = binding.root


        // Temporary list of images
        val listOfImages = listOf(
            R.drawable.ic_home_black_24dp,
            R.drawable.ic_dashboard_black_24dp,
            R.drawable.ic_notifications_black_24dp
        )

        // Main image viewer
        val viewPager = binding.destinationViewPager
        val imageAdapter = ImageAdapter(listOfImages)
        viewPager.adapter = imageAdapter

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}