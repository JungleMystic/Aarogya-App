package com.lrm.aarogya.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lrm.aarogya.R
import com.lrm.aarogya.adapters.StateListAdapter
import com.lrm.aarogya.databinding.FragmentSelectStateBinding

class SelectStateFragment : Fragment() {

    private lateinit var binding: FragmentSelectStateBinding
    private lateinit var adapter: StateListAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSelectStateBinding.inflate(inflater, container, false)

        binding.toolbar.inflateMenu(R.menu.menu)
        binding.toolbar.setOnMenuItemClickListener {

            when (it.itemId) {
                R.id.your_appointments -> {val action = SelectStateFragmentDirections.actionSelectStateFragmentToAppointmentDetailsFragment()
                                    this.findNavController().navigate(action)}

                R.id.your_profile -> {val action = SelectStateFragmentDirections.actionSelectStateFragmentToYourProfileFragment()
                    this.findNavController().navigate(action)}

                else -> {val action = SelectStateFragmentDirections.actionSelectStateFragmentToSettingsFragment()
                    this.findNavController().navigate(action)}
            }
            true
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = StateListAdapter()

        recyclerView = binding.recyclerview
        recyclerView.layoutManager = GridLayoutManager(requireContext(),2)
        recyclerView.adapter = adapter

        adapter.setOnItemClickListener(object : StateListAdapter.onItemClickListener {
            override fun onItemClick(position: Int) {
                val action = SelectStateFragmentDirections.actionSelectStateFragmentToFindHospitalFragment()
                findNavController().navigate(action)
            }
        })
    }
}