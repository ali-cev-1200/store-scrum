package com.ali1200.store.util

import org.junit.Assert.assertEquals
import org.junit.Test

class MyScreensTest {

    @Test
    fun allRoutes_areUnique() {
        val routes = listOf(
            MyScreens.MainScreen,
            MyScreens.ProductScreen,
            MyScreens.CategoryScreen,
            MyScreens.ProfileScreen,
            MyScreens.CartScreen,
            MyScreens.SignUpScreen,
            MyScreens.SignInScreen
        ).map { it.route }

        assertEquals(routes.size, routes.toSet().size)
    }

    @Test
    fun productRoute_withArgument_isBuiltCorrectly() {
        val route = MyScreens.ProductScreen.route + "/" + "{$KEY_PRODUCT_ARG}"
        assertEquals("productScreen/{productId}", route)
    }
}
