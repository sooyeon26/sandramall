package com.sooyeon.sandramall

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication as runApplication

@SpringBootApplication
open class SandraApplication
    fun main(){
        runApplication<SandraApplication>()
    }
