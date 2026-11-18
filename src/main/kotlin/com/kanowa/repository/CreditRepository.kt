package com.kanowa.repository

import com.kanowa.entity.CreditEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface CreditRepository : JpaRepository<CreditEntity, String> {
    fun findAllByUserIdOrderByCreatedAtDesc(userId: Long): List<CreditEntity>

    fun findAllByOrderByCreatedAtDesc(): List<CreditEntity>

    @Query("SELECT COALESCE(SUM(c.amount), 0.0) FROM CreditEntity c WHERE c.user.id = :userId")
    fun getTotalMonthlyCreditsByUserId(@Param("userId") userId: Long): Double

    @Query("SELECT COALESCE(SUM(c.amount), 0.0) FROM CreditEntity c")
    fun getTotalMonthlyCredits(): Double
}
