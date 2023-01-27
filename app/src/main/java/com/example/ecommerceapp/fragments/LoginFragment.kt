package com.example.ecommerceapp.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.ecommerceapp.Activities.ShoppingActivity
import com.example.ecommerceapp.R
import com.example.ecommerceapp.databinding.FragmentLoginBinding
import com.example.ecommerceapp.viewModel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import util.States

@AndroidEntryPoint
class LoginFragment:Fragment() {
    var _binding: FragmentLoginBinding? = null
    val binding get() = _binding!!
  private val viewModel by viewModels<LoginViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            loginButton.setOnClickListener{
                val email = email.text.toString().trim()
                val password = password.text.toString()
                viewModel.loginUser(email,password)
            }
        }

        //Todo forgot password

        binding.forgotPassword.setOnClickListener{
            findNavController().navigate(R.id.action_loginFragment_to_forgotPasswordFragment)
        }


        lifecycleScope.launchWhenStarted {
            viewModel.login.collect{
                when(it){
                    is States.Loading -> {
                        binding.loginButton.startAnimation()
                    }
                    is States.OnSuccess -> {
                        binding.loginButton.revertAnimation()
                        Intent(requireActivity(),ShoppingActivity::class.java).also {
                            it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)//this pops the previous activity so user
                        // doesn't have to navigate back to login activity
                            startActivity(it)
                        }

                    }
                    is States.OnFailure ->{
                        Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_LONG).show()
                        binding.loginButton.revertAnimation()
                    }else -> Unit
                }
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}