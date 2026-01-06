package com.apnadukan.catalog.service

import com.apnadukan.catalog.dto.response.ProductDetailResponse
import com.apnadukan.catalog.dto.response.ProductResponse
import com.apnadukan.catalog.model.Product
import com.apnadukan.catalog.repository.CategoryRepository
import com.apnadukan.catalog.repository.ProductRepository
import jakarta.annotation.PostConstruct
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class ProductService(
    private val productRepository: ProductRepository,
    private val categoryRepository: CategoryRepository
) {

    @PostConstruct
    fun initData() {
        if (productRepository.count() == 0L) {
            val categories = categoryRepository.findAll().associateBy { it.name }
            val fashion = categories["Fashion"]
            val tech = categories["Tech"]
            val home = categories["Home"]
            val beauty = categories["Beauty"]
            val sports = categories["Sports"]

            val dummyProducts = listOf(
                Product(name = "Wireless Headphones", description = "High-quality wireless headphones with noise cancellation.", price = BigDecimal(1999.00), mrp = BigDecimal(2499.00), discountPercentage = 20.0, imageUrl = "https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=800&h=800&fit=crop", rating = 4.5, stockQuantity = 10, category = tech),
                Product(name = "Smart Watch", description = "Elegant smart watch with heart rate monitor and GPS.", price = BigDecimal(2999.00), mrp = BigDecimal(3499.00), discountPercentage = 14.3, imageUrl = "https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=800&h=800&fit=crop", rating = 4.7, stockQuantity = 5, category = tech),
                Product(name = "Laptop Stand", description = "Ergonomic aluminum laptop stand for better posture.", price = BigDecimal(499.00), mrp = BigDecimal(599.00), discountPercentage = 16.7, imageUrl = "https://images.unsplash.com/photo-1527864550417-7fd91fc51a46?w=800&h=800&fit=crop", rating = 4.3, stockQuantity = 20, category = tech),
                Product(name = "Wireless Earbuds Pro", description = "Noise isolating earbuds with long battery life.", price = BigDecimal(3499.00), mrp = BigDecimal(4299.00), discountPercentage = 18.6, imageUrl = "https://images.unsplash.com/photo-1518444065438-402ad0bd7afa?w=800&h=800&fit=crop", rating = 4.6, stockQuantity = 16, category = tech),
                Product(name = "Running Shoes", description = "Lightweight running shoes for maximum comfort.", price = BigDecimal(2499.00), mrp = BigDecimal(4999.00), discountPercentage = 50.0, imageUrl = "https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=800&h=800&fit=crop", rating = 4.8, stockQuantity = 15, category = sports),
                Product(name = "Cotton T-Shirt", description = "Soft and breathable 100% cotton t-shirt.", price = BigDecimal(799.00), mrp = BigDecimal(1299.00), discountPercentage = 38.5, imageUrl = "https://images.unsplash.com/photo-1521572163474-6864f9cf17ab?w=800&h=800&fit=crop", rating = 4.2, stockQuantity = 50, category = fashion),
                Product(name = "Silk Scarf", description = "Premium silk scarf with elegant patterns.", price = BigDecimal(1599.00), mrp = BigDecimal(2199.00), discountPercentage = 27.3, imageUrl = "https://images.unsplash.com/photo-1509631179647-0177331693ae?w=800&h=800&fit=crop", rating = 4.4, stockQuantity = 25, category = fashion),
                Product(name = "Coffee Maker", description = "Quick brew coffee maker for your morning energy.", price = BigDecimal(3999.00), mrp = BigDecimal(4999.00), discountPercentage = 20.0, imageUrl = "https://images.unsplash.com/photo-1517668808822-9ebb02f2a0e6?w=800&h=800&fit=crop", rating = 4.6, stockQuantity = 8, category = home),
                Product(name = "Face Cream", description = "Hydrating face cream with natural ingredients.", price = BigDecimal(899.00), mrp = BigDecimal(1199.00), discountPercentage = 25.0, imageUrl = "https://images.unsplash.com/photo-1556228720-195a672e8a03?w=800&h=800&fit=crop", rating = 4.4, stockQuantity = 30, category = beauty),
                Product(name = "Vitamin C Serum", description = "Brightening serum with stabilized vitamin C.", price = BigDecimal(1299.00), mrp = BigDecimal(1699.00), discountPercentage = 23.5, imageUrl = "https://images.unsplash.com/photo-1522335789203-aabd1fc54bc9?w=800&h=800&fit=crop", rating = 4.5, stockQuantity = 40, category = beauty),
                Product(name = "Yoga Mat", description = "Non-slip yoga mat for your daily exercise.", price = BigDecimal(1299.00), mrp = BigDecimal(1999.00), discountPercentage = 35.0, imageUrl = "https://images.unsplash.com/photo-1592432678016-e910b452f9a2?w=800&h=800&fit=crop", rating = 4.5, stockQuantity = 25, category = sports),
                Product(name = "Sports Water Bottle", description = "BPA-free insulated bottle keeps drinks cold.", price = BigDecimal(699.00), mrp = BigDecimal(999.00), discountPercentage = 30.0, imageUrl = "https://images.unsplash.com/photo-1502741126161-b048400d0832?w=800&h=800&fit=crop", rating = 4.3, stockQuantity = 60, category = sports),
                Product(name = "Desk Lamp", description = "Modern LED desk lamp with adjustable brightness.", price = BigDecimal(1499.00), mrp = BigDecimal(1999.00), discountPercentage = 25.0, imageUrl = "https://images.unsplash.com/photo-1534073828943-f801091bb18c?w=800&h=800&fit=crop", rating = 4.3, stockQuantity = 12, category = home),
                Product(name = "Aroma Diffuser", description = "Ultrasonic diffuser for essential oils.", price = BigDecimal(1799.00), mrp = BigDecimal(2299.00), discountPercentage = 21.8, imageUrl = "https://images.unsplash.com/photo-1524592094714-0f0654e20314?w=800&h=800&fit=crop", rating = 4.4, stockQuantity = 18, category = home),
                Product(name = "Bluetooth Speaker", description = "Portable bluetooth speaker with extra bass.", price = BigDecimal(2499.00), mrp = BigDecimal(3499.00), discountPercentage = 28.6, imageUrl = "https://images.unsplash.com/photo-1608156639585-b3a032ef9689?w=800&h=800&fit=crop", rating = 4.7, stockQuantity = 18, category = tech),
                Product(name = "Leather Wallet", description = "Premium leather wallet with multiple card slots.", price = BigDecimal(1299.00), mrp = BigDecimal(1999.00), discountPercentage = 35.0, imageUrl = "https://images.unsplash.com/photo-1627123424574-724758594e93?w=800&h=800&fit=crop", rating = 4.5, stockQuantity = 40, category = fashion),
                Product(name = "Backpack", description = "Durable water-resistant backpack for travel and work.", price = BigDecimal(3499.00), mrp = BigDecimal(4999.00), discountPercentage = 30.0, imageUrl = "https://images.unsplash.com/photo-1553062407-98eb64c6a62?w=800&h=800&fit=crop", rating = 4.6, stockQuantity = 20, category = fashion),
                Product(name = "Sunscreen SPF 50", description = "High protection sunscreen for all skin types.", price = BigDecimal(599.00), mrp = BigDecimal(799.00), discountPercentage = 25.0, imageUrl = "https://images.unsplash.com/photo-1526947425960-945c6e72858f?w=800&h=800&fit=crop", rating = 4.4, stockQuantity = 100, category = beauty),
                Product(name = "Electric Kettle", description = "Fast boiling electric kettle with auto shut-off.", price = BigDecimal(1899.00), mrp = BigDecimal(2499.00), discountPercentage = 24.0, imageUrl = "https://images.unsplash.com/photo-1594212699903-ec8a3ecc50f1?w=800&h=800&fit=crop", rating = 4.1, stockQuantity = 15, category = home),
                Product(name = "Mechanic Keyboard", description = "RGB backlit mechanical keyboard with blue switches.", price = BigDecimal(4999.00), mrp = BigDecimal(6999.00), discountPercentage = 28.6, imageUrl = "https://images.unsplash.com/photo-1511467687858-23d96c32e4ae?w=800&h=800&fit=crop", rating = 4.9, stockQuantity = 5, category = tech),
                Product(name = "Dumbbell Set", description = "Adjustable dumbbell set for home gym workouts.", price = BigDecimal(2999.00), mrp = BigDecimal(4499.00), discountPercentage = 33.3, imageUrl = "https://images.unsplash.com/photo-1638536532686-d610adfc8e5c?w=800&h=800&fit=crop", rating = 4.7, stockQuantity = 10, category = sports),
                Product(name = "E-Reader", description = "Lightweight e-reader with glare-free display.", price = BigDecimal(8999.00), mrp = BigDecimal(10999.00), discountPercentage = 18.2, imageUrl = "https://images.unsplash.com/photo-1589998059171-988d887df646?w=800&h=800&fit=crop", rating = 4.8, stockQuantity = 12, category = tech),
                Product(name = "Ceramic Bowl Set", description = "Beautifully crafted ceramic bowl set of 4.", price = BigDecimal(1499.00), mrp = BigDecimal(1999.00), discountPercentage = 25.0, imageUrl = "https://images.unsplash.com/photo-1610832958506-aa56368176cf?w=800&h=800&fit=crop", rating = 4.3, stockQuantity = 22, category = home)
            )
            productRepository.saveAll(dummyProducts)
        }
    }

    fun getAllProducts(): List<ProductResponse> {
        return productRepository.findAll().map { it.toResponse() }
    }

    fun getProductById(productId: Long): ProductDetailResponse {
        val product = productRepository.findById(productId)
            .orElseThrow { RuntimeException("Product not found with id: $productId") }
        return product.toDetailResponse()
    }

    fun getRelatedProducts(productId: Long, limit: Int = 10): List<ProductResponse> {
        val product = productRepository.findById(productId)
            .orElseThrow { RuntimeException("Product not found with id: $productId") }
        val categoryId = product.category?.id

        val relatedProducts = if (categoryId != null) {
            // Prefer products from the same category and exclude the current one
            productRepository.findTop10ByCategoryIdAndIdNot(categoryId, productId)
        } else {
            // Fallback to active products when category is missing
            productRepository.findByIsActiveTrue().filter { it.id != productId }
        }

        return relatedProducts
            .asSequence()
            .take(limit)
            .map { it.toResponse() }
            .toList()
    }

    fun searchProducts(keyword: String): List<ProductResponse> {
        return productRepository.findAll()
            .filter { it.name.contains(keyword, ignoreCase = true) || it.description?.contains(keyword, ignoreCase = true) == true }
            .map { it.toResponse() }
    }

    fun filterProducts(minPrice: Double?, maxPrice: Double?, minRating: Double?): List<ProductResponse> {
        return productRepository.findAll()
            .filter { product ->
                (minPrice == null || product.price.toDouble() >= minPrice) &&
                (maxPrice == null || product.price.toDouble() <= maxPrice) &&
                (minRating == null || product.rating >= minRating)
            }
            .map { it.toResponse() }
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

    private fun Product.toDetailResponse() = ProductDetailResponse(
        id = this.id ?: 0,
        name = this.name,
        description = this.description ?: "",
        price = this.price.toDouble(),
        mrp = this.mrp.toDouble(),
        discountPercentage = this.discountPercentage,
        imageUrls = listOf(this.imageUrl ?: ""),
        categoryId = this.category?.id ?: 0,
        categoryName = this.category?.name ?: "Uncategorized",
        stock = this.stockQuantity,
        rating = this.rating,
        reviewsCount = (this.id?.toInt() ?: 0) * 15 + 50 // Mocking review count
    )
}
