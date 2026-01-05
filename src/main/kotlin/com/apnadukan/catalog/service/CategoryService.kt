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
            val fashion = Category(name = "Fashion", slug = "fashion")
            val tech = Category(name = "Tech", slug = "tech")
            val home = Category(name = "Home", slug = "home")
            val beauty = Category(name = "Beauty", slug = "beauty")
            val sports = Category(name = "Sports", slug = "sports")
            
            categoryRepository.saveAll(listOf(fashion, tech, home, beauty, sports))
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
