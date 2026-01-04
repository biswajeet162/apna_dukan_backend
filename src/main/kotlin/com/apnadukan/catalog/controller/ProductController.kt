package com.apnadukan.catalog.controller

import com.apnadukan.auth.dto.response.ApiResponse
import com.apnadukan.catalog.dto.response.ProductResponse
import com.apnadukan.catalog.service.ProductService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/products")
class ProductController(private val productService: ProductService) {

    @GetMapping
    fun getAllProducts(): ResponseEntity<ApiResponse<List<ProductResponse>>> {
        val products = productService.getAllProducts()
        return ResponseEntity.ok(
            ApiResponse(
                success = true,
                message = "Products retrieved successfully",
                data = products
            )
        )
    }
}
