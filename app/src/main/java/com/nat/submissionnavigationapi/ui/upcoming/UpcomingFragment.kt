package com.nat.submissionnavigationapi.ui.upcoming

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.nat.submissionnavigationapi.databinding.FragmentUpcomingBinding
import com.nat.submissionnavigationapi.ui.detail.DetailEventActivity
import com.nat.submissionnavigationapi.ui.detail.ListEventsItem
import com.nat.submissionnavigationapi.ui.settings.SettingsPreferences
import com.nat.submissionnavigationapi.ui.settings.SettingsViewModel
import com.nat.submissionnavigationapi.ui.settings.SettingsViewModelFactory
import com.nat.submissionnavigationapi.ui.settings.dataStore

class UpcomingFragment : Fragment(), UpcomingEventsAdapterForUpcomingFragment.OnItemClickCallback {

    private var _binding: FragmentUpcomingBinding? = null
    private val binding get() = _binding!!

    private val settingsViewModel: SettingsViewModel by viewModels {
        val pref = SettingsPreferences.getInstance(requireContext().dataStore)
        SettingsViewModelFactory(pref)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val upcomingViewModel = ViewModelProvider(this)[UpcomingViewModel::class.java]

        _binding = FragmentUpcomingBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.rvUpcoming.layoutManager = LinearLayoutManager(context)

        upcomingViewModel.events.observe(viewLifecycleOwner) { events ->
            val adapter = UpcomingEventsAdapterForUpcomingFragment(events.take(5))
            adapter.setOnItemClickCallback(this) // Set callback
            binding.rvUpcoming.adapter = adapter
        }

        upcomingViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.rvUpcoming.visibility = if (isLoading) View.GONE else View.VISIBLE
        }

        settingsViewModel.getThemeSettings()
            .observe(viewLifecycleOwner) { isDarkModeActive: Boolean ->
                if (isDarkModeActive) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
            }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClicked(data: ListEventsItem) {
        val intent = Intent(requireContext(), DetailEventActivity::class.java)
        intent.putExtra("event_id", data.id)
        startActivity(intent)
    }
}