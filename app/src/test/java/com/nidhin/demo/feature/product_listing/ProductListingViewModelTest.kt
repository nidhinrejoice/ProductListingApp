package com.nidhin.demo.feature.product_listing

import com.nidhin.demo.domain.feature.product_listing.GetProductsUseCase
import com.nidhin.demo.domain.models.ProductDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

@ExperimentalCoroutinesApi
class ProductListingViewModelTest {


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

    private lateinit var getProductsUseCase: GetProductsUseCase
    private lateinit var productListingViewModel: ProductListingViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        getProductsUseCase = mock()

        productListingViewModel = ProductListingViewModel(getProductsUseCase)
    }

    @Test
    fun `loadNextItems should update state with products on success`() = runTest {
        val products = listOf(mockProduct)
        `when`(getProductsUseCase.invoke(0, 10)).thenReturn(Result.success(products))

        productListingViewModel.getProducts(0)
        advanceUntilIdle()

        with(productListingViewModel.state) {
            assertTrue(items.isNotEmpty())
            assertEquals(1, items.size)
            assertEquals(1, page)
            assertFalse(endReached)
        }
    }

    @Test
    fun `loadNextItems should update state with error message on failure`() = runTest {
        val errorMessage = "Internet is not available"
        `when`(getProductsUseCase.invoke(0, 10)).thenReturn(Result.failure(Exception(errorMessage)))

        productListingViewModel.getProducts(0)
        advanceUntilIdle()

        with(productListingViewModel.state) {
            assertNotNull(error)
            assertEquals(errorMessage, error)
            assertFalse(isLoading)
        }
    }

    @Test
    fun `loadNextItems should update isLoading state correctly`() = runTest {
        `when`(getProductsUseCase.invoke(0, 10)).thenReturn(Result.success(emptyList()))

        productListingViewModel.getProducts(0)
        advanceUntilIdle()

        with(productListingViewModel.state) {
            assertFalse(isLoading)
        }
    }

    @Test
    fun `getProducts should call loadNextItems`() = runTest {
        val products = listOf(mockProduct)
        `when`(getProductsUseCase.invoke(0, 10)).thenReturn(Result.success(products))

        productListingViewModel.getProducts(0)
        advanceUntilIdle()

        verify(getProductsUseCase).invoke(0, 10)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}