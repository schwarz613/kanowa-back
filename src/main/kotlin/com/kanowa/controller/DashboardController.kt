package com.kanowa.controller

import com.kanowa.dto.FinancialSummaryResponse
import com.kanowa.dto.TransactionResponse
import com.kanowa.service.DashboardService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = ["*"])
class DashboardController(
    private val dashboardService: DashboardService
) {
    @GetMapping("/summary")
    fun getSummary(): ResponseEntity<FinancialSummaryResponse> {
        val summary = dashboardService.getFinancialSummary()
        return ResponseEntity.ok(summary)
    }

    @GetMapping("/transactions")
    fun getTransactions(): ResponseEntity<List<TransactionResponse>> {
        val transactions = dashboardService.getRecentTransactions()
        return ResponseEntity.ok(transactions)
    }
}
