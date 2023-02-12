package com.example.ecommerceapp.util

import android.view.View
import androidx.fragment.app.Fragment
import com.example.ecommerceapp.Activities.ShoppingActivity
import com.example.ecommerceapp.R
import com.google.android.material.bottomnavigation.BottomNavigationView

fun Fragment.hideBottomNav(){
    //hide the bottom nav
    val bottomNav = (activity as ShoppingActivity).findViewById<BottomNavigationView>(R.id.bottom_nav_view)
    bottomNav.visibility = View.GONE
}
fun Fragment.showBottomNav(){
    //show the bottom nav
    val bottomNav = (activity as ShoppingActivity) .findViewById<BottomNavigationView>(R.id.bottom_nav_view)
    bottomNav.visibility = View.VISIBLE
}