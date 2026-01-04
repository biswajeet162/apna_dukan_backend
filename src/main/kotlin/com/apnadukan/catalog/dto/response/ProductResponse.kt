package com.apnadukan.catalog.dto.response

data class ProductResponse(
    val id: Long,
    val name: String,
    val price: Double,
    val mrp: Double,
    val discountPercentage: Double,
    val imageUrl: String,
    val rating: Double,
    val stockAvailable: Boolean
)
