package com.kanowa.repository

import com.kanowa.entity.DebitEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface DebitRepository : JpaRepository<DebitEntity, String> {
    fun findAllByUserIdOrderByCreatedAtDesc(userId: Long): List<DebitEntity>

    fun findAllByOrderByCreatedAtDesc(): List<DebitEntity>

    @Query("SELECT COALESCE(SUM(d.amount), 0.0) FROM DebitEntity d WHERE d.user.id = :userId")
    fun getTotalMonthlyDebitsByUserId(@Param("userId") userId: Long): Double

    @Query("SELECT COALESCE(SUM(d.amount), 0.0) FROM DebitEntity d")
    fun getTotalMonthlyDebits(): Double

    fun countByUserIdAndStatusNot(userId: Long, status: String): Int

    fun countByStatusNot(status: String): Int
}
