package com.example.ecommerceapp.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.ecommerceapp.R
import com.example.ecommerceapp.data.User
import com.example.ecommerceapp.databinding.FragmentRegisterBinding
import com.example.ecommerceapp.util.RegisterValidation
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.withContext
import util.States
import viewModel.RegisterViewModel
private val TAG = "RegisterFragment"
@AndroidEntryPoint
class RegisterFragment:Fragment() {
    private var _binding:FragmentRegisterBinding? = null
    val binding get() = _binding!!
    private val viewModel by viewModels<RegisterViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.apply {
            registerButton.setOnClickListener {
                val user = User(
                    fName.text.toString().trim(),
                    lName.text.toString().trim(),
                    email.text.toString().trim()
                )
                val password = password.text.toString()
                viewModel.createAccount(user, password)
            }
        }
        //Navigate to login
        binding.toLogin.setOnClickListener{
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }
            lifecycleScope.launchWhenStarted {
                viewModel.register.collect{
                    when(it){
                        is States.Loading ->{
                            //animation
                            binding.registerButton.startAnimation()
                        }
                        is States.OnSuccess ->{
                            Log.d(TAG,it.data.toString())
                            binding.registerButton.revertAnimation()
                        }
                        is States.OnFailure ->{
                            Log.d(TAG,it.message.toString())
                            binding.registerButton.revertAnimation()
                        }
                        else -> Unit
                    }//end of when
                }
            }//end of lifecycle scope
        lifecycleScope.launchWhenStarted {
            viewModel.validation.collect{
                if(it.email is RegisterValidation.Failed){
                    withContext(Dispatchers.Main){
                        binding.email.apply {
                            requestFocus()
                            error = it.email.message
                        }
                    }
                }

                if(it.password is RegisterValidation.Failed){
                    withContext(Dispatchers.Main){
                        binding.password.apply {
                            requestFocus()
                            error = it.password.message
                        }
                    }
                }
            }
        }

    }


    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}

