package com.lrm.aarogya.fragments

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.lrm.aarogya.R
import com.lrm.aarogya.databinding.FragmentAddDoctorBinding

class AddDoctorFragment : Fragment() {

    private lateinit var binding: FragmentAddDoctorBinding
    private lateinit var databaseRef: DatabaseReference

    private lateinit var imageUri: Uri

    private val selectImage = registerForActivityResult(ActivityResultContracts.GetContent()) {
        imageUri = it!!

        binding.doctorImage.setImageURI(imageUri)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddDoctorBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navArgs: AddDoctorFragmentArgs by navArgs()
        val hospitalId: String = navArgs.hospitalId

        binding.hospitalDoctorId.setText(hospitalId)

        binding.backButton.setOnClickListener {
            this.findNavController().navigate(R.id.action_addDoctorFragment_to_findDoctorFragment)
        }

        binding.doctorImage.setOnClickListener {
            selectImage.launch("image/*")
        }

        binding.saveButton.setOnClickListener {
            uploadImage()
        }
    }

    private fun uploadImage() {
        val hospitalId = binding.hospitalDoctorId.text.toString()
        val doctorId = binding.doctorId.text.toString()

        val storageRef: StorageReference = FirebaseStorage.getInstance().getReference("doctor_pics")
            .child(hospitalId)
            .child(doctorId)
            .child("doctor_pic")

        storageRef.putFile(imageUri)
            .addOnCompleteListener {
                storageRef.downloadUrl.addOnSuccessListener {
                    storeData(it, hospitalId, doctorId)
                }
            }
    }

    private fun storeData(imageUri: Uri?, hospitalId: String, doctorId: String) {

        val doctorName = binding.doctorName.text.toString()
        val doctorSpecialization = binding.doctorSpecialization.text.toString()
        val doctorExperience = binding.doctorExperience.text.toString()
        val doctorAvailability = binding.doctorAvailability.text.toString()

        databaseRef = FirebaseDatabase.getInstance().getReference("doctors_list").child(hospitalId).child(doctorId)

        val hashMap: HashMap<String, String> = HashMap()
        hashMap.put("hospitalId", hospitalId)
        hashMap.put("doctorId", doctorId)
        hashMap.put("doctorName", doctorName)
        hashMap.put("doctorSpecialization", doctorSpecialization)
        hashMap.put("doctorExperience", doctorExperience)
        hashMap.put("doctorAvailability", doctorAvailability)
        hashMap.put("doctorPic", imageUri.toString())

        databaseRef.setValue(hashMap).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                this.findNavController().navigate(R.id.action_addDoctorFragment_to_findDoctorFragment)
                Toast.makeText(context, "Data Uploaded Successfully...", Toast.LENGTH_SHORT).show()
            }
        }
    }
}