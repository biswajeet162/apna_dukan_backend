package com.apnadukan.catalog.dto.response

data class ProductDetailResponse(
    val id: Long,
    val name: String,
    val description: String,
    val price: Double,
    val mrp: Double,
    val discountPercentage: Double,
    val imageUrls: List<String>,
    val categoryId: Long,
    val categoryName: String,
    val stock: Int,
    val rating: Double,
    val reviewsCount: Int
)
