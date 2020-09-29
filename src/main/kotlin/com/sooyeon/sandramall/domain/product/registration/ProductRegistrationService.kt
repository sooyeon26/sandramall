package com.sooyeon.sandramall.domain.product.registration

import com.sooyeon.sandramall.SandraException
import com.sooyeon.sandramall.domain.auth.UserContextHolder
import com.sooyeon.sandramall.domain.product.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ProductRegistrationService @Autowired constructor(
    private val productRepository: ProductRepository,
    private val productImageRepository: ProductImageRepository,
    private val userContextHolder: UserContextHolder
) {
    fun register(request: ProductRegistrationRequest) =
        userContextHolder.id?.let { userId ->
            val images by lazy { findAndValidateImages(request.imageIds) }
            request.validateRequest()
            request.toProduct(images, userId)
                .run(::save)
        } ?: throw SandraException(
            "no such user info exists. can't register the product."
        )

    private fun findAndValidateImages(imageIds: List<Long?>) =
        productImageRepository.findByIdIn(imageIds.filterNotNull())
            .also { images ->
                images.forEach { image ->
                    if (image.productId != null)
                        throw SandraException("already registered product")
                }
            }

    private fun save(product: Product) = productRepository.save(product)

}

private fun ProductRegistrationRequest.toProduct(
    images: MutableList<ProductImage>,
    userId: Long
) = Product(
    name,
    description,
    price,
    categoryId,
    ProductStatus.SELLING,
    images,
    userId
)

private fun ProductRegistrationRequest.validateRequest() = when {
    name.length !in 1..40 ||
            imageIds.size !in 1..4 ||
            imageIds.filterNotNull().isEmpty() ||
            description.length !in 1..500 ||
            price <= 0
    -> throw SandraException("incorrect product info")
    else -> {
    }


}
