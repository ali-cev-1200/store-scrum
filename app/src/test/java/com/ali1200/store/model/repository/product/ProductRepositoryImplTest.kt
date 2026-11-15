package com.ali1200.store.model.repository.product

import com.ali1200.store.fakes.fakeProduct
import com.ali1200.store.model.data.Ads
import com.ali1200.store.model.data.AdsResponse
import com.ali1200.store.model.data.ProductResponse
import com.ali1200.store.model.db.ProductDao
import com.ali1200.store.model.net.ApiService
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ProductRepositoryImplTest {

    private lateinit var apiService: ApiService
    private lateinit var productDao: ProductDao
    private lateinit var repository: ProductRepositoryImpl

    @Before
    fun setUp() {
        apiService = mockk()
        productDao = mockk(relaxed = true)
        repository = ProductRepositoryImpl(apiService, productDao)
    }

    @Test
    fun online_returnsServerProductsAndCachesThem() = runBlocking {
        val products = listOf(fakeProduct("1"), fakeProduct("2"))
        coEvery { apiService.getAllProducts() } returns ProductResponse(true, products)

        val result = repository.getAllProducts(isInternetConnected = true)

        assertEquals(products, result)
        coVerify { productDao.insertOrUpdate(products) }
    }

    @Test
    fun offline_readsProductsFromDatabase() = runBlocking {
        val cached = listOf(fakeProduct("1"))
        coEvery { productDao.getAll() } returns cached

        val result = repository.getAllProducts(isInternetConnected = false)

        assertEquals(cached, result)
        coVerify(exactly = 0) { apiService.getAllProducts() }
    }

    @Test
    fun online_serverFailure_returnsEmptyList() = runBlocking {
        coEvery { apiService.getAllProducts() } returns ProductResponse(false, listOf())

        assertTrue(repository.getAllProducts(isInternetConnected = true).isEmpty())
    }

    @Test
    fun ads_offline_returnsEmptyList() = runBlocking {
        assertTrue(repository.getAllAds(isInternetConnected = false).isEmpty())
    }

    @Test
    fun ads_online_returnsServerAds() = runBlocking {
        val ads = listOf(Ads("a1", "url", "1"))
        coEvery { apiService.getAllAds() } returns AdsResponse(true, ads)

        assertEquals(ads, repository.getAllAds(isInternetConnected = true))
    }
}
