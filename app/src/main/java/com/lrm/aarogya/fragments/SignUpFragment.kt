package com.lrm.aarogya.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.lrm.aarogya.activities.MainActivity
import com.lrm.aarogya.databinding.FragmentSignUpBinding

class SignUpFragment : Fragment() {

    private lateinit var binding: FragmentSignUpBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseRef: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSignUpBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()

        binding.signUpButton.setOnClickListener {
            performSignUp()
        }

        binding.signInLL.setOnClickListener {
            val action = SignUpFragmentDirections.actionSignUpFragmentToSignInFragment()
            this.findNavController().navigate(action)
        }
    }

    private fun performSignUp() {
        val inputName = binding.usernameET.text.toString()
        val inputMail = binding.emailET.text.toString()
        val inputPassword = binding.passwordET.text.toString()
        val confirmPassword = binding.confirmPasswordET.text.toString()

        if (inputName.isNotEmpty() && inputMail.isNotEmpty() && inputPassword.isNotEmpty() && confirmPassword.isNotEmpty()) {
            if (inputPassword == confirmPassword) {
                auth.createUserWithEmailAndPassword(inputMail, confirmPassword)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            addUserToDatabase(inputName, inputMail)
                        }
                    }
                    .addOnFailureListener {
                        Toast.makeText(context, "Error occurred ${it.localizedMessage}", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(context, "Password is not matching...", Toast.LENGTH_SHORT).show()
                Log.i("Password Mismatch", "Input password is $inputPassword")
                Log.i("Password Mismatch", "Confirm password is $confirmPassword")
            }
        } else {
            Toast.makeText(context, "Please fill all the fields...", Toast.LENGTH_SHORT).show()
        }
    }

    private fun addUserToDatabase(inputName: String, inputMail: String) {

        val currentUser = auth.currentUser!!.uid

        databaseRef = FirebaseDatabase.getInstance().getReference("users").child(currentUser)

        val hashMap: HashMap<String, String> = HashMap()
        hashMap.put("name", inputName)
        hashMap.put("inputEmail", inputMail)
        hashMap.put("uid", currentUser)
        hashMap.put("profilePic", "")

        databaseRef.setValue(hashMap).addOnCompleteListener { task ->

            if (task.isSuccessful) {

                val intent = Intent(this@SignUpFragment.requireContext(), MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)

                Toast.makeText(context, "Account has been created", Toast.LENGTH_SHORT).show()
            }
        }
    }
}