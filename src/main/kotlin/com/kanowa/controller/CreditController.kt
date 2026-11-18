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
    fun getAllCredits(): ResponseEntity<List<CreditResponse>> {
        val list = creditService.getAllCredits()
        return ResponseEntity.ok(list)
    }

    @GetMapping("/{id}")
    fun getCreditById(@PathVariable id: String): ResponseEntity<CreditResponse> {
        val credit = creditService.getCreditById(id)
        return ResponseEntity.ok(credit)
    }

    @PostMapping
    fun createCredit(@RequestBody request: CreateCreditRequest): ResponseEntity<CreditResponse> {
        val created = creditService.createCredit(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(created)
    }

    @PutMapping("/{id}")
    fun updateCredit(
        @PathVariable id: String,
        @RequestBody request: UpdateCreditRequest
    ): ResponseEntity<CreditResponse> {
        val updated = creditService.updateCredit(id, request)
        return ResponseEntity.ok(updated)
    }

    @DeleteMapping("/{id}")
    fun deleteCredit(@PathVariable id: String): ResponseEntity<Void> {
        creditService.deleteCredit(id)
        return ResponseEntity.noContent().build()
    }

    @GetMapping("/total")
    fun getTotalMonthlyCredits(): ResponseEntity<Map<String, Double>> {
        val total = creditService.getTotalMonthlyCredits()
        return ResponseEntity.ok(mapOf("totalMonthlyCredits" to total))
    }
}
