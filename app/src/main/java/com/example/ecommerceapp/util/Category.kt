package com.example.ecommerceapp.util

sealed class Category(val category: String){

    object Chair: Category("chair")
    object Table: Category("table")
    object Furniture: Category("furniture")
    object Accessory: Category("accessory")
    object Cupboard: Category("cupboard")
}
