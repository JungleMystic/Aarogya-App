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
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.lrm.aarogya.databinding.FragmentAddHospitalBinding

class AddHospitalFragment : Fragment() {

    private lateinit var binding: FragmentAddHospitalBinding
    private lateinit var databaseRef: DatabaseReference

    private lateinit var imageUri: Uri

    private val selectImage = registerForActivityResult(ActivityResultContracts.GetContent()) {
        imageUri = it!!

        binding.hospitalImage.setImageURI(imageUri)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddHospitalBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backButton.setOnClickListener {
            val action = AddHospitalFragmentDirections.actionAddHospitalFragmentToFindHospitalFragment()
            this.findNavController().navigate(action)
        }

        binding.hospitalImage.setOnClickListener {
            selectImage.launch("image/*")
        }

        binding.saveButton.setOnClickListener {
            uploadImage()
        }
    }

    private fun uploadImage() {
        val hospitalId = binding.hospitalId.text.toString()

        val storageRef: StorageReference = FirebaseStorage.getInstance().getReference("hospitals_pics")
            .child(hospitalId)
            .child("hospital_pic")

        storageRef.putFile(imageUri)
            .addOnCompleteListener {
                storageRef.downloadUrl.addOnSuccessListener {
                    storeData(it, hospitalId!!)
                }
            }
    }

    private fun storeData(imageUri: Uri?, hospitalId: String) {

        databaseRef = FirebaseDatabase.getInstance().getReference("hospitals_list").child(hospitalId)

        val hospitalName = binding.hospitalName.text.toString()
        val hospitalAddress = binding.hospitalAddress.text.toString()

        val hashMap: HashMap<String, String> = HashMap()
        hashMap.put("hospitalId", hospitalId)
        hashMap.put("hospitalName", hospitalName)
        hashMap.put("hospital_pic", imageUri.toString())
        hashMap.put("hospital_address", hospitalAddress)

        databaseRef.setValue(hashMap).addOnCompleteListener { task ->
            if (task.isSuccessful) {

                val action = AddHospitalFragmentDirections.actionAddHospitalFragmentToFindHospitalFragment()
                this.findNavController().navigate(action)
                Toast.makeText(context, "Data Uploaded Successfully...", Toast.LENGTH_SHORT).show()
            }
        }
    }
}