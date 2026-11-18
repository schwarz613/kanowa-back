package com.kanowa.service

import com.kanowa.dto.CreateCreditRequest
import com.kanowa.dto.CreditResponse
import com.kanowa.dto.UpdateCreditRequest
import com.kanowa.entity.CreditEntity
import com.kanowa.repository.CreditRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Service
class CreditService(
    private val creditRepository: CreditRepository
) {
    private val dateFormatter = DateTimeFormatter.ofPattern("dd 'de' MMM, yyyy", Locale("pt", "BR"))

    fun getAllCredits(): List<CreditResponse> {
        return creditRepository.findAllByOrderByCreatedAtDesc().map { toResponse(it) }
    }

    fun getCreditById(id: String): CreditResponse {
        val entity = creditRepository.findById(id).orElseThrow {
            ResponseStatusException(HttpStatus.NOT_FOUND, "Crédito não encontrado com id: $id")
        }
        return toResponse(entity)
    }

    fun createCredit(request: CreateCreditRequest): CreditResponse {
        val dateStr = request.date ?: LocalDate.now().format(dateFormatter)
        val entity = CreditEntity(
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

    fun updateCredit(id: String, request: UpdateCreditRequest): CreditResponse {
        val entity = creditRepository.findById(id).orElseThrow {
            ResponseStatusException(HttpStatus.NOT_FOUND, "Crédito não encontrado com id: $id")
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

    fun deleteCredit(id: String) {
        if (!creditRepository.existsById(id)) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Crédito não encontrado com id: $id")
        }
        creditRepository.deleteById(id)
    }

    fun getTotalMonthlyCredits(): Double {
        return creditRepository.getTotalMonthlyCredits()
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
            date = entity.date
        )
    }
}
