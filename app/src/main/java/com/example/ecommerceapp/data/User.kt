package com.example.ecommerceapp.data

data class User(
    val f_name:String,
    val other_name:String,
    val email:String,
    var image_path:String = ""
) {
    constructor():this("","","","")//to be used by the firebase
}