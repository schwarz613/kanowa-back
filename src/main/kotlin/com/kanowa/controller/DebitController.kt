package com.kanowa.controller

import com.kanowa.dto.CreateDebitRequest
import com.kanowa.dto.DebitResponse
import com.kanowa.dto.UpdateDebitRequest
import com.kanowa.service.DebitService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/debits")
@CrossOrigin(origins = ["*"])
class DebitController(
    private val debitService: DebitService
) {
    @GetMapping
    fun getAllDebits(
        @RequestHeader(value = "X-User-Id", required = false) userIdHeader: Long?,
        @RequestParam(value = "userId", required = false) userIdParam: Long?
    ): ResponseEntity<List<DebitResponse>> {
        val userId = userIdParam ?: userIdHeader
        val list = debitService.getAllDebits(userId)
        return ResponseEntity.ok(list)
    }

    @GetMapping("/{id}")
    fun getDebitById(
        @PathVariable id: String,
        @RequestHeader(value = "X-User-Id", required = false) userIdHeader: Long?,
        @RequestParam(value = "userId", required = false) userIdParam: Long?
    ): ResponseEntity<DebitResponse> {
        val userId = userIdParam ?: userIdHeader
        val debit = debitService.getDebitById(id, userId)
        return ResponseEntity.ok(debit)
    }

    @PostMapping
    fun createDebit(
        @RequestBody request: CreateDebitRequest,
        @RequestHeader(value = "X-User-Id", required = false) userIdHeader: Long?,
        @RequestParam(value = "userId", required = false) userIdParam: Long?
    ): ResponseEntity<DebitResponse> {
        val userId = userIdParam ?: userIdHeader ?: request.userId
        val created = debitService.createDebit(request, userId)
        return ResponseEntity.status(HttpStatus.CREATED).body(created)
    }

    @PutMapping("/{id}")
    fun updateDebit(
        @PathVariable id: String,
        @RequestBody request: UpdateDebitRequest,
        @RequestHeader(value = "X-User-Id", required = false) userIdHeader: Long?,
        @RequestParam(value = "userId", required = false) userIdParam: Long?
    ): ResponseEntity<DebitResponse> {
        val userId = userIdParam ?: userIdHeader
        val updated = debitService.updateDebit(id, request, userId)
        return ResponseEntity.ok(updated)
    }

    @DeleteMapping("/{id}")
    fun deleteDebit(
        @PathVariable id: String,
        @RequestHeader(value = "X-User-Id", required = false) userIdHeader: Long?,
        @RequestParam(value = "userId", required = false) userIdParam: Long?
    ): ResponseEntity<Void> {
        val userId = userIdParam ?: userIdHeader
        debitService.deleteDebit(id, userId)
        return ResponseEntity.noContent().build()
    }

    @GetMapping("/total")
    fun getTotalMonthlyDebits(
        @RequestHeader(value = "X-User-Id", required = false) userIdHeader: Long?,
        @RequestParam(value = "userId", required = false) userIdParam: Long?
    ): ResponseEntity<Map<String, Double>> {
        val userId = userIdParam ?: userIdHeader
        val total = debitService.getTotalMonthlyDebits(userId)
        return ResponseEntity.ok(mapOf("totalMonthlyDebits" to total))
    }
}
