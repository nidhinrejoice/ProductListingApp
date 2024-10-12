package com.nidhin.demo.feature.product_listing

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nidhin.demo.models.ProductUiModel
import com.nidhin.demo.models.toUiModel
import com.nidhin.demo.domain.feature.product_listing.GetProductsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@HiltViewModel
class ProductListingViewModel @Inject constructor(
    private val getProductUseCase: GetProductsUseCase
) : ViewModel() {

    var state by mutableStateOf(PaginatorState())

    data class PaginatorState(
        val page: Int = 0,
        val endReached: Boolean = false,
        val isLoading: Boolean = false,
        val error: String? = null,
        val items: List<ProductUiModel> = emptyList()
    )

    val pageinator = DefaultPaginator<Int, ProductUiModel>(
        initialKey = state.page,
        onLoadUpdated = {
            state = state.copy(isLoading = it)
        },
        onRequest = { nextPage ->
            getProductUseCase(nextPage, 10)
        },
        getNextKey = {
            state.page + 1
        },
        onError = {
            state = state.copy(error = it?.localizedMessage, isLoading = false)
        },
        onSuccess = { items, newKey ->
            state = state.copy(
                items = state.items + items.map { it.toUiModel() },
                page = newKey,
                endReached = items.isEmpty(), error = null
            )
        }

    )

    init {
        getProducts(state.page)
    }


    fun getProducts(page: Int) {
        viewModelScope.launch {
            pageinator.loadNextItems()
        }
    }

}