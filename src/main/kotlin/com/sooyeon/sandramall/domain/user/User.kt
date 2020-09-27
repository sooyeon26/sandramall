package com.sooyeon.sandramall.domain.user

import com.sooyeon.sandramall.domain.jpa.BaseEntity
import java.util.*
import javax.persistence.*

@Entity(name = "user")
class User(
        var email: String,
        var password: String,
        var name: String
) : BaseEntity() {
}