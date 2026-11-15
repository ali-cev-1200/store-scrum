package com.ali1200.store.ui.features.main

import com.ali1200.store.fakes.MainDispatcherRule
import com.ali1200.store.fakes.fakeProduct
import com.ali1200.store.model.data.Ads
import com.ali1200.store.model.repository.product.ProductRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var productRepository: ProductRepository

    @Before
    fun setUp() {
        productRepository = mockk()
    }

    @Test
    fun init_loadsProductsAndAds() = runTest {
        val products = listOf(fakeProduct("1"), fakeProduct("2"))
        val ads = listOf(Ads("a1", "url", "1"))
        coEvery { productRepository.getAllProducts(true) } returns products
        coEvery { productRepository.getAllAds(true) } returns ads

        val viewModel = MainViewModel(productRepository, true)
        advanceUntilIdle()

        assertEquals(products, viewModel.dataProducts.value)
        assertEquals(ads, viewModel.dataAds.value)
        assertFalse(viewModel.showProgressBar.value)
    }

}
