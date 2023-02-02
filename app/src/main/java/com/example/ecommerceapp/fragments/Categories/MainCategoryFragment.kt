package com.example.ecommerceapp.fragments.Categories

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecommerceapp.R
import com.example.ecommerceapp.adapter.SpecialProductAdapter
import com.example.ecommerceapp.databinding.FragmentMainCategoryBinding
import com.example.ecommerceapp.viewModel.MainCategoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import util.States
@AndroidEntryPoint
class MainCategoryFragment : Fragment(R.layout.fragment_main_category) {
    private var _binding: FragmentMainCategoryBinding? = null
    val binding get() = _binding!!
    private lateinit var specialProductAdapter: SpecialProductAdapter
    private val mainCategoryViewModel by viewModels<MainCategoryViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainCategoryBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRecylerView()

        lifecycleScope.launchWhenStarted {
            mainCategoryViewModel.specialProduct.collectLatest {
                when (it) {
                    is States.Loading -> {
                        showLoaidng()
                    }
                    is States.OnSuccess -> {
                        specialProductAdapter.differ.submitList(it.data)
                        hideLoading()
                    }
                    is States.OnFailure ->{
                        hideLoading()
                        Log.e("MainCategory",it.message.toString())
                        Toast.makeText(requireContext(),it.message,Toast.LENGTH_LONG).show()
                    }else -> Unit

                }
            }
        }
    }

    private fun hideLoading() {
        binding.Loading.visibility = View.GONE

    }

    private fun showLoaidng() {
        binding.Loading.visibility = View.VISIBLE
    }

    private fun setUpRecylerView() {
        specialProductAdapter = SpecialProductAdapter()
        binding.rvSpecial.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = specialProductAdapter
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}