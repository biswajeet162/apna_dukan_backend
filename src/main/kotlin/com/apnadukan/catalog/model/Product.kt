package com.apnadukan.catalog.model

import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
@Table(name = "products")
data class Product(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    var name: String = "",

    @Column(columnDefinition = "TEXT")
    var description: String? = null,

    @Column(nullable = false)
    var price: BigDecimal = BigDecimal.ZERO,

    @Column(nullable = false)
    var mrp: BigDecimal = BigDecimal.ZERO,

    @Column(name = "discount_percentage")
    var discountPercentage: Double = 0.0,

    @Column(name = "image_url")
    var imageUrl: String? = null,

    var rating: Double = 0.0,

    @Column(name = "stock_quantity")
    var stockQuantity: Int = 0,

    @Column(name = "is_active")
    var isActive: Boolean = true,

    @Column(name = "created_at")
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "updated_at")
    var updatedAt: LocalDateTime = LocalDateTime.now()
) {
    val stockAvailable: Boolean
        get() = stockQuantity > 0
}
