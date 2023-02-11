package com.example.ecommerceapp.fragments.Categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecommerceapp.R
import com.example.ecommerceapp.adapter.BestProductsAdapter
import com.example.ecommerceapp.databinding.FragmentBaseCategoryBinding

open class BaseCategory() : Fragment(R.layout.fragment_base_category) {

    private var _binding: FragmentBaseCategoryBinding? = null
    private val binding get() = _binding!!
    protected val bestProductAdapter: BestProductsAdapter by lazy { BestProductsAdapter() }
    protected val offerPercentageAdapter: BestProductsAdapter by lazy { BestProductsAdapter() }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBaseCategoryBinding.inflate(layoutInflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        setUpOfferRecylerView()
        setUpBestProducts()
    }

    private fun setUpBestProducts() {
        binding.rvBestProducts.apply {
            layoutManager =
                GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
            adapter = bestProductAdapter
        }
    }

    private fun setUpOfferRecylerView() {
        binding.rvOfferPercentage.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = offerPercentageAdapter
        }
    }

    fun showOfferLoading() {
        binding.rvOfferPercentageProgressBar.visibility = View.VISIBLE

    }
    fun hideOfferLoading(){
        binding.rvOfferPercentageProgressBar.visibility = View.GONE

    }

    fun showBestProductLoading() {
        binding.rvBestProductsProgressBar.visibility =  View.VISIBLE

    }
    fun hideBestProductsLoading(){
        binding.rvBestProductsProgressBar.visibility =  View.GONE

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}