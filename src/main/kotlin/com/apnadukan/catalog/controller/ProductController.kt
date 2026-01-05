package com.apnadukan.catalog.controller

import com.apnadukan.auth.dto.response.ApiResponse
import com.apnadukan.catalog.dto.response.ProductDetailResponse
import com.apnadukan.catalog.dto.response.ProductResponse
import com.apnadukan.catalog.service.ProductService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
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

    @GetMapping("/{id}")
    fun getProductById(@PathVariable id: Long): ResponseEntity<ApiResponse<ProductDetailResponse>> {
        val product = productService.getProductById(id)
        return ResponseEntity.ok(
            ApiResponse(
                success = true,
                message = "Product details retrieved successfully",
                data = product
            )
        )
    }

    @GetMapping("/search")
    fun searchProducts(@RequestParam keyword: String): ResponseEntity<ApiResponse<List<ProductResponse>>> {
        val products = productService.searchProducts(keyword)
        return ResponseEntity.ok(
            ApiResponse(
                success = true,
                message = "Search results retrieved successfully",
                data = products
            )
        )
    }

    @GetMapping("/filter")
    fun filterProducts(
        @RequestParam(required = false) minPrice: Double?,
        @RequestParam(required = false) maxPrice: Double?,
        @RequestParam(required = false) minRating: Double?
    ): ResponseEntity<ApiResponse<List<ProductResponse>>> {
        val products = productService.filterProducts(minPrice, maxPrice, minRating)
        return ResponseEntity.ok(
            ApiResponse(
                success = true,
                message = "Filtered products retrieved successfully",
                data = products
            )
        )
    }
}
