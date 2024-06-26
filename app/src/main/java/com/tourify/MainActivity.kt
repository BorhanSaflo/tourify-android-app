package com.tourify

import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.tourify.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        navView.setupWithNavController(navController)

        val toolbar: Toolbar = binding.toolbar2
        val toolbarController = findNavController(R.id.nav_host_fragment_activity_main)
        toolbar.setupWithNavController(toolbarController)

        toolbarController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.navigation_home)
                toolbar.setLogo(R.drawable.tourify_black_logo_short)
            else
                toolbar.setLogo(null)
        }

    }
}