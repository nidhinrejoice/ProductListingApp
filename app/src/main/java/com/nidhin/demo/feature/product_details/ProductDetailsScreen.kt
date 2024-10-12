package com.nidhin.demo.feature.product_details

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.nidhin.demo.feature.product_listing.ProductCard
import com.nidhin.demo.formatCurrency
import com.nidhin.demo.models.ProductUiModel
import com.nidhin.demo.utils.PaletteGenerator.convertImageUrlToBitmap
import com.nidhin.demo.utils.PaletteGenerator.extractColorsFromBitmap

@Composable
fun ProductDetailsScreen(
    modifier: Modifier = Modifier,
    product: ProductUiModel,
) {
    Surface {

        val context = LocalContext.current
        var dominantColor by remember { mutableStateOf(Color.Black) }
        var mutedColor by remember { mutableStateOf(Color.Transparent) }

        var currentImageSelected by remember {
            mutableStateOf(if (product.images.isEmpty()) product.thumbnail else product.images[0])
        }
        LaunchedEffect(true) {
            val bitmap = convertImageUrlToBitmap(product.thumbnail, context)
            bitmap?.let {
                val colors = extractColorsFromBitmap(bitmap)
                dominantColor = Color(android.graphics.Color.parseColor(colors["darkMuted"]))
                mutedColor = Color(android.graphics.Color.parseColor(colors["lightMuted"]))
            }
        }

        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(12.dp)
        ) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(), contentAlignment = Alignment.Center
                    ) {
                        if (currentImageSelected?.isNotEmpty() == true) {
                            ImageViewWithLoader(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .fillParentMaxHeight(0.43f),
                                url = currentImageSelected ?: "",
                                selected = false
                            )
                        }
                    }
                    HorizontalDivider(color = MaterialTheme.colorScheme.tertiary)
                    LazyRow(modifier = Modifier.fillMaxWidth()) {
                        items(product.images) { url ->
                            url?.let {
                                Box(
                                    modifier = Modifier
                                        .padding(top = 10.dp)
                                        .size(100.dp)
                                        .border(
                                            2.dp,
                                            if (currentImageSelected == it) mutedColor else Color.Transparent,
                                            RectangleShape
                                        )
                                        .clickable {
                                            currentImageSelected = it
                                        }, contentAlignment = Alignment.Center
                                ) {

                                    ImageViewWithLoader(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .fillParentMaxHeight(0.3f),
                                        url = url,
                                        selected = false
                                    )

                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    Column(modifier = Modifier.fillMaxWidth()) {

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = product.title,
                                style = MaterialTheme.typography.titleLarge,
                                color = dominantColor
                            )
                        }
                        if (product.rating > 0) {
                            Spacer(modifier = Modifier.size(5.dp))
                            Text(
                                text = "Rating : ${product.rating}",
                                style = MaterialTheme.typography.bodySmall,
                                color = dominantColor
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        HorizontalDivider()
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {

                            Text(
                                text = product.category.uppercase(),
                                style = MaterialTheme.typography.bodyLarge,
                                color = dominantColor
                            )
                            Text(
                                text = product.price.formatCurrency(),
                                style = MaterialTheme.typography.bodySmall,
                                color = dominantColor
                            )
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                        Text(
                            text = product.description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = dominantColor
                        )
                    }


                }
            }
        }
    }
}

@Composable
fun ImageViewWithLoader(modifier: Modifier = Modifier, url: String, selected: Boolean) {

    SubcomposeAsyncImage(
        model = url,
        contentDescription = "",
        loading = {
            Box(
                modifier = modifier,
                contentAlignment = Alignment.Center
            ) {

                CircularProgressIndicator(
                    modifier = Modifier.padding(
                        12.dp
                    )
                )
            }
        },
        modifier = modifier,
    )
}

@Preview
@Composable
fun PreviewProductDetails(modifier: Modifier = Modifier) {
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