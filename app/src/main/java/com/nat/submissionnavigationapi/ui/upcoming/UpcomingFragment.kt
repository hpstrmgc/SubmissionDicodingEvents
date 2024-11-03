package com.nat.submissionnavigationapi.ui.upcoming

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.nat.submissionnavigationapi.DetailEventActivity
import com.nat.submissionnavigationapi.ListEventsItem
import com.nat.submissionnavigationapi.UpcomingEventsAdapterForUpcomingFragment
import com.nat.submissionnavigationapi.databinding.FragmentUpcomingBinding

class UpcomingFragment : Fragment(), UpcomingEventsAdapterForUpcomingFragment.OnItemClickCallback {

    private var _binding: FragmentUpcomingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
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