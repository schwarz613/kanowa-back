package com.kanowa.service

import com.kanowa.dto.FinancialSummaryResponse
import com.kanowa.dto.TransactionResponse
import com.kanowa.entity.CreditEntity
import com.kanowa.entity.DebitEntity
import com.kanowa.repository.CreditRepository
import com.kanowa.repository.DebitRepository
import org.springframework.stereotype.Service

@Service
class DashboardService(
    private val creditRepository: CreditRepository,
    private val debitRepository: DebitRepository
) {
    fun getFinancialSummary(userId: Long? = null): FinancialSummaryResponse {
        val totalRevenue = if (userId != null) {
            creditRepository.getTotalMonthlyCreditsByUserId(userId)
        } else {
            creditRepository.getTotalMonthlyCredits()
        }

        val totalExpenses = if (userId != null) {
            debitRepository.getTotalMonthlyDebitsByUserId(userId)
        } else {
            debitRepository.getTotalMonthlyDebits()
        }

        val totalBalance = totalRevenue - totalExpenses

        val savingsRate = if (totalRevenue > 0) {
            ((totalRevenue - totalExpenses) / totalRevenue * 100).coerceAtLeast(0.0)
        } else {
            0.0
        }

        val activeDebitsCount = if (userId != null) {
            debitRepository.countByUserIdAndStatusNot(userId, "PAGO")
        } else {
            debitRepository.countByStatusNot("PAGO")
        }

        return FinancialSummaryResponse(
            totalBalance = totalBalance,
            balanceGrowthPercentage = 0.0,
            monthlyRevenue = totalRevenue,
            revenueGrowthPercentage = 0.0,
            monthlyExpenses = totalExpenses,
            expensesReductionPercentage = 0.0,
            savingsRatePercentage = savingsRate,
            activeDebitsCount = activeDebitsCount
        )
    }

    fun getRecentTransactions(userId: Long? = null): List<TransactionResponse> {
        val credits: List<CreditEntity> = if (userId != null) {
            creditRepository.findAllByUserIdOrderByCreatedAtDesc(userId)
        } else {
            creditRepository.findAllByOrderByCreatedAtDesc()
        }

        val debits: List<DebitEntity> = if (userId != null) {
            debitRepository.findAllByUserIdOrderByCreatedAtDesc(userId)
        } else {
            debitRepository.findAllByOrderByCreatedAtDesc()
        }

        val creditTx = credits.take(10).map { c ->
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

        val debitTx = debits.take(10).map { d ->
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
