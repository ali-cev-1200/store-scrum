package com.ali1200.store.fakes

import com.ali1200.store.model.data.Product

fun fakeProduct(
    id: String,
    category: String = "Backpack",
    quantity: String? = null
) = Product(
    productId = id,
    name = "Product $id",
    imgUrl = "",
    detailText = "",
    price = "100000",
    soldItem = "0",
    category = category,
    material = "",
    tags = "Newest",
    quantity = quantity
)
