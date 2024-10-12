package com.nidhin.demo.data.feature

import com.nidhin.demo.data.api.ApiService
import com.nidhin.demo.data.models.remote.toDomainModel
import com.nidhin.demo.domain.models.ProductDomainModel
import com.nidhin.demo.domain.repositories.IProductRepository
import javax.inject.Inject

class ProductRepository @Inject constructor(
    private val apiService: ApiService
) : IProductRepository {
    override suspend fun getAllProducts(page: Int, limit: Int): List<ProductDomainModel> {

        return apiService.getProducts(
            limit = limit,
            skip = page * limit
        ).products.map { it.toDomainModel() }
    }
}