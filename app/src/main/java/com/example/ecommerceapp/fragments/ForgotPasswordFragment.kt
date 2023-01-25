package com.example.ecommerceapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.ecommerceapp.databinding.FragmentForgotPasswordBinding

class ForgotPasswordFragment:Fragment() {
    var _binding: FragmentForgotPasswordBinding? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentForgotPasswordBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            forgotPasswordButton.setOnClickListener{
                val email = email.text.toString().trim()

            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}