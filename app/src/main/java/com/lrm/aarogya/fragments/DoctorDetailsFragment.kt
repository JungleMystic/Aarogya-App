package com.lrm.aarogya.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.firebase.database.*
import com.lrm.aarogya.R
import com.lrm.aarogya.databinding.FragmentDoctorDetailsBinding
import com.lrm.aarogya.model.Doctor
import com.lrm.aarogya.model.Hospital

class DoctorDetailsFragment : Fragment() {

    private lateinit var binding: FragmentDoctorDetailsBinding
    private lateinit var hospitalName: String
    private lateinit var doctorName: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDoctorDetailsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var hospitalName: String = ""
        var doctorName: String = ""
        var hospitalAddress: String = ""
        var doctorSpecialization: String = ""

        val navArgs: DoctorDetailsFragmentArgs by navArgs()
        val doctorId: String = navArgs.doctorId
        val hospitalId: String = navArgs.hospitalId

        val databaseRef: DatabaseReference = FirebaseDatabase.getInstance().getReference("hospitals_list").child(hospitalId)

        databaseRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val hospitalData = snapshot.getValue(Hospital::class.java)

                binding.hospitalNameInDetails.text = hospitalData!!.hospitalName
                hospitalName = hospitalData.hospitalName

                binding.hospitalAddressInDetails.text = hospitalData.hospital_address
                hospitalAddress = hospitalData.hospital_address

                if (hospitalData.hospital_pic == "") {
                    binding.hospitalPicInDetails.setImageResource(R.drawable.hospital_logo3)
                } else {
                    Glide.with(requireContext()).load(hospitalData.hospital_pic).placeholder(R.drawable.hospital_logo3).into(binding.hospitalPicInDetails)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

        val databaseRef2: DatabaseReference = FirebaseDatabase.getInstance().getReference("doctors_list")
            .child(hospitalId)
            .child(doctorId)

        databaseRef2.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val doctorData = snapshot.getValue(Doctor::class.java)

                binding.doctorNameDetails.text = doctorData!!.doctorName
                doctorName = doctorData.doctorName
                binding.doctorNameInDetails.text = doctorData.doctorName

                binding.doctorSpecializationInDetails.text = doctorData.doctorSpecialization
                doctorSpecialization = doctorData.doctorSpecialization

                binding.doctorExperienceInDetails.text = doctorData.doctorExperience
                binding.doctorAvailabilityInDetails.text = doctorData.doctorAvailability

                if (doctorData.doctorPic == "") {
                    binding.doctorPicInDetails.setImageResource(R.drawable.doctor_icon2)
                } else {
                    Glide.with(context!!).load(doctorData.doctorPic).placeholder(R.drawable.doctor_icon2).into(binding.doctorPicInDetails)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        binding.bookAppointmentButton.setOnClickListener {
            val action = DoctorDetailsFragmentDirections.actionDoctorDetailsFragmentToPatientProblemFragment(hospitalName, doctorName, hospitalAddress, doctorSpecialization)
            this.findNavController().navigate(action)
        }
    }
}