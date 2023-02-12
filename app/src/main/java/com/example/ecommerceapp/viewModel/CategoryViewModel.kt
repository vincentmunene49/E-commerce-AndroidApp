package com.example.ecommerceapp.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerceapp.data.Product
import com.example.ecommerceapp.util.Category
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import util.States

class CategoryViewModel(
    private val firestore: FirebaseFirestore,
    private val category: Category
) : ViewModel() {
    private var _offerProducts = MutableStateFlow<States<List<Product>>>(States.Starting())
    val offerProducts: StateFlow<States<List<Product>>> = _offerProducts
    private var _bestProducts = MutableStateFlow<States<List<Product>>>(States.Starting())
    val bestProducts: StateFlow<States<List<Product>>> = _bestProducts
    private val pagingInfo = PagingInfo()

    init {
        getBestProducts()
        getOfferProducts()
    }

    //retrieve products with offer
    fun getOfferProducts() {
        viewModelScope.launch {
            _offerProducts.emit(States.Loading())
        }
        firestore.collection("Product").whereEqualTo("category", category.category)
            .whereNotEqualTo("offerPercentage", null).get().addOnSuccessListener {

                val productList = it.toObjects(Product::class.java)
                viewModelScope.launch {
                    _offerProducts.emit(States.OnSuccess(productList))
                }


            }.addOnFailureListener {
                viewModelScope.launch {
                    _offerProducts.emit(States.OnFailure(it.message.toString()))
                }

            }

    }

    fun getBestProducts() {
        if (!pagingInfo.isPagingEnd) {


            viewModelScope.launch {
                _bestProducts.emit(States.Loading())
            }
            firestore.collection("Product").whereEqualTo("category", category.category)
                .whereEqualTo("offerPercentage", null).limit(pagingInfo.bestProductsPage * 10)
                .get().addOnSuccessListener {

                    val productList = it.toObjects(Product::class.java)
                    pagingInfo.isPagingEnd = productList == pagingInfo.oldBestProduct
                    pagingInfo.oldBestProduct = productList
                    viewModelScope.launch {
                        _bestProducts.emit(States.OnSuccess(productList))
                    }
                    pagingInfo.bestProductsPage++


                }.addOnFailureListener {
                    viewModelScope.launch {
                        _bestProducts.emit(States.OnFailure(it.message.toString()))
                    }

                }
        }
    }
    internal data class PagingInfo(
        var bestProductsPage: Long = 1,
        var oldBestProduct: List<Product> = emptyList(),
        var isPagingEnd: Boolean = false

    )
}
