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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecommerceapp.R
import com.example.ecommerceapp.adapter.BestDealsAdapter
import com.example.ecommerceapp.adapter.BestProductsAdapter
import com.example.ecommerceapp.adapter.SpecialProductAdapter
import com.example.ecommerceapp.databinding.FragmentMainCategoryBinding
import com.example.ecommerceapp.viewModel.MainCategoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import util.States
@AndroidEntryPoint
class MainCategoryFragment : Fragment(R.layout.fragment_main_category) {
    private var _binding: FragmentMainCategoryBinding? = null
    val binding get() = _binding!!
    private lateinit var specialProductAdapter: SpecialProductAdapter
    private lateinit var bestDealsAdapter:BestDealsAdapter
    private lateinit var bestProductAdapter: BestProductsAdapter
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
        setUpBestDealsRv()
        setUpBestProductsRv()


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
                    is States.OnFailure -> {
                        hideLoading()
                        Log.e("MainCategory", it.message.toString())
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                    }
                    else -> Unit

                }
            }
        }
            //best deals
            lifecycleScope.launchWhenStarted {
                mainCategoryViewModel.bestDeals.collectLatest {
                    when (it) {
                        is States.Loading -> {
                            showLoaidng()
                        }
                        is States.OnSuccess -> {
                            bestDealsAdapter.differ.submitList(it.data)

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
        //best products
        lifecycleScope.launchWhenStarted {
            mainCategoryViewModel.bestProducts.collectLatest {
                when (it) {
                    is States.Loading -> {
                        showLoaidng()
                    }
                    is States.OnSuccess -> {
                        bestProductAdapter.differ.submitList(it.data)

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

    private fun setUpBestProductsRv() {
        bestProductAdapter = BestProductsAdapter()
        binding.bestProducts.apply {
           layoutManager  =GridLayoutManager(requireContext(),2,GridLayoutManager.VERTICAL,false)
            adapter = bestProductAdapter
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
    private fun setUpBestDealsRv(){
        //bestDeals
        bestDealsAdapter = BestDealsAdapter()
        binding.bestDeals.apply {
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
            adapter = bestDealsAdapter
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}