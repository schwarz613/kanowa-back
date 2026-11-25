package com.kanowa.service

import com.kanowa.dto.LoginRequest
import com.kanowa.dto.LoginResponse
import com.kanowa.repository.UserRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import java.util.UUID

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val userService: UserService
) {
    @Transactional(readOnly = true)
    fun login(request: LoginRequest): LoginResponse {
        val email = request.email.trim().lowercase()
        val user = userRepository.findByEmail(email)
            ?: throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciais inválidas. Verifique seu e-mail e senha.")

        if (user.password != request.password) {
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciais inválidas. Verifique seu e-mail e senha.")
        }

        val token = "kanowa-jwt-" + UUID.randomUUID().toString()

        return LoginResponse(
            success = true,
            message = "Login realizado com sucesso!",
            user = userService.toResponse(user),
            token = token
        )
    }
}
