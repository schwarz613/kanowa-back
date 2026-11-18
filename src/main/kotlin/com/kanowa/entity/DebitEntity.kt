package com.kanowa.entity

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "debits")
data class DebitEntity(
    @Id
    @Column(length = 36)
    var id: String = UUID.randomUUID().toString(),

    @Column(nullable = false)
    var title: String = "",

    @Column(nullable = false)
    var subtitle: String = "Vencimento Programado",

    @Column(nullable = false)
    var amount: Double = 0.0,

    @Column(nullable = false)
    var status: String = "PENDENTE",

    @Column(nullable = false)
    var isAlert: Boolean = false,

    @Column(nullable = false)
    var category: String = "Geral",

    @Column(nullable = false)
    var iconType: String = "HOME",

    @Column(nullable = false)
    var dueDate: String = "",

    @Column(nullable = false)
    var createdAt: LocalDateTime = LocalDateTime.now()
)
