package com.example.ecommerceapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecommerceapp.R
import com.example.ecommerceapp.adapter.ColorItemAdapter
import com.example.ecommerceapp.adapter.ImagesViewPagerAdapter
import com.example.ecommerceapp.adapter.SizesAdapter
import com.example.ecommerceapp.databinding.FragmentProductDetialsBinding

class ProductDetailsFragment : Fragment(R.layout.fragment_product_detials) {
    private var _binding: FragmentProductDetialsBinding? = null
    private val binding get() = _binding!!
    private val imagesViewAdapter by lazy { ImagesViewPagerAdapter() }
    private val sizesAdapter by lazy { SizesAdapter() }
    private val colorAdapter by lazy { ColorItemAdapter() }
    private val args by navArgs<ProductDetailsFragmentArgs>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductDetialsBinding.inflate(layoutInflater)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val product = args.product

        //setUp recylerView
        setUpSizesRecylerView()
        setUpColorsRecylerView()
        setUpViewPager()

        //display info
        binding.apply {
            tvProductName.text = product.name
            product.description?.let {
                tvProductDescription.text = it
            }
            tvProductPrice.text = "$ ${product.price.toString()}"

            if (product.sizes.isNullOrEmpty()) {
                tvProductSizes.visibility = View.GONE
            }
            if (product.colors.isNullOrEmpty()) {
                tvProductColors.visibility = View.GONE
            }

        }
        imagesViewAdapter.differ.submitList(product.images)
        product.colors?.let {
            colorAdapter.differ.submitList(it)
        }
        product.sizes?.let {
            sizesAdapter.differ.submitList(it)
        }

        binding.imageClose.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setUpViewPager() {
        binding.apply {
            imageViewPager.adapter = imagesViewAdapter
        }
    }

    private fun setUpColorsRecylerView() {
        binding.rvColors.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = colorAdapter
        }
    }

    private fun setUpSizesRecylerView() {
        binding.rvSizes.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = sizesAdapter
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}