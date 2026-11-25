package com.kanowa.service

import com.kanowa.dto.CreateUserRequest
import com.kanowa.dto.UserResponse
import com.kanowa.entity.UserEntity
import com.kanowa.repository.UserRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import java.time.format.DateTimeFormatter

@Service
class UserService(
    private val userRepository: UserRepository
) {
    private val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")

    @Transactional
    fun createUser(request: CreateUserRequest): UserResponse {
        val email = request.email.trim().lowercase()
        if (userRepository.existsByEmail(email)) {
            throw ResponseStatusException(HttpStatus.CONFLICT, "E-mail já cadastrado: $email")
        }

        val entity = UserEntity(
            name = request.name.trim(),
            email = email,
            password = request.password
        )
        val saved = userRepository.save(entity)
        return toResponse(saved)
    }

    @Transactional(readOnly = true)
    fun getUserById(id: Long): UserResponse {
        val user = userRepository.findById(id).orElseThrow {
            ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado com id: $id")
        }
        return toResponse(user)
    }

    @Transactional(readOnly = true)
    fun getUserByEmail(email: String): UserResponse {
        val user = userRepository.findByEmail(email.trim().lowercase()) ?: throw ResponseStatusException(
            HttpStatus.NOT_FOUND, "Usuário não encontrado com e-mail: $email"
        )
        return toResponse(user)
    }

    @Transactional(readOnly = true)
    fun getAllUsers(): List<UserResponse> {
        return userRepository.findAll().map { toResponse(it) }
    }

    fun toResponse(entity: UserEntity): UserResponse {
        return UserResponse(
            id = entity.id ?: 0L,
            name = entity.name,
            email = entity.email,
            createdAt = entity.createdAt.format(dateFormatter)
        )
    }
}
