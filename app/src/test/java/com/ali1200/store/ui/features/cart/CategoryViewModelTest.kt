package com.ali1200.store.ui.features.cart

import com.ali1200.store.fakes.MainDispatcherRule
import com.ali1200.store.fakes.fakeProduct
import com.ali1200.store.model.repository.product.ProductRepository
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class CategoryViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun loadDataByCategory_showsOnlyThatCategory() {
        val bags = listOf(fakeProduct("1", category = "Handbag"))
        val repository: ProductRepository = mockk()
        coEvery { repository.getAllProductsByCategory("Handbag") } returns bags

        val viewModel = CategoryViewModel(repository)
        viewModel.loadDataByCategory("Handbag")

        assertEquals(bags, viewModel.dataProducts.value)
    }
}
