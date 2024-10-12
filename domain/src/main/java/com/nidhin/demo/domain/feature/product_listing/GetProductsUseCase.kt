package com.nidhin.demo.domain.feature.product_listing

import com.nidhin.demo.domain.models.ProductDomainModel
import com.nidhin.demo.domain.repositories.IProductRepository
import java.io.IOException
import javax.inject.Inject

class GetProductsUseCase @Inject constructor(private val productRepository: IProductRepository){


    suspend operator fun invoke(page:Int, limit: Int) : Result<List<ProductDomainModel>>{

        return try {
            Result.success(productRepository.getAllProducts(page,limit))

        }catch (e : IOException){
            Result.failure(Exception("Internet is not available"))
        }catch (e : Exception){
            Result.failure(e)
        }
    }

}