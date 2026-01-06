package com.apnadukan.catalog.repository

import com.apnadukan.catalog.model.Product
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProductRepository : JpaRepository<Product, Long> {
    fun findByIsActiveTrue(): List<Product>
    fun findByNameContainingIgnoreCase(name: String): List<Product>
    fun findTop10ByCategoryIdAndIdNot(categoryId: Long, id: Long): List<Product>
}
