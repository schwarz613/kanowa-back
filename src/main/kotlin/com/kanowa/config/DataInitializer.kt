package com.kanowa.config

import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.JdbcTemplate

@Configuration
class DataInitializer(
    private val jdbcTemplate: JdbcTemplate
) : CommandLineRunner {

    private val logger = LoggerFactory.getLogger(DataInitializer::class.java)

    override fun run(vararg args: String?) {
        // Remove legacy seed rows created before user_id existed so all records belong to a user.
        val deletedCredits = jdbcTemplate.update("DELETE FROM credits WHERE user_id IS NULL")
        val deletedDebits = jdbcTemplate.update("DELETE FROM debits WHERE user_id IS NULL")
        if (deletedCredits > 0 || deletedDebits > 0) {
            logger.info("Limpeza de registros legados sem user_id: credits={}, debits={}", deletedCredits, deletedDebits)
        }
        logger.info("Inicialização concluída: banco de dados pronto.")
    }
}
