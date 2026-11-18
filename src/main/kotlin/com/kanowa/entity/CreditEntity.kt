package com.kanowa.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "credits")
data class CreditEntity(
    @Id
    @Column(length = 36)
    var id: String = UUID.randomUUID().toString(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    var user: UserEntity? = null,

    @Column(nullable = false)
    var title: String = "",

    @Column(nullable = false)
    var subtitle: String = "Recebimento Corporativo",

    @Column(nullable = false)
    var amount: Double = 0.0,

    @Column(nullable = false)
    var status: String = "CONCLUÍDO",

    @Column(nullable = false)
    var frequency: String = "Mensal",

    @Column(nullable = false)
    var category: String = "Receita Operacional",

    @Column(nullable = false)
    var iconType: String = "CORPORATE",

    @Column(nullable = false)
    var date: String = "",

    @Column(nullable = false)
    var createdAt: LocalDateTime = LocalDateTime.now()
)
