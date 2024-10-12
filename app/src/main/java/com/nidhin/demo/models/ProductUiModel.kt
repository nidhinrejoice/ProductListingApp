package com.nidhin.demo.models

import com.nidhin.demo.domain.models.ProductDomainModel

data class ProductUiModel(
    val category: String,
    val description: String,
    val id: Int,
    val images: List<String?>,
    val price: Double,
    val rating: Double,
    val thumbnail: String,
    val title: String,
)

fun ProductDomainModel.toUiModel() =
    ProductUiModel(
        category = category,
        description = description,
        id = id,
        images = images,
        price = price,
        rating = rating,
        thumbnail = thumbnail,
        title = title,

    )