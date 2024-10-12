package com.nidhin.demo.domain.repositories

import com.nidhin.demo.domain.models.ProductDomainModel


interface IProductRepository {

    suspend fun getAllProducts(page: Int, limit: Int): List<ProductDomainModel>
}