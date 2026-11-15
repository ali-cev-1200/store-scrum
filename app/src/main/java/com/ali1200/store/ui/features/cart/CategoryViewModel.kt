package com.ali1200.store.ui.features.cart

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.isDebugInspectorInfoEnabled
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ali1200.store.model.data.Ads
import com.ali1200.store.model.data.Product
import com.ali1200.store.model.repository.product.ProductRepository
import com.ali1200.store.model.repository.user.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CategoryViewModel(
    private val productRepository: ProductRepository
) : ViewModel() {
    val dataProducts = mutableStateOf<List<Product>>(listOf())

    fun loadDataByCategory(category: String) {

        viewModelScope.launch {

            val dataFromLocal = productRepository.getAllProductsByCategory(category)
            dataProducts.value = dataFromLocal

        }

    }

}