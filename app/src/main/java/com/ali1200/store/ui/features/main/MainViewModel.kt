package com.ali1200.store.ui.features.main

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.isDebugInspectorInfoEnabled
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ali1200.store.model.data.Ads
import com.ali1200.store.model.data.Product
import com.ali1200.store.model.repository.product.ProductRepository
import com.ali1200.store.model.repository.user.UserRepository
import com.ali1200.store.util.coroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainViewModel(
    private val productRepository: ProductRepository,
    isInternetConnected: Boolean
) : ViewModel() {

    val dataProducts = mutableStateOf<List<Product>>(listOf())
    val dataAds = mutableStateOf<List<Ads>>(listOf())
    val showProgressBar = mutableStateOf(false)
    // the cart badge is added with the cart in sprint 5
    val badgeNumber = mutableStateOf(0)


    init {
        refreshAllDataFromNet(isInternetConnected)
    }

    private fun refreshAllDataFromNet(isInternetConnected: Boolean) {

        viewModelScope.launch(coroutineExceptionHandler) {

            if (isInternetConnected)
                showProgressBar.value = true

            delay(1000)

            val newDataProducts = async { productRepository.getAllProducts(isInternetConnected) }
            val newDataAds = async { productRepository.getAllAds(isInternetConnected) }

            updateData(newDataProducts.await(), newDataAds.await())

            showProgressBar.value = false

        }

    }

    private fun updateData(products: List<Product>, ads: List<Ads>) {
        dataProducts.value = products
        dataAds.value = ads
    }

}