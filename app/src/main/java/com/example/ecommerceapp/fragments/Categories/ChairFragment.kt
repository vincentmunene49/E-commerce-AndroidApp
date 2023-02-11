package com.example.ecommerceapp.fragments.Categories

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.ecommerceapp.util.Category
import com.example.ecommerceapp.viewModel.CategoryViewModel
import com.example.ecommerceapp.viewModel.ProviderFactory
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import util.States
import javax.inject.Inject

@AndroidEntryPoint
class ChairFragment : BaseCategory() {
    @Inject
    lateinit var firebase: FirebaseFirestore
    val viewModel by viewModels<CategoryViewModel> {
        ProviderFactory(firebase, Category.Chair)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        lifecycleScope.launchWhenStarted {
            viewModel.offerProducts.collectLatest {
                when (it) {
                    is States.Loading -> {
                        showOfferLoading()
                    }
                    is States.OnSuccess -> {
                        offerPercentageAdapter.differ.submitList(it.data)
                        hideOfferLoading()
                    }
                    is States.OnFailure -> {
                        Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_LONG)
                            .show()
                        hideOfferLoading()

                    }
                    else -> Unit
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.bestProducts.collectLatest {
                when (it) {
                    is States.Loading -> {
                        showBestProductLoading()

                    }
                    is States.OnSuccess -> {
                        bestProductAdapter.differ.submitList(it.data)
                        hideBestProductsLoading()

                    }
                    is States.OnFailure -> {
                        Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_LONG)
                            .show()
                        hideBestProductsLoading()


                    }
                    else -> Unit
                }
            }
        }
    }
}