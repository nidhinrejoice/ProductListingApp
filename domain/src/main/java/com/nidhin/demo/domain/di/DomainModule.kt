package com.nidhin.demo.domain.di

import com.nidhin.demo.domain.feature.product_listing.GetProductsUseCase
import com.nidhin.demo.domain.repositories.IProductRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
class DomainModule {

    @Provides
    fun provideGetProductsUseCase(productRepository: IProductRepository): GetProductsUseCase {
        return GetProductsUseCase(productRepository)
    }

}