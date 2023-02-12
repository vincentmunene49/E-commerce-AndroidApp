package com.example.ecommerceapp.fragments.Categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecommerceapp.R
import com.example.ecommerceapp.adapter.BestProductsAdapter
import com.example.ecommerceapp.databinding.FragmentBaseCategoryBinding
import com.example.ecommerceapp.util.showBottomNav

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

        //navigate to product details
        bestProductAdapter.onItemClickedListener = {
            val bundle = Bundle().apply {
                putParcelable("product", it)
            }

            findNavController().navigate(R.id.productDetailsFragment2, bundle)
        }
        //navigate to product details
        offerPercentageAdapter.onItemClickedListener = {
            val bundle = Bundle().apply {
                putParcelable("product", it)
            }

            findNavController().navigate(R.id.productDetailsFragment2, bundle)
        }

        //listen to our nested scrollview
        binding.BaseCategoryScrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, _, scrollY, _, _ ->
            if (v.getChildAt(0).bottom <= v.height + scrollY) {
                paging()
            }
        })
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

    fun hideOfferLoading() {
        binding.rvOfferPercentageProgressBar.visibility = View.GONE

    }

    fun showBestProductLoading() {
        binding.rvBestProductsProgressBar.visibility = View.VISIBLE

    }

    fun hideBestProductsLoading() {
        binding.rvBestProductsProgressBar.visibility = View.GONE

    }

    open fun paging() {

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        showBottomNav()
    }
}