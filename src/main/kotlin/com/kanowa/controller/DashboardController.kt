package com.kanowa.controller

import com.kanowa.dto.FinancialSummaryResponse
import com.kanowa.dto.TransactionResponse
import com.kanowa.service.DashboardService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = ["*"])
class DashboardController(
    private val dashboardService: DashboardService
) {
    @GetMapping("/summary")
    fun getSummary(
        @RequestHeader(value = "X-User-Id", required = false) userIdHeader: Long?,
        @RequestParam(value = "userId", required = false) userIdParam: Long?
    ): ResponseEntity<FinancialSummaryResponse> {
        val userId = userIdParam ?: userIdHeader
        val summary = dashboardService.getFinancialSummary(userId)
        return ResponseEntity.ok(summary)
    }

    @GetMapping("/transactions")
    fun getTransactions(
        @RequestHeader(value = "X-User-Id", required = false) userIdHeader: Long?,
        @RequestParam(value = "userId", required = false) userIdParam: Long?
    ): ResponseEntity<List<TransactionResponse>> {
        val userId = userIdParam ?: userIdHeader
        val transactions = dashboardService.getRecentTransactions(userId)
        return ResponseEntity.ok(transactions)
    }
}
