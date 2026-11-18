package com.kanowa.service

import com.kanowa.dto.FinancialSummaryResponse
import com.kanowa.dto.TransactionResponse
import com.kanowa.repository.CreditRepository
import com.kanowa.repository.DebitRepository
import org.springframework.stereotype.Service
import java.time.format.DateTimeFormatter
import java.util.Locale

@Service
class DashboardService(
    private val creditRepository: CreditRepository,
    private val debitRepository: DebitRepository
) {
    private val dateFormatter = DateTimeFormatter.ofPattern("dd 'de' MMM, yyyy", Locale("pt", "BR"))

    fun getFinancialSummary(): FinancialSummaryResponse {
        val totalRevenue = creditRepository.getTotalMonthlyCredits()
        val totalExpenses = debitRepository.getTotalMonthlyDebits()
        val baseCapital = 142500.00
        val totalBalance = baseCapital + totalRevenue - totalExpenses

        val savingsRate = if (totalRevenue > 0) {
            ((totalRevenue - totalExpenses) / totalRevenue * 100).coerceAtLeast(0.0)
        } else {
            0.0
        }

        val activeDebitsCount = debitRepository.countByStatusNot("PAGO")

        return FinancialSummaryResponse(
            totalBalance = totalBalance,
            balanceGrowthPercentage = 4.2,
            monthlyRevenue = if (totalRevenue > 0) totalRevenue else 12840.00,
            revenueGrowthPercentage = 12.0,
            monthlyExpenses = if (totalExpenses > 0) totalExpenses else 4320.00,
            expensesReductionPercentage = 2.4,
            savingsRatePercentage = if (savingsRate > 0) savingsRate else 68.4,
            activeDebitsCount = if (activeDebitsCount > 0) activeDebitsCount else 4
        )
    }

    fun getRecentTransactions(): List<TransactionResponse> {
        val creditTx = creditRepository.findAllByOrderByCreatedAtDesc().take(10).map { c ->
            TransactionResponse(
                id = "credit-" + c.id,
                title = c.title,
                category = c.category,
                date = c.date,
                amount = c.amount,
                type = "CREDIT",
                iconType = when (c.iconType) {
                    "CORPORATE" -> "RECEIPT"
                    "SHOPPING" -> "PAYMENT"
                    "INVESTMENT" -> "PAYMENT"
                    else -> "GENERIC"
                }
            )
        }

        val debitTx = debitRepository.findAllByOrderByCreatedAtDesc().take(10).map { d ->
            TransactionResponse(
                id = "debit-" + d.id,
                title = d.title,
                category = d.category,
                date = d.dueDate,
                amount = -d.amount,
                type = "DEBIT",
                iconType = when (d.iconType) {
                    "HOME" -> "HOME"
                    "RECEIPT" -> "RECEIPT"
                    "BANK" -> "TAX"
                    "CARD" -> "CREDIT_CARD"
                    else -> "GENERIC"
                }
            )
        }

        val combined = (creditTx + debitTx)
        return if (combined.isNotEmpty()) combined.take(10) else emptyList()
    }
}
