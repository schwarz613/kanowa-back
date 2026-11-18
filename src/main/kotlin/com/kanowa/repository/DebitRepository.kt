package com.kanowa.repository

import com.kanowa.entity.DebitEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface DebitRepository : JpaRepository<DebitEntity, String> {
    fun findAllByOrderByCreatedAtDesc(): List<DebitEntity>

    @Query("SELECT COALESCE(SUM(d.amount), 0.0) FROM DebitEntity d")
    fun getTotalMonthlyDebits(): Double

    fun countByStatusNot(status: String): Int
}
