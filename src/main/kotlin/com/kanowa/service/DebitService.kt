package com.kanowa.service

import com.kanowa.dto.CreateDebitRequest
import com.kanowa.dto.DebitResponse
import com.kanowa.dto.UpdateDebitRequest
import com.kanowa.entity.DebitEntity
import com.kanowa.repository.DebitRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Service
class DebitService(
    private val debitRepository: DebitRepository
) {
    private val dateFormatter = DateTimeFormatter.ofPattern("dd 'de' MMM, yyyy", Locale("pt", "BR"))

    fun getAllDebits(): List<DebitResponse> {
        return debitRepository.findAllByOrderByCreatedAtDesc().map { toResponse(it) }
    }

    fun getDebitById(id: String): DebitResponse {
        val entity = debitRepository.findById(id).orElseThrow {
            ResponseStatusException(HttpStatus.NOT_FOUND, "Débito não encontrado com id: $id")
        }
        return toResponse(entity)
    }

    fun createDebit(request: CreateDebitRequest): DebitResponse {
        val dateStr = request.dueDate ?: ("Vence em " + LocalDate.now().plusDays(10).format(dateFormatter))
        val entity = DebitEntity(
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

    fun updateDebit(id: String, request: UpdateDebitRequest): DebitResponse {
        val entity = debitRepository.findById(id).orElseThrow {
            ResponseStatusException(HttpStatus.NOT_FOUND, "Débito não encontrado com id: $id")
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

    fun deleteDebit(id: String) {
        if (!debitRepository.existsById(id)) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Débito não encontrado com id: $id")
        }
        debitRepository.deleteById(id)
    }

    fun getTotalMonthlyDebits(): Double {
        return debitRepository.getTotalMonthlyDebits()
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
            dueDate = entity.dueDate
        )
    }
}
