package com.apnadukan.catalog.dto.response

data class CategoryResponse(
    val id: Long,
    val name: String,
    val slug: String,
    val description: String?,
    val children: List<CategoryResponse> = emptyList()
)
