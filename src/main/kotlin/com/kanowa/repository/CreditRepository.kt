package com.kanowa.repository

import com.kanowa.entity.CreditEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface CreditRepository : JpaRepository<CreditEntity, String> {
    fun findAllByOrderByCreatedAtDesc(): List<CreditEntity>

    @Query("SELECT COALESCE(SUM(c.amount), 0.0) FROM CreditEntity c")
    fun getTotalMonthlyCredits(): Double
}
