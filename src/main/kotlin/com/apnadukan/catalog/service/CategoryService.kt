package com.apnadukan.catalog.service

import com.apnadukan.catalog.dto.response.CategoryResponse
import com.apnadukan.catalog.model.Category
import com.apnadukan.catalog.repository.CategoryRepository
import jakarta.annotation.PostConstruct
import org.springframework.stereotype.Service

@Service
class CategoryService(private val categoryRepository: CategoryRepository) {

    @PostConstruct
    fun initData() {
        if (categoryRepository.count() == 0L) {
            val electronics = Category(name = "Electronics", slug = "electronics")
            val mobile = Category(name = "Mobiles", slug = "mobiles", parent = electronics)
            val laptop = Category(name = "Laptops", slug = "laptops", parent = electronics)
            
            val fashion = Category(name = "Fashion", slug = "fashion")
            val men = Category(name = "Men's Wear", slug = "mens-wear", parent = fashion)
            
            categoryRepository.saveAll(listOf(electronics, mobile, laptop, fashion, men))
        }
    }

    fun getAllCategories(): List<CategoryResponse> {
        return categoryRepository.findByParentIsNull().map { it.toResponse() }
    }

    private fun Category.toResponse(): CategoryResponse = CategoryResponse(
        id = this.id ?: 0,
        name = this.name,
        slug = this.slug ?: "",
        description = this.description,
        children = this.children.map { it.toResponse() }
    )
}
