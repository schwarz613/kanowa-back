package com.kanowa

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class KanowaApplication

fun main(args: Array<String>) {
    runApplication<KanowaApplication>(*args)
}
