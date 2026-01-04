package com.apnadukan.catalog.repository

import com.apnadukan.catalog.model.Category
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CategoryRepository : JpaRepository<Category, Long> {
    fun findByParentIsNull(): List<Category>
}
