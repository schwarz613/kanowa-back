package com.kanowa.dto

data class FinancialSummaryResponse(
    val totalBalance: Double,
    val balanceGrowthPercentage: Double,
    val monthlyRevenue: Double,
    val revenueGrowthPercentage: Double,
    val monthlyExpenses: Double,
    val expensesReductionPercentage: Double,
    val savingsRatePercentage: Double,
    val activeDebitsCount: Int
)

data class TransactionResponse(
    val id: String,
    val title: String,
    val category: String,
    val date: String,
    val amount: Double,
    val type: String, // "CREDIT" or "DEBIT"
    val iconType: String
)
