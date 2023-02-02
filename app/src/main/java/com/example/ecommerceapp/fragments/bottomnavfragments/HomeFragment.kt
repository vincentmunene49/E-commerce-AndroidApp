package com.example.ecommerceapp.fragments.bottomnavfragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.ecommerceapp.R
import com.example.ecommerceapp.adapter.HomeFragmentViewPagerAdapter
import com.example.ecommerceapp.databinding.FragmentHomeBinding
import com.example.ecommerceapp.databinding.FragmentLoginBinding
import com.example.ecommerceapp.fragments.Categories.*
import com.google.android.material.tabs.TabLayoutMediator
import kotlin.reflect.KProperty

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val categoriesFragments = arrayListOf<Fragment>(
            MainCategoryFragment(),
            ChairFragment(),
            FurnitureFragment(),
            AccessoryFragment(),
            TableFragment(),
            CupBoardFragment()
        )

        //adapter instance
        val view_pager_adapter = HomeFragmentViewPagerAdapter(
            categoriesFragments,
            childFragmentManager,
            lifecycle
        )
        binding.viewPager.adapter = view_pager_adapter
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, pos ->
            when (pos) {
                0 -> tab.text = "Main"
                1 -> tab.text = "Chair" 
                2 -> tab.text = "Cupboard"
                3 -> tab.text = "Table"
                4-> tab.text = "Accessory"
                5 -> tab.text = "Furniture"

            }
        }.attach()


    }
    //this is a test file this should genenrate a heratbea i recon

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}