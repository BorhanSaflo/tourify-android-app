package com.tourify.ui.user.savedDestinations

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tourify.ImageFetcher
import com.tourify.R
import com.tourify.databinding.FragmentSavedDestinationsBinding
import com.tourify.ui.home.destination.DestinationFragment

class SavedDestinationsFragment : Fragment() {

    private var _binding: FragmentSavedDestinationsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val savedDestinationsViewModel =
            ViewModelProvider(this)[SavedDestinationsViewModel::class.java]

        _binding = FragmentSavedDestinationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

//        loadMostLikedDestinations()

        return root
    }

//    private fun loadMostLikedDestinations() {
//        RetrofitClient.apiService.getMostLikedDestinations().enqueue(object : Callback<List<Destination>> {
//            override fun onResponse(call: Call<List<Destination>>, response: Response<List<Destination>>) {
//                if (response.isSuccessful)
//                    response.body()?.let { places -> updateSection(places, binding.savedDestinationsGridLayout) }
//            }
//
//            override fun onFailure(call: Call<List<Destination>>, t: Throwable) {
//                Log.d("HomeFragment", "Error fetching most liked destinations: ${t.message}")
//            }
//        })
//    }

//    private fun updateSection(destinations: List<Destination>, layout: ViewGroup) {
//        layout.removeAllViews()
//        for (destination in destinations) {
//            val placeLayout = LayoutInflater.from(context).inflate(R.layout.item_destination_card, layout, false)
//            val imageView = placeLayout.findViewById<ImageView>(R.id.image_view_destination)
//            val textView = placeLayout.findViewById<TextView>(R.id.text_view_destination_name)
//            val loadingIcon = placeLayout.findViewById<ProgressBar>(R.id.progress_bar_destination)
//
//            textView.text = destination.name
//
//            loadingIcon.visibility = View.VISIBLE
//            imageView.visibility = View.GONE
//
//            getImage(destination.thumbnail) { bitmap ->
//                imageView.setImageBitmap(bitmap)
//                setupImageClickListener(imageView, destination.id.toInt())
//                loadingIcon.visibility = View.GONE
//                imageView.visibility = View.VISIBLE
//                imageView.layoutParams.width = layout.width / 3 - placeLayout.paddingLeft * 3
//            }
//
//            layout.addView(placeLayout)
//        }
//    }

    private fun getImage(url: String, onImageFetched: (Bitmap?) -> Unit) {
        ImageFetcher.fetchImage(url) { bitmap ->
            activity?.runOnUiThread {
                if (bitmap == null) {
                    Toast.makeText(requireContext(), "Failed to load image", Toast.LENGTH_SHORT).show()
                    onImageFetched(null)
                } else {
                    onImageFetched(bitmap)
                }
            }
        }
    }

    private fun setupImageClickListener(imageView: ImageView, id : Int) {
        imageView.setOnClickListener {
            val args = Bundle()
            args.putInt("id", id)

            val fragment = DestinationFragment()
            fragment.arguments = args

            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.saved_destinations_frame_layout, fragment)
                .addToBackStack(null)
                .commit()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}