package com.lrm.aarogya.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.lrm.aarogya.activities.MainActivity
import com.lrm.aarogya.databinding.FragmentSignInBinding

class SignInFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: FragmentSignInBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSignInBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()

        binding.signInButton.setOnClickListener {
            performSignIn()
        }

        binding.signUpLL.setOnClickListener {
            val action = SignInFragmentDirections.actionSignInFragmentToSignUpFragment()
            this.findNavController().navigate(action)
        }
    }

    private fun performSignIn() {
        val inputMail = binding.emailET.text.toString()
        val inputPassword = binding.passwordET.text.toString()

        if (inputMail.isNotEmpty() && inputPassword.isNotEmpty()) {

            auth.signInWithEmailAndPassword(inputMail, inputPassword)
                .addOnCompleteListener { task ->

                    if (task.isSuccessful) {

                        val intent = Intent(this@SignInFragment.requireContext(), MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)

                        Toast.makeText(context, "Signed in Successfully...", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(context, "Authentication failed...${it.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(context, "Please fill all the fields...", Toast.LENGTH_SHORT).show()
        }
    }
}