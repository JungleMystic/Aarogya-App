package com.lrm.aarogya.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.lrm.aarogya.R
import com.lrm.aarogya.databinding.FragmentPatientProblemBinding

class PatientProblemFragment : Fragment() {

    private lateinit var binding: FragmentPatientProblemBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPatientProblemBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navArgs: PatientProblemFragmentArgs by navArgs()
        val hospitalName: String = navArgs.hospitalName
        val doctorName: String = navArgs.doctorName
        val hospitalAddress: String = navArgs.hospitalAddress
        val doctorSpecialization: String = navArgs.doctorSpecialization



        val user: FirebaseUser = FirebaseAuth.getInstance().currentUser!!

        binding.saveAppointmentButton.setOnClickListener {

            val patientName = binding.patientNameET.text.toString()
            val patientAge = binding.ageET.text.toString()
            val patientGender = binding.genderET.text.toString()
            val appointmentDate = binding.dateET.text.toString()
            val appointmentTime = binding.timeET.text.toString()
            val patientProblem = binding.problemET.text.toString()
            val appointmentId = "$patientName+$appointmentDate+$appointmentTime"

            if (patientName.isNotEmpty() && patientAge.isNotEmpty() && patientGender.isNotEmpty() &&
                patientProblem.isNotEmpty() && appointmentDate.isNotEmpty() && appointmentTime.isNotEmpty()) {
                val databaseRef: DatabaseReference = FirebaseDatabase.getInstance().getReference("appointment_data")
                    .child(user.uid)
                    .child(appointmentId)

                val hashMap: HashMap<String, String> = HashMap()
                hashMap.put("doctorName", doctorName)
                hashMap.put("doctorSpecialization", doctorSpecialization)
                hashMap.put("hospitalName", hospitalName)
                hashMap.put("hospitalAddress", hospitalAddress)
                hashMap.put("patientName", patientName)
                hashMap.put("patientAge", patientAge)
                hashMap.put("patientGender", patientGender)
                hashMap.put("appointmentDate", appointmentDate)
                hashMap.put("appointmentTime", appointmentTime)
                hashMap.put("patientProblem", patientProblem)
                hashMap.put("appointmentId", appointmentId)

                databaseRef.setValue(hashMap).addOnCompleteListener { task ->
                    if (task.isSuccessful) {

                        val action = PatientProblemFragmentDirections.actionPatientProblemFragmentToAppointmentDetailsFragment()
                        this.findNavController().navigate(action)
                        Toast.makeText(context, "Data Uploaded Successfully...", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {

                Log.i("Patient data", "$patientName, $patientAge, $patientGender, $appointmentDate, $appointmentTime, $patientProblem")
                Toast.makeText(context, "Please fill all the fields...", Toast.LENGTH_SHORT).show()
            }
        }
    }
}