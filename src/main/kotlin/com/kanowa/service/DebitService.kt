package com.kanowa.service

import com.kanowa.dto.CreateDebitRequest
import com.kanowa.dto.DebitResponse
import com.kanowa.dto.UpdateDebitRequest
import com.kanowa.entity.DebitEntity
import com.kanowa.repository.DebitRepository
import com.kanowa.repository.UserRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Service
class DebitService(
    private val debitRepository: DebitRepository,
    private val userRepository: UserRepository
) {
    private val dateFormatter = DateTimeFormatter.ofPattern("dd 'de' MMM, yyyy", Locale("pt", "BR"))

    @Transactional(readOnly = true)
    fun getAllDebits(userId: Long? = null): List<DebitResponse> {
        val list = if (userId != null) {
            debitRepository.findAllByUserIdOrderByCreatedAtDesc(userId)
        } else {
            debitRepository.findAllByOrderByCreatedAtDesc()
        }
        return list.map { toResponse(it) }
    }

    @Transactional(readOnly = true)
    fun getDebitById(id: String, userId: Long? = null): DebitResponse {
        val entity = debitRepository.findById(id).orElseThrow {
            ResponseStatusException(HttpStatus.NOT_FOUND, "Débito não encontrado com id: $id")
        }
        if (userId != null && entity.user?.id != userId) {
            throw ResponseStatusException(HttpStatus.FORBIDDEN, "Acesso negado a este débito.")
        }
        return toResponse(entity)
    }

    @Transactional
    fun createDebit(request: CreateDebitRequest, targetUserId: Long? = null): DebitResponse {
        val resolvedUserId = targetUserId ?: request.userId
        val user = if (resolvedUserId != null) {
            userRepository.findById(resolvedUserId).orElseThrow {
                ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado com id: $resolvedUserId")
            }
        } else {
            userRepository.findAll().firstOrNull() ?: throw ResponseStatusException(
                HttpStatus.BAD_REQUEST, "Nenhum usuário cadastrado no sistema. Crie um usuário antes de adicionar débitos."
            )
        }

        val dateStr = request.dueDate ?: ("Vence em " + LocalDate.now().plusDays(10).format(dateFormatter))
        val entity = DebitEntity(
            user = user,
            title = request.title.trim(),
            subtitle = request.subtitle ?: "Vencimento Programado",
            amount = request.amount,
            status = request.status ?: "PENDENTE",
            isAlert = request.isAlert ?: false,
            category = request.category ?: "Geral",
            iconType = request.iconType ?: "HOME",
            dueDate = dateStr
        )
        val saved = debitRepository.save(entity)
        return toResponse(saved)
    }

    @Transactional
    fun updateDebit(id: String, request: UpdateDebitRequest, userId: Long? = null): DebitResponse {
        val entity = debitRepository.findById(id).orElseThrow {
            ResponseStatusException(HttpStatus.NOT_FOUND, "Débito não encontrado com id: $id")
        }
        if (userId != null && entity.user?.id != userId) {
            throw ResponseStatusException(HttpStatus.FORBIDDEN, "Acesso negado a este débito.")
        }

        request.title?.let { entity.title = it.trim() }
        request.amount?.let { entity.amount = it }
        request.category?.let { entity.category = it }
        request.subtitle?.let { entity.subtitle = it }
        request.status?.let { entity.status = it }
        request.isAlert?.let { entity.isAlert = it }
        request.iconType?.let { entity.iconType = it }
        request.dueDate?.let { entity.dueDate = it }

        val updated = debitRepository.save(entity)
        return toResponse(updated)
    }

    @Transactional
    fun deleteDebit(id: String, userId: Long? = null) {
        val entity = debitRepository.findById(id).orElseThrow {
            ResponseStatusException(HttpStatus.NOT_FOUND, "Débito não encontrado com id: $id")
        }
        if (userId != null && entity.user?.id != userId) {
            throw ResponseStatusException(HttpStatus.FORBIDDEN, "Acesso negado a este débito.")
        }
        debitRepository.delete(entity)
    }

    @Transactional(readOnly = true)
    fun getTotalMonthlyDebits(userId: Long? = null): Double {
        return if (userId != null) {
            debitRepository.getTotalMonthlyDebitsByUserId(userId)
        } else {
            debitRepository.getTotalMonthlyDebits()
        }
    }

    fun toResponse(entity: DebitEntity): DebitResponse {
        return DebitResponse(
            id = entity.id,
            title = entity.title,
            subtitle = entity.subtitle,
            amount = entity.amount,
            status = entity.status,
            isAlert = entity.isAlert,
            category = entity.category,
            iconType = entity.iconType,
            dueDate = entity.dueDate,
            userId = entity.user?.id
        )
    }
}
