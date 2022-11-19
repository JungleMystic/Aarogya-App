package com.lrm.aarogya.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.lrm.aarogya.R
import com.lrm.aarogya.adapters.HospitalAdapter
import com.lrm.aarogya.databinding.FragmentFindHospitalBinding
import com.lrm.aarogya.model.Hospital

class FindHospitalFragment : Fragment() {

    private lateinit var binding: FragmentFindHospitalBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var hospitalList: ArrayList<Hospital>
    private lateinit var adapter: HospitalAdapter
    private lateinit var databaseRef: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFindHospitalBinding.inflate(inflater, container, false)

        binding.toolbar.inflateMenu(R.menu.menu_add_hospital)
        binding.toolbar.setOnMenuItemClickListener {

            if (it.itemId == R.id.addHospital) {
                val action = FindHospitalFragmentDirections.actionFindHospitalFragmentToAddHospitalFragment()
                this.findNavController().navigate(action)
            }
            true
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        hospitalList = ArrayList()
        adapter = HospitalAdapter(requireContext(), hospitalList)

        recyclerView = binding.recyclerview
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        adapter.setOnItemClickListener(object: HospitalAdapter.onItemClickListener {
            override fun onItemClick(position: Int) {

                val hospital = hospitalList[position]
                val hospitalId:String = hospital.hospitalId

                val action = FindHospitalFragmentDirections.actionFindHospitalFragmentToFindDoctorFragment(hospitalId)
                findNavController().navigate(action)
            }
        })

        databaseRef = FirebaseDatabase.getInstance().getReference("hospitals_list")

        databaseRef.addValueEventListener(object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {

                hospitalList.clear()

                for (postSnapshot in snapshot.children) {
                    val hospitals = postSnapshot.getValue(Hospital::class.java)

                    hospitalList.add(hospitals!!)
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
            }
        })

    }

}