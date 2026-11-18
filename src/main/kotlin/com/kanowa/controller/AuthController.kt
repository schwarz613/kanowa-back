package com.kanowa.controller

import com.kanowa.dto.CreateUserRequest
import com.kanowa.dto.LoginRequest
import com.kanowa.dto.LoginResponse
import com.kanowa.dto.UserResponse
import com.kanowa.service.AuthService
import com.kanowa.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = ["*"])
class AuthController(
    private val authService: AuthService,
    private val userService: UserService
) {
    @PostMapping("/register")
    fun register(@RequestBody request: CreateUserRequest): ResponseEntity<UserResponse> {
        val user = userService.createUser(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(user)
    }

    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequest): ResponseEntity<LoginResponse> {
        val response = authService.login(request)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/me")
    fun me(
        @RequestParam(required = false) email: String?,
        @RequestHeader(value = "X-User-Id", required = false) userId: Long?
    ): ResponseEntity<UserResponse> {
        val user = when {
            !email.isNullOrBlank() -> userService.getUserByEmail(email)
            userId != null -> userService.getUserById(userId)
            else -> throw org.springframework.web.server.ResponseStatusException(
                HttpStatus.BAD_REQUEST, "Informe o parâmetro email ou o cabeçalho X-User-Id"
            )
        }
        return ResponseEntity.ok(user)
    }
}
