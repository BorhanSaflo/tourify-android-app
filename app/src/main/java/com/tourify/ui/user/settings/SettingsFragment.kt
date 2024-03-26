package com.tourify.ui.user.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tourify.R
import com.tourify.databinding.FragmentSettingsBinding
import com.tourify.ui.user.settings.langauge.LanguageFragment
import com.tourify.ui.user.settings.notifications.NotificationsFragment
import com.tourify.ui.user.settings.theme.ThemeFragment

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val settingsViewModel =
            ViewModelProvider(this)[SettingsViewModel::class.java]

        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Theme button
        val themeButton: Button = binding.settingsThemeButton
        themeButton.setOnClickListener {
            changeFragment(ThemeFragment())
        }

        // Notification button
        val notificationButton: Button = binding.settingsNotificationsButton
        notificationButton.setOnClickListener {
            changeFragment(NotificationsFragment())
        }

        // Language button
        val languageButton: Button = binding.settingsLanguageButton
        languageButton.setOnClickListener {
            changeFragment(LanguageFragment())
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun changeFragment(newFragment: Fragment) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.settings_frame_layout, newFragment)
            .addToBackStack(null)
            .commit()
    }
}