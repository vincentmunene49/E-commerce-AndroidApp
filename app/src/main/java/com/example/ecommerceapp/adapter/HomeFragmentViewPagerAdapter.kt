package com.example.ecommerceapp.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class HomeFragmentViewPagerAdapter(
    private val fragment_list:List<Fragment>,
    fragment_manager:FragmentManager,
    lifecycle: Lifecycle
): FragmentStateAdapter(fragment_manager,lifecycle) {
    override fun getItemCount(): Int {
       return fragment_list.size
    }
    override fun createFragment(position: Int): Fragment {
        return fragment_list[position]
    }
}