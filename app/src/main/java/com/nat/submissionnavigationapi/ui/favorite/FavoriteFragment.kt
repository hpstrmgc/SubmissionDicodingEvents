package com.nat.submissionnavigationapi.ui.favorite

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
import com.nat.submissionnavigationapi.database.FavoriteEvent
import com.nat.submissionnavigationapi.databinding.FragmentFavoriteBinding
import com.nat.submissionnavigationapi.repository.EventRepository
import com.nat.submissionnavigationapi.ui.detail.DetailEventActivity
import com.nat.submissionnavigationapi.ui.settings.SettingsPreferences
import com.nat.submissionnavigationapi.ui.settings.SettingsViewModel
import com.nat.submissionnavigationapi.ui.settings.SettingsViewModelFactory
import com.nat.submissionnavigationapi.ui.settings.dataStore

class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: FavoriteViewModel
    private lateinit var favoriteAdapter: FavoriteEventsAdapter

    private val settingsViewModel: SettingsViewModel by viewModels {
        val pref = SettingsPreferences.getInstance(requireContext().dataStore)
        SettingsViewModelFactory(pref)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val repository = EventRepository.Injection.provideRepository(requireActivity().application)
        val factory = FavoriteViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[FavoriteViewModel::class.java]

        favoriteAdapter = FavoriteEventsAdapter(emptyList())
        binding.recyclerViewFavorite.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = favoriteAdapter
        }

        viewModel.favoriteEvents.observe(viewLifecycleOwner) { events ->
            if (events.isEmpty()) {
                binding.tvNoFavorites.visibility = View.VISIBLE
                binding.recyclerViewFavorite.visibility = View.GONE
            } else {
                binding.tvNoFavorites.visibility = View.GONE
                binding.recyclerViewFavorite.visibility = View.VISIBLE
                favoriteAdapter.setEvents(events)
            }
        }

        favoriteAdapter.setOnItemClickCallback(object : FavoriteEventsAdapter.OnItemClickCallback {
            override fun onItemClicked(data: FavoriteEvent) {
                val intent = Intent(activity, DetailEventActivity::class.java)
                intent.putExtra("event_id", data.id)
                startActivity(intent)
            }
        })

        settingsViewModel.getThemeSettings()
            .observe(viewLifecycleOwner) { isDarkModeActive: Boolean ->
                if (isDarkModeActive) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}