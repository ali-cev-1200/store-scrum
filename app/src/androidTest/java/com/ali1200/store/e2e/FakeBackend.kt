package com.ali1200.store.e2e

import com.google.gson.Gson
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import java.util.concurrent.CopyOnWriteArrayList

/**
 * A local fake of the shop API, served by MockWebServer.
 * E2E tests drive the real UI, ViewModels, repositories, Retrofit and Room against it.
 */
object FakeBackend {

    const val PAYMENT_LINK = "https://pay.example.com/order/7"

    // Plain JSON maps (not app model classes) so this file compiles in every sprint
    private fun product(id: String, name: String, detail: String, price: String, category: String, quantity: Int? = null) =
        mapOf(
            "productId" to id, "name" to name, "imgUrl" to "", "detailText" to detail, "price" to price,
            "soldItem" to "5", "category" to category, "material" to "Leather", "tags" to "Newest",
            "quantity" to quantity?.toString()
        )

    private val products = listOf(
        product("1", "Leather Backpack", "A brown leather backpack", "250000", "Backpack"),
        product("2", "Red Handbag", "A small red handbag", "180000", "Handbag")
    )

    /** How many "Leather Backpack" items are in the user's cart. */
    @Volatile
    var cartQuantity = 0

    /** Path and body of every request the app sent, in order. */
    val requests = CopyOnWriteArrayList<Pair<String, String>>()

    private val gson = Gson()

    private fun cart() =
        if (cartQuantity == 0) emptyList()
        else listOf(product("1", "Leather Backpack", "A brown leather backpack", "250000", "Backpack", cartQuantity))

    private val server = MockWebServer().apply {
        dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                val path = request.path ?: ""
                val body = request.body.readUtf8()
                requests.add(path to body)
                val ok = mapOf("success" to true, "message" to "ok")
                val response: Map<String, Any?> = when (path) {
                    "/signIn" ->
                        if (body.contains("wrong")) mapOf("success" to false, "message" to "wrong password", "token" to "", "expiresAt" to 0)
                        else ok + mapOf("token" to "test-token", "expiresAt" to 0)
                    "/signUp", "/refreshToken" -> ok + mapOf("token" to "test-token", "expiresAt" to 0)
                    "/getProducts" -> ok + mapOf("products" to products)
                    "/getSliderPics" -> ok + mapOf("ads" to listOf(mapOf("adId" to "1", "imageURL" to "", "productId" to "1")))
                    "/getComments" -> ok + mapOf("comments" to listOf(mapOf("commentId" to "1", "text" to "Great quality!", "userEmail" to "buyer@mail.com")))
                    "/addNewComment", "/addToCart", "/removeFromCart" -> ok
                    "/getUserCart" -> ok + mapOf("productList" to cart(), "totalPrice" to 250000 * cartQuantity)
                    "/submitOrder" -> ok + mapOf("orderId" to 7, "paymentLink" to PAYMENT_LINK)
                    "/checkout" -> mapOf("success" to false)
                    else -> return MockResponse().setResponseCode(404)
                }
                return MockResponse().setBody(gson.toJson(response))
            }
        }
    }

    val baseUrl: String by lazy {
        server.start()
        server.url("/").toString()
    }

    fun reset() {
        cartQuantity = 0
        requests.clear()
    }

    fun paths() = requests.map { it.first }

    fun lastBody(path: String) = requests.last { it.first == path }.second
}
