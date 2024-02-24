package com.tourify.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tourify.R
import com.tourify.databinding.FragmentHomeBinding
import com.tourify.ui.home.destination.DestinationFragment

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this)[HomeViewModel::class.java]

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Greeting text
        val textView: TextView = binding.greetingTextView
        textView.text = "Hi, %s!".format(activity?.intent?.getStringExtra("username"))

        // Temporary image list
        val imgList = mutableListOf<Int>()
        imgList.add(R.drawable.ic_dashboard_black_24dp)
        imgList.add(R.drawable.ic_home_black_24dp)
        imgList.add(R.drawable.ic_notifications_black_24dp)

        // Trending scroll view
        val trendingLinearLayout = binding.trendingLinearLayout
        for(i in 1..10) {
            trendingLinearLayout.addView(ImageView(context).apply {
                setImageResource(imgList[i%3])
                layoutParams = ViewGroup.LayoutParams(384, 384)
                val marginParams = ViewGroup.MarginLayoutParams(384, 384)
                marginParams.setMargins(10, 10, 10, 10)
                layoutParams = marginParams
            })
        }

        // Near You scroll view
        val forYouLinearLayout = binding.nearYouLinearLayout
        for(i in 1..10) {
            forYouLinearLayout.addView(ImageView(context).apply {
                setImageResource(imgList[i%3])
                layoutParams = ViewGroup.LayoutParams(384, 384)
                val marginParams = ViewGroup.MarginLayoutParams(384, 384)
                marginParams.setMargins(10, 10, 10, 10)
                layoutParams = marginParams
            })
        }

        val destinationTestButton: Button = binding.destinationTestButton
        destinationTestButton.setOnClickListener {
            val fragment = DestinationFragment()
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.home_frame_layout, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}