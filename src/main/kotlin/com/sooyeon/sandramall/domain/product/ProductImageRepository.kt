package com.sooyeon.sandramall.domain.product

import org.springframework.data.jpa.repository.JpaRepository

interface ProductImageRepository : JpaRepository<ProductImage, Long> {
}