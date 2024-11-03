package com.nat.submissionnavigationapi.ui.finished

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.nat.submissionnavigationapi.DetailEventActivity
import com.nat.submissionnavigationapi.FinishedEventsAdapterForFinishedFragment
import com.nat.submissionnavigationapi.ListEventsItem
import com.nat.submissionnavigationapi.databinding.FragmentFinishedBinding

class FinishedFragment : Fragment(),
    FinishedEventsAdapterForFinishedFragment.OnItemClickCallback {

    private var _binding: FragmentFinishedBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val finishedViewModel =
            ViewModelProvider(this)[FinishedViewModel::class.java]

        _binding = FragmentFinishedBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val gridLayoutManager = GridLayoutManager(context, 2)
        binding.rvFinished.layoutManager = gridLayoutManager

        finishedViewModel.finishedEvents.observe(viewLifecycleOwner) { finishedEvents ->
            binding.progressBar.visibility = View.GONE
            val adapter = FinishedEventsAdapterForFinishedFragment(finishedEvents)
            adapter.setOnItemClickCallback(this) // Set callback
            binding.rvFinished.adapter = adapter
        }

        finishedViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
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