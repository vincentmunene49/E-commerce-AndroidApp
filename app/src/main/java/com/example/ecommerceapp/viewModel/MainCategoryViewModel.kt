package com.example.ecommerceapp.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerceapp.data.Product
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObjects
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import util.States
import javax.inject.Inject

@HiltViewModel
class MainCategoryViewModel @Inject constructor(
    private val firestore: FirebaseFirestore
) : ViewModel() {

    private val _specialProduct = MutableStateFlow<States<List<Product>>>(States.Starting())
    val specialProduct:StateFlow<States<List<Product>>> = _specialProduct

init {
    fetchFromFireStroe()
}
    //fetch product from firestore

    fun fetchFromFireStroe(){
        viewModelScope.launch {
            _specialProduct.emit(States.Loading())
        }
        firestore.collection("Product")
            .whereEqualTo("category","Chair")
            .get()
            .addOnSuccessListener {
                val specialProductList = it.toObjects(Product::class.java)
                viewModelScope.launch {
                    _specialProduct.emit(States.OnSuccess(specialProductList))
                }
            }
            .addOnFailureListener {
                viewModelScope.launch {
                    _specialProduct.emit(States.OnFailure(it.message.toString()))
                }

            }
    }
}