package com.kanowa.dto

data class CreateCreditRequest(
    val title: String,
    val amount: Double,
    val frequency: String? = "Mensal",
    val subtitle: String? = "Recebimento Corporativo",
    val status: String? = "CONCLUÍDO",
    val category: String? = "Receita Operacional",
    val iconType: String? = "CORPORATE",
    val date: String? = null,
    val userId: Long? = null
)

data class UpdateCreditRequest(
    val title: String? = null,
    val amount: Double? = null,
    val frequency: String? = null,
    val subtitle: String? = null,
    val status: String? = null,
    val category: String? = null,
    val iconType: String? = null,
    val date: String? = null
)

data class CreditResponse(
    val id: String,
    val title: String,
    val subtitle: String,
    val amount: Double,
    val status: String,
    val frequency: String,
    val category: String,
    val iconType: String,
    val date: String,
    val userId: Long? = null
)
