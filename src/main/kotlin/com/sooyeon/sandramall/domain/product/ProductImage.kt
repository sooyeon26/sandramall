package com.sooyeon.sandramall.domain.product

import com.sooyeon.sandramall.domain.jpa.BaseEntity
import javax.persistence.*
import java.util.*

@Entity(name = "product_image")
class ProductImage(
        var path: String,
        var productId: Long? = null
) : BaseEntity() {
}