package com.kanowa.dto

data class CreateDebitRequest(
    val title: String,
    val amount: Double,
    val category: String? = "Geral",
    val subtitle: String? = "Vencimento Programado",
    val status: String? = "PENDENTE",
    val isAlert: Boolean? = false,
    val iconType: String? = "HOME",
    val dueDate: String? = null
)

data class UpdateDebitRequest(
    val title: String? = null,
    val amount: Double? = null,
    val category: String? = null,
    val subtitle: String? = null,
    val status: String? = null,
    val isAlert: Boolean? = null,
    val iconType: String? = null,
    val dueDate: String? = null
)

data class DebitResponse(
    val id: String,
    val title: String,
    val subtitle: String,
    val amount: Double,
    val status: String,
    val isAlert: Boolean,
    val category: String,
    val iconType: String,
    val dueDate: String
)
