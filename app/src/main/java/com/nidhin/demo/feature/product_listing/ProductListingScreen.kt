package com.nidhin.demo.feature.product_listing

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.nidhin.demo.feature.product_details.ImageViewWithLoader
import com.nidhin.demo.models.ProductUiModel
import kotlinx.coroutines.launch

@Composable
fun ProductListingScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: ProductListingViewModel = hiltViewModel()
) {
    val scope = rememberCoroutineScope()
    Surface(modifier = Modifier.fillMaxSize()) {
        if (viewModel.state.items.isEmpty() && viewModel.state.error == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {

            Column(modifier = modifier) {
                val lazyGridState = rememberLazyGridState()
                val isLastItemVisible by remember {
                    derivedStateOf {
                        val visibleItemCount = lazyGridState.layoutInfo.visibleItemsInfo.size
                        val totalItems = lazyGridState.layoutInfo.totalItemsCount
                        val lastVisibleItemIndex =
                            lazyGridState.firstVisibleItemIndex + visibleItemCount
                        totalItems != 0 && lastVisibleItemIndex >= totalItems - 1
                    }
                }
                LaunchedEffect(isLastItemVisible) {
                    if (isLastItemVisible && !viewModel.state.endReached && !viewModel.state.isLoading) {
                        viewModel.pageinator.loadNextItems()
                    }
                }
                if (viewModel.state.error?.isNotEmpty() == true) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize(), contentAlignment = Alignment.Center
                    ) {
                        Text(text = "${viewModel.state.error ?: ""}. Click to Retry",
                            modifier = Modifier
                                .clickable {
                                    scope.launch {
                                        viewModel.pageinator.reset()
                                        viewModel.pageinator.loadNextItems()
                                    }
                                })
                    }
                }
                SwipeRefresh(
                    state = rememberSwipeRefreshState(viewModel.state.isLoading && viewModel.state.items.isNotEmpty()),
                    onRefresh = {
                        scope.launch {
                            viewModel.pageinator.reset()
                            viewModel.pageinator.loadNextItems()
                        }
                    },
                ) {
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(200.dp),
                        state = lazyGridState
                    ) {
                        itemsIndexed(
                            items = viewModel.state.items,
                            key = { index, item ->
                                item.id
                            }
                        ) { index, item ->
                            ProductCard(product = item) {
                                navController.navigate("product_details?product_id=$it")
                            }
                        }
                        item {
                            if (viewModel.state.isLoading && viewModel.state.items.isNotEmpty()) {
                                Box(
                                    modifier = Modifier.fillMaxWidth(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column {
                                        CircularProgressIndicator()
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "Loading items...",
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

            }
        }
    }

}

@Composable
fun ProductCard(modifier: Modifier = Modifier, product: ProductUiModel, onClick: (String) -> Unit) {

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                onClick(product.id.toString())
            },
        elevation = CardDefaults.cardElevation(4.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            ImageViewWithLoader(
                modifier = Modifier
                    .aspectRatio(1f)
                    .padding(4.dp), url = product.thumbnail, selected = false
            )
            Spacer(modifier = Modifier.height(20.dp))
            Column {

                Text(
                    text = product.title,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = product.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}


@Preview
@Composable
fun PreviewProductCard(modifier: Modifier = Modifier) {
    val product = ProductUiModel(
        category = "Makeup",
        description = "A luxurious lipstick that offers a creamy finish.",
        id = 1,
        images = listOf("https://cdn.dummyjson.com/products/images/fragrances/Calvin%20Klein%20CK%20One/1.png"),
        price = 25.0,
        rating = 4.5,
        thumbnail = "thumbnail_url1",
        title = "Luxury Lipstick",
    )
    ProductCard(product = product) {}
}