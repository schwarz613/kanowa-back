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
    fun getAllDebits(): ResponseEntity<List<DebitResponse>> {
        val list = debitService.getAllDebits()
        return ResponseEntity.ok(list)
    }

    @GetMapping("/{id}")
    fun getDebitById(@PathVariable id: String): ResponseEntity<DebitResponse> {
        val debit = debitService.getDebitById(id)
        return ResponseEntity.ok(debit)
    }

    @PostMapping
    fun createDebit(@RequestBody request: CreateDebitRequest): ResponseEntity<DebitResponse> {
        val created = debitService.createDebit(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(created)
    }

    @PutMapping("/{id}")
    fun updateDebit(
        @PathVariable id: String,
        @RequestBody request: UpdateDebitRequest
    ): ResponseEntity<DebitResponse> {
        val updated = debitService.updateDebit(id, request)
        return ResponseEntity.ok(updated)
    }

    @DeleteMapping("/{id}")
    fun deleteDebit(@PathVariable id: String): ResponseEntity<Void> {
        debitService.deleteDebit(id)
        return ResponseEntity.noContent().build()
    }

    @GetMapping("/total")
    fun getTotalMonthlyDebits(): ResponseEntity<Map<String, Double>> {
        val total = debitService.getTotalMonthlyDebits()
        return ResponseEntity.ok(mapOf("totalMonthlyDebits" to total))
    }
}
