package com.kanowa.service

import com.kanowa.dto.CreateCreditRequest
import com.kanowa.dto.CreditResponse
import com.kanowa.dto.UpdateCreditRequest
import com.kanowa.entity.CreditEntity
import com.kanowa.repository.CreditRepository
import com.kanowa.repository.UserRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Service
class CreditService(
    private val creditRepository: CreditRepository,
    private val userRepository: UserRepository
) {
    private val dateFormatter = DateTimeFormatter.ofPattern("dd 'de' MMM, yyyy", Locale("pt", "BR"))

    @Transactional(readOnly = true)
    fun getAllCredits(userId: Long? = null): List<CreditResponse> {
        val list = if (userId != null) {
            creditRepository.findAllByUserIdOrderByCreatedAtDesc(userId)
        } else {
            creditRepository.findAllByOrderByCreatedAtDesc()
        }
        return list.map { toResponse(it) }
    }

    @Transactional(readOnly = true)
    fun getCreditById(id: String, userId: Long? = null): CreditResponse {
        val entity = creditRepository.findById(id).orElseThrow {
            ResponseStatusException(HttpStatus.NOT_FOUND, "Crédito não encontrado com id: $id")
        }
        if (userId != null && entity.user?.id != userId) {
            throw ResponseStatusException(HttpStatus.FORBIDDEN, "Acesso negado a este crédito.")
        }
        return toResponse(entity)
    }

    @Transactional
    fun createCredit(request: CreateCreditRequest, targetUserId: Long? = null): CreditResponse {
        val resolvedUserId = targetUserId ?: request.userId
        val user = if (resolvedUserId != null) {
            userRepository.findById(resolvedUserId).orElseThrow {
                ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado com id: $resolvedUserId")
            }
        } else {
            userRepository.findAll().firstOrNull() ?: throw ResponseStatusException(
                HttpStatus.BAD_REQUEST, "Nenhum usuário cadastrado no sistema. Crie um usuário antes de adicionar créditos."
            )
        }

        val dateStr = request.date ?: LocalDate.now().format(dateFormatter)
        val entity = CreditEntity(
            user = user,
            title = request.title.trim(),
            subtitle = request.subtitle ?: "Recebimento Corporativo",
            amount = request.amount,
            status = request.status ?: "CONCLUÍDO",
            frequency = request.frequency ?: "Mensal",
            category = request.category ?: "Receita Operacional",
            iconType = request.iconType ?: "CORPORATE",
            date = dateStr
        )
        val saved = creditRepository.save(entity)
        return toResponse(saved)
    }

    @Transactional
    fun updateCredit(id: String, request: UpdateCreditRequest, userId: Long? = null): CreditResponse {
        val entity = creditRepository.findById(id).orElseThrow {
            ResponseStatusException(HttpStatus.NOT_FOUND, "Crédito não encontrado com id: $id")
        }
        if (userId != null && entity.user?.id != userId) {
            throw ResponseStatusException(HttpStatus.FORBIDDEN, "Acesso negado a este crédito.")
        }

        request.title?.let { entity.title = it.trim() }
        request.amount?.let { entity.amount = it }
        request.frequency?.let { entity.frequency = it }
        request.subtitle?.let { entity.subtitle = it }
        request.status?.let { entity.status = it }
        request.category?.let { entity.category = it }
        request.iconType?.let { entity.iconType = it }
        request.date?.let { entity.date = it }

        val updated = creditRepository.save(entity)
        return toResponse(updated)
    }

    @Transactional
    fun deleteCredit(id: String, userId: Long? = null) {
        val entity = creditRepository.findById(id).orElseThrow {
            ResponseStatusException(HttpStatus.NOT_FOUND, "Crédito não encontrado com id: $id")
        }
        if (userId != null && entity.user?.id != userId) {
            throw ResponseStatusException(HttpStatus.FORBIDDEN, "Acesso negado a este crédito.")
        }
        creditRepository.delete(entity)
    }

    @Transactional(readOnly = true)
    fun getTotalMonthlyCredits(userId: Long? = null): Double {
        return if (userId != null) {
            creditRepository.getTotalMonthlyCreditsByUserId(userId)
        } else {
            creditRepository.getTotalMonthlyCredits()
        }
    }

    fun toResponse(entity: CreditEntity): CreditResponse {
        return CreditResponse(
            id = entity.id,
            title = entity.title,
            subtitle = entity.subtitle,
            amount = entity.amount,
            status = entity.status,
            frequency = entity.frequency,
            category = entity.category,
            iconType = entity.iconType,
            date = entity.date,
            userId = entity.user?.id
        )
    }
}
