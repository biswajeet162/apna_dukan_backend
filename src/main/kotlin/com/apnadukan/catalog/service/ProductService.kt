package com.apnadukan.catalog.service

import com.apnadukan.catalog.dto.response.ProductResponse
import com.apnadukan.catalog.model.Product
import com.apnadukan.catalog.repository.ProductRepository
import jakarta.annotation.PostConstruct
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class ProductService(private val productRepository: ProductRepository) {

    @PostConstruct
    fun initData() {
        if (productRepository.count() == 0L) {
            val dummyProducts = listOf(
                Product(name = "Wireless Headphones", price = BigDecimal(1999.00), mrp = BigDecimal(2499.00), discountPercentage = 20.0, imageUrl = "https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=800&h=800&fit=crop", rating = 4.5, stockQuantity = 10),
                Product(name = "Smart Watch", price = BigDecimal(2999.00), mrp = BigDecimal(3499.00), discountPercentage = 14.3, imageUrl = "https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=800&h=800&fit=crop", rating = 4.7, stockQuantity = 5),
                Product(name = "Laptop Stand", price = BigDecimal(499.00), mrp = BigDecimal(599.00), discountPercentage = 16.7, imageUrl = "https://images.unsplash.com/photo-1527864550417-7fd91fc51a46?w=800&h=800&fit=crop", rating = 4.3, stockQuantity = 20)
            )
            productRepository.saveAll(dummyProducts)
        }
    }

    fun getAllProducts(): List<ProductResponse> {
        return productRepository.findAll().map { it.toResponse() }
    }

    private fun Product.toResponse() = ProductResponse(
        id = this.id ?: 0,
        name = this.name,
        price = this.price.toDouble(),
        mrp = this.mrp.toDouble(),
        discountPercentage = this.discountPercentage,
        imageUrl = this.imageUrl ?: "",
        rating = this.rating,
        stockAvailable = this.stockAvailable
    )
}
