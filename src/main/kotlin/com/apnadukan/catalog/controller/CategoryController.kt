package com.apnadukan.catalog.controller

import com.apnadukan.auth.dto.response.ApiResponse
import com.apnadukan.catalog.dto.response.CategoryResponse
import com.apnadukan.catalog.service.CategoryService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/categories")
class CategoryController(private val categoryService: CategoryService) {

    @GetMapping
    fun getAllCategories(): ResponseEntity<ApiResponse<List<CategoryResponse>>> {
        val categories = categoryService.getAllCategories()
        return ResponseEntity.ok(
            ApiResponse(
                success = true,
                message = "Categories retrieved successfully",
                data = categories
            )
        )
    }
}
