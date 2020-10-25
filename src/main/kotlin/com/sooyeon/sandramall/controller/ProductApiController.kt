package com.sooyeon.sandramall.controller

import com.sooyeon.sandramall.ApiResponse
import com.sooyeon.sandramall.domain.product.Product
import com.sooyeon.sandramall.domain.product.ProductService
import com.sooyeon.sandramall.domain.product.registration.ProductImageService
import com.sooyeon.sandramall.domain.product.registration.ProductRegistrationRequest
import com.sooyeon.sandramall.domain.product.registration.ProductRegistrationService
import com.sooyeon.sandramall.domain.product.toProductListItemResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/v1")
class ProductApiController @Autowired constructor(
    private val productImageService: ProductImageService,
    private val productRegistration: ProductRegistrationService,
    private val productService: ProductService
) {
    @PostMapping("/product_images")
    fun uploadImage(image: MultipartFile) =
        ApiResponse.ok(
            productImageService.uploadImage(image)
        )

    @PostMapping("/products")
    fun register(
        @RequestBody request: ProductRegistrationRequest
    ) = ApiResponse.ok(productRegistration.register(request))

    @GetMapping("/products")
    fun search(
        @RequestParam productId: Long,
        @RequestParam(required=false) categoryId: Int?,
        @RequestParam direction: String,
        @RequestParam(required = false) limit: Int?
    ) = productService
        .search(categoryId, productId, direction, limit ?: 10)
        .mapNotNull(Product::toProductListItemResponse)
        .let { ApiResponse.ok(it) }

}