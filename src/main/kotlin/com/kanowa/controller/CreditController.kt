package com.kanowa.controller

import com.kanowa.dto.CreateCreditRequest
import com.kanowa.dto.CreditResponse
import com.kanowa.dto.UpdateCreditRequest
import com.kanowa.service.CreditService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/credits")
@CrossOrigin(origins = ["*"])
class CreditController(
    private val creditService: CreditService
) {
    @GetMapping
    fun getAllCredits(
        @RequestHeader(value = "X-User-Id", required = false) userIdHeader: Long?,
        @RequestParam(value = "userId", required = false) userIdParam: Long?
    ): ResponseEntity<List<CreditResponse>> {
        val userId = userIdParam ?: userIdHeader
        val list = creditService.getAllCredits(userId)
        return ResponseEntity.ok(list)
    }

    @GetMapping("/{id}")
    fun getCreditById(
        @PathVariable id: String,
        @RequestHeader(value = "X-User-Id", required = false) userIdHeader: Long?,
        @RequestParam(value = "userId", required = false) userIdParam: Long?
    ): ResponseEntity<CreditResponse> {
        val userId = userIdParam ?: userIdHeader
        val credit = creditService.getCreditById(id, userId)
        return ResponseEntity.ok(credit)
    }

    @PostMapping
    fun createCredit(
        @RequestBody request: CreateCreditRequest,
        @RequestHeader(value = "X-User-Id", required = false) userIdHeader: Long?,
        @RequestParam(value = "userId", required = false) userIdParam: Long?
    ): ResponseEntity<CreditResponse> {
        val userId = userIdParam ?: userIdHeader ?: request.userId
        val created = creditService.createCredit(request, userId)
        return ResponseEntity.status(HttpStatus.CREATED).body(created)
    }

    @PutMapping("/{id}")
    fun updateCredit(
        @PathVariable id: String,
        @RequestBody request: UpdateCreditRequest,
        @RequestHeader(value = "X-User-Id", required = false) userIdHeader: Long?,
        @RequestParam(value = "userId", required = false) userIdParam: Long?
    ): ResponseEntity<CreditResponse> {
        val userId = userIdParam ?: userIdHeader
        val updated = creditService.updateCredit(id, request, userId)
        return ResponseEntity.ok(updated)
    }

    @DeleteMapping("/{id}")
    fun deleteCredit(
        @PathVariable id: String,
        @RequestHeader(value = "X-User-Id", required = false) userIdHeader: Long?,
        @RequestParam(value = "userId", required = false) userIdParam: Long?
    ): ResponseEntity<Void> {
        val userId = userIdParam ?: userIdHeader
        creditService.deleteCredit(id, userId)
        return ResponseEntity.noContent().build()
    }

    @GetMapping("/total")
    fun getTotalMonthlyCredits(
        @RequestHeader(value = "X-User-Id", required = false) userIdHeader: Long?,
        @RequestParam(value = "userId", required = false) userIdParam: Long?
    ): ResponseEntity<Map<String, Double>> {
        val userId = userIdParam ?: userIdHeader
        val total = creditService.getTotalMonthlyCredits(userId)
        return ResponseEntity.ok(mapOf("totalMonthlyCredits" to total))
    }
}
