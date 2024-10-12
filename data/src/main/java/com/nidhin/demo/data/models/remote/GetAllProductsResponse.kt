package com.nidhin.demo.data.models.remote

import com.nidhin.demo.data.models.remote.Product

data class GetAllProductsResponse(
    val limit: Int,
    val products: List<Product>,
    val skip: Int,
    val total: Int
)