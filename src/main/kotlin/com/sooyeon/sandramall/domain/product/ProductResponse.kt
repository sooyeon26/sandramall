package com.sooyeon.sandramall.domain.product

import com.sooyeon.sandramall.SandraException

data class ProductResponse(
    val id: Long,
    val name: String,
    val description: String,
    val price: Int,
    val status: String,
    val sellerId: Long,
    val imagePaths: List<String>
)

fun Product.toProductResponse() = id?.let {
    ProductResponse(
        it,
        name,
        description,
        price,
        status.name,
        userId,
        images.map { it.path }
    )
} ?: throw SandraException("cannot find product info")