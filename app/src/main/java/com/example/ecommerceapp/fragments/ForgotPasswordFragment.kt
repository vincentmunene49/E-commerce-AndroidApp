package com.example.ecommerceapp.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.ecommerceapp.databinding.FragmentForgotPasswordBinding
import com.example.ecommerceapp.viewModel.LoginViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import util.States
@AndroidEntryPoint
class ForgotPasswordFragment:Fragment() {
    var _binding: FragmentForgotPasswordBinding? = null
    val binding get() = _binding!!
    val viewModel by viewModels<LoginViewModel>()

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
            sendEmail.setOnClickListener{
                val email = email.text.toString().trim()
                viewModel.resetPassword(email)

            }
        }
        lifecycleScope.launchWhenStarted {
            viewModel.resetPassword.collect{
                when(it){
                    is States.Loading ->{
                        //animation
                        binding.sendEmail.startAnimation()
                    }
                    is States.OnSuccess ->{
                        binding.sendEmail.revertAnimation()
                        Snackbar.make(requireView(),"Email Sent",Snackbar.LENGTH_LONG).show()
                    }
                    is States.OnFailure ->{
                        binding.sendEmail.revertAnimation()
                        Snackbar.make(requireView(),"Error:${it.message.toString()}",Snackbar.LENGTH_LONG).show()
                    }
                    else -> Unit
                }//end of when
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}