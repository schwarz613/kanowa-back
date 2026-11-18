package com.kanowa.controller

import com.kanowa.dto.CreateUserRequest
import com.kanowa.dto.UserResponse
import com.kanowa.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = ["*"])
class UserController(
    private val userService: UserService
) {
    @PostMapping
    fun createUser(@RequestBody request: CreateUserRequest): ResponseEntity<UserResponse> {
        val created = userService.createUser(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(created)
    }

    @GetMapping("/{id}")
    fun getUserById(@PathVariable id: Long): ResponseEntity<UserResponse> {
        val user = userService.getUserById(id)
        return ResponseEntity.ok(user)
    }

    @GetMapping
    fun getUsers(@RequestParam(required = false) email: String?): ResponseEntity<Any> {
        return if (email != null) {
            val user = userService.getUserByEmail(email)
            ResponseEntity.ok(user)
        } else {
            val users = userService.getAllUsers()
            ResponseEntity.ok(users)
        }
    }
}
