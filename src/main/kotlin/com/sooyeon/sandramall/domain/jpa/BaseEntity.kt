package com.sooyeon.sandramall.domain.jpa

import javax.persistence.*
import java.util.*

@MappedSuperclass
abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open var id: Long? = null

    open var createdAt: Date? = null
    open var updatedAt: Date? = null

    @PrePersist
    fun prePersist() {
        createdAt = Date()
        updatedAt = Date()
    }

    @PreUpdate
    fun preUpdate() {
        updatedAt = Date()
    }
}