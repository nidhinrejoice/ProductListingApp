package com.nidhin.demo.domain.feature.product_listing

import com.nidhin.demo.domain.models.ProductDomainModel
import com.nidhin.demo.domain.repositories.IProductRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*

import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import java.io.IOException

class GetProductsUseCaseTest {

    val mockProduct =  ProductDomainModel(
        availabilityStatus = "In Stock",
        brand = "Brand A",
        category = "Makeup",
        description = "A luxurious lipstick that offers a creamy finish.",
        discountPercentage = 15.0,
        id = 1,
        images = listOf("https://cdn.dummyjson.com/products/images/fragrances/Calvin%20Klein%20CK%20One/1.png"),
        minimumOrderQuantity = 1,
        price = 25.0,
        rating = 4.5,
        returnPolicy = "30 days return policy",
        shippingInformation = "Free shipping on orders over $50",
        sku = "SKU123",
        stock = 50,
        tags = listOf("lipstick", "makeup"),
        thumbnail = "thumbnail_url1",
        title = "Luxury Lipstick",
        warrantyInformation = "No warranty",
        weight = 200
    )
    @Mock
    lateinit var productRepository: IProductRepository
    private lateinit var getProductsUseCase: GetProductsUseCase
    @Before
    fun setUp() {

        MockitoAnnotations.openMocks(this)
        getProductsUseCase = GetProductsUseCase(productRepository)
    }

    @Test
    fun `should return products when repository call is successful`() = runBlocking {
        val expectedProducts = listOf(mockProduct)
        val page = 0
        val limit = 10
        `when`(productRepository.getAllProducts(page, limit)).thenReturn(expectedProducts)

        val result = getProductsUseCase(page, limit)

        assertTrue(result.isSuccess)
        assertEquals(expectedProducts, result.getOrNull())
    }

    @Test
    fun `should return failure when IOException occurs`() = runBlocking {
        val page = 0
        val limit = 10
        `when`(productRepository.getAllProducts(page, limit)).thenThrow(IOException("No internet"))

        val result = getProductsUseCase(page, limit)

        assertTrue(result.isFailure)
        assertEquals("Internet is not available", result.exceptionOrNull()?.message)
    }

    @Test
    fun `should return failure for other exceptions`() = runBlocking {
        val page = 0
        val limit = 10
        val exceptionMessage = "Some other error"
        `when`(productRepository.getAllProducts(page, limit)).thenThrow(RuntimeException(exceptionMessage))

        val result = getProductsUseCase(page, limit)

        assertTrue(result.isFailure)
        assertEquals(exceptionMessage, result.exceptionOrNull()?.message)
    }
}