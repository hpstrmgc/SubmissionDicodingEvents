package com.nat.submissionnavigationapi.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.nat.submissionnavigationapi.databinding.FragmentHomeBinding
import com.nat.submissionnavigationapi.ui.detail.DetailEventActivity
import com.nat.submissionnavigationapi.ui.detail.ListEventsItem
import com.nat.submissionnavigationapi.ui.finished.FinishedEventsAdapter
import com.nat.submissionnavigationapi.ui.upcoming.UpcomingEventsAdapter

class HomeFragment : Fragment(), FinishedEventsAdapter.OnItemClickCallback {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var upcomingEventsAdapter: UpcomingEventsAdapter
    private lateinit var finishedEventsAdapter: FinishedEventsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        binding.rvUpcoming.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.rvFinished.layoutManager = LinearLayoutManager(context)

        homeViewModel.events.observe(viewLifecycleOwner) { events ->
            events?.let {
                upcomingEventsAdapter = UpcomingEventsAdapter(it)
                binding.rvUpcoming.adapter = upcomingEventsAdapter
            } ?: run {
                Log.d("HomeFragment", "No upcoming events found.")
            }
        }

        homeViewModel.finishedEvents.observe(viewLifecycleOwner) { finishedEvents ->
            finishedEvents?.let {
                finishedEventsAdapter = FinishedEventsAdapter(it)
                finishedEventsAdapter.setOnItemClickCallback(this) // Set callback
                binding.rvFinished.adapter = finishedEventsAdapter
            } ?: run {
                Log.d("HomeFragment", "No finished events found.")
            }
        }

        homeViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.rvUpcoming.visibility = if (isLoading) View.GONE else View.VISIBLE
            binding.rvFinished.visibility = if (isLoading) View.GONE else View.VISIBLE
        }

        homeViewModel.errorState.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage?.let {
                Log.e("HomeFragment", it)
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
                homeViewModel.clearError()
            }
        }

        return binding.root
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