package com.lrm.aarogya.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.lrm.aarogya.R
import com.lrm.aarogya.adapters.AppointmentAdapter
import com.lrm.aarogya.databinding.FragmentAppointmentDetailsBinding
import com.lrm.aarogya.model.AppointmentData
import com.lrm.aarogya.model.UserDetails

class AppointmentDetailsFragment : Fragment() {

    private lateinit var binding: FragmentAppointmentDetailsBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var appointmentList: ArrayList<AppointmentData>
    private lateinit var adapter: AppointmentAdapter
    private lateinit var databaseRef: DatabaseReference
    private lateinit var databaseRef2: DatabaseReference
    private lateinit var user: FirebaseUser

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAppointmentDetailsBinding.inflate(inflater, container, false)

        binding.toolbar.inflateMenu(R.menu.menu)
        binding.toolbar.setOnMenuItemClickListener {

            if (it.itemId == R.id.settings) {
                val action = AppointmentDetailsFragmentDirections.actionAppointmentDetailsFragmentToSettingsFragment()
                this.findNavController().navigate(action)
            }
            true
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        user = FirebaseAuth.getInstance().currentUser!!

        appointmentList = ArrayList()
        adapter = AppointmentAdapter(requireContext(), appointmentList)

        recyclerView = binding.recyclerview
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        adapter.setOnItemClickListener(object: AppointmentAdapter.onItemClickListener {
            override fun onItemClick(position: Int) {
                val appointments = appointmentList[position]
                val appointmentId = appointments.appointmentId
            }

        })

        databaseRef2 = FirebaseDatabase.getInstance().getReference("users").child(user.uid)

        databaseRef2.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val currentUser = snapshot.getValue(UserDetails::class.java)

                if (currentUser!!.profilePic == "") {
                    binding.mainMyProfilePic.setImageResource(R.drawable.profile_icon)
                } else {
                    Glide.with(this@AppointmentDetailsFragment).load(currentUser.profilePic)
                        .into(binding.mainMyProfilePic)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
            }

        })


        databaseRef = FirebaseDatabase.getInstance().getReference("appointment_data").child(user.uid)

        databaseRef.addValueEventListener(object: ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {

                appointmentList.clear()

                for (postSnapshot in snapshot.children) {
                    val appointments = postSnapshot.getValue(AppointmentData::class.java)
                    appointmentList.add(appointments!!)
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        binding.mainMyProfilePic.setOnClickListener {
            val action = AppointmentDetailsFragmentDirections.actionAppointmentDetailsFragmentToYourProfileFragment()
            this.findNavController().navigate(action)
        }

        binding.searchHospitalsFAB.setOnClickListener {
            val action = AppointmentDetailsFragmentDirections.actionAppointmentDetailsFragmentToFindHospitalFragment()
            this.findNavController().navigate(action)
        }
    }
}