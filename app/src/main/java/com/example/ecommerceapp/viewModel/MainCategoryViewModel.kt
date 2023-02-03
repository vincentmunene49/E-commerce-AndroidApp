package com.example.ecommerceapp.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerceapp.data.Product
import com.google.firebase.firestore.FirebaseFirestore
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

    private val _bestDeals = MutableStateFlow<States<List<Product>>>(States.Starting())
    val bestDeals:StateFlow<States<List<Product>>> = _bestDeals

    private val _bestProducts = MutableStateFlow<States<List<Product>>>(States.Starting())
    val bestProducts:StateFlow<States<List<Product>>> = _bestProducts

init {
    fetchFromFireStore()
    fetchBestDeals()
    fetchBestProducts()

}
    //fetch product from firestore

    fun fetchFromFireStore(){
        viewModelScope.launch {
            _specialProduct.emit(States.Loading())
        }
        //fetch special procucts
        firestore.collection("Product")
            .whereEqualTo("category","special products")
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

    fun fetchBestDeals(){
        viewModelScope.launch {
            _bestDeals.emit(States.Loading())
        }
        //fetch bestDeals
        firestore.collection("Product")
            .whereEqualTo("category","best deals")
            .get()
            .addOnSuccessListener {
                val best_deals_list = it.toObjects(Product::class.java)
                viewModelScope.launch {
                    _bestDeals.emit(States.OnSuccess(best_deals_list))
                }
            }.addOnFailureListener {
                viewModelScope.launch{
                    _bestDeals.emit(States.OnFailure(it.message.toString()))
                }
            }
    }

    fun fetchBestProducts(){
        viewModelScope.launch {
            _bestProducts.emit(States.Loading())
        }

        //fetch bestProducts
        firestore.collection("Product")
            .get()
            .addOnSuccessListener {
                val bestProductsList = it.toObjects(Product::class.java)
                viewModelScope.launch {
                    _bestProducts.emit(States.OnSuccess(bestProductsList))
                }
            }.addOnFailureListener {
                viewModelScope.launch{
                    _bestProducts.emit(States.OnFailure(it.message.toString()))
                }
            }
    }
}