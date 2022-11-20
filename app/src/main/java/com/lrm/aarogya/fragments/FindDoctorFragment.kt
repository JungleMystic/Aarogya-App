package com.lrm.aarogya.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.lrm.aarogya.R
import com.lrm.aarogya.adapters.DoctorsAdapter
import com.lrm.aarogya.databinding.FragmentFindDoctorBinding
import com.lrm.aarogya.model.Doctor

class FindDoctorFragment : Fragment() {

    private lateinit var binding: FragmentFindDoctorBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var doctorsList: ArrayList<Doctor>
    private lateinit var adapter: DoctorsAdapter
    private lateinit var databaseRef: DatabaseReference

    private lateinit var hospitalId: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFindDoctorBinding.inflate(inflater, container, false)

        val navArgs: FindDoctorFragmentArgs by navArgs()
        hospitalId = navArgs.hospitalId

        binding.toolbar.inflateMenu(R.menu.menu_add_doctor)
        binding.toolbar.setOnMenuItemClickListener {

            if (it.itemId == R.id.addDoctor) {
                val action = FindDoctorFragmentDirections.actionFindDoctorFragmentToAddDoctorFragment(hospitalId)
                this.findNavController().navigate(action)
            }
            true
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var doctorId: String = ""

        doctorsList = ArrayList()
        adapter = DoctorsAdapter(requireContext(), doctorsList)

        recyclerView = binding.recyclerview
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        adapter.setOnItemClickListener(object: DoctorsAdapter.onItemClickListener {
            override fun onItemClick(position: Int) {

                val doctor = doctorsList[position]
                doctorId = doctor.doctorId

                val action = FindDoctorFragmentDirections.actionFindDoctorFragmentToDoctorDetailsFragment(hospitalId, doctorId)
                findNavController().navigate(action)
            }
        })

        databaseRef = FirebaseDatabase.getInstance().getReference("doctors_list").child(hospitalId)

        databaseRef.addValueEventListener(object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {

                doctorsList.clear()

                for (postSnapshot in snapshot.children) {
                    val doctors = postSnapshot.getValue(Doctor::class.java)
                    doctorsList.add(doctors!!)
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
            }
        })
    }
}