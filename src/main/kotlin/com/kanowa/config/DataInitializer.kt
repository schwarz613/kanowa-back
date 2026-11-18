package com.kanowa.config

import com.kanowa.entity.CreditEntity
import com.kanowa.entity.DebitEntity
import com.kanowa.entity.UserEntity
import com.kanowa.repository.CreditRepository
import com.kanowa.repository.DebitRepository
import com.kanowa.repository.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Configuration

@Configuration
class DataInitializer(
    private val userRepository: UserRepository,
    private val creditRepository: CreditRepository,
    private val debitRepository: DebitRepository
) : CommandLineRunner {

    private val logger = LoggerFactory.getLogger(DataInitializer::class.java)

    override fun run(vararg args: String?) {
        // 1. Seed User
        if (userRepository.count() == 0L) {
            val defaultUser = UserEntity(
                name = "Exemplo Usuário",
                email = "usuario@kanowa.com",
                password = "password123"
            )
            userRepository.save(defaultUser)
            logger.info("Usuário inicial criado: ${defaultUser.email}")
        }

        // 2. Seed Credits
        if (creditRepository.count() == 0L) {
            val credits = listOf(
                CreditEntity(
                    title = "Contrato Prestação de Serviços",
                    subtitle = "Consultoria Corporativa Q4",
                    amount = 14500.00,
                    status = "CONCLUÍDO",
                    frequency = "Mensal",
                    category = "Receita Operacional",
                    iconType = "CORPORATE",
                    date = "28 de out, 2026"
                ),
                CreditEntity(
                    title = "Recebimento Stripe Pagamentos",
                    subtitle = "Assinaturas SaaS do Mês",
                    amount = 8500.00,
                    status = "CONCLUÍDO",
                    frequency = "Mensal",
                    category = "Receita Operacional",
                    iconType = "SHOPPING",
                    date = "22 de out, 2026"
                ),
                CreditEntity(
                    title = "Dividendos Fundo Imobiliário",
                    subtitle = "Rendimentos Tesouro & FII",
                    amount = 3200.00,
                    status = "CONCLUÍDO",
                    frequency = "Mensal",
                    category = "Investimento",
                    iconType = "INVESTMENT",
                    date = "18 de out, 2026"
                ),
                CreditEntity(
                    title = "Venda de Licença Corporativa",
                    subtitle = "Setup & Customização",
                    amount = 6800.00,
                    status = "CONCLUÍDO",
                    frequency = "Único",
                    category = "Receita Operacional",
                    iconType = "CORPORATE",
                    date = "10 de out, 2026"
                )
            )
            creditRepository.saveAll(credits)
            logger.info("Créditos iniciais populados: ${credits.size} registros.")
        }

        // 3. Seed Debits
        if (debitRepository.count() == 0L) {
            val debits = listOf(
                DebitEntity(
                    title = "Amazon Web Services",
                    subtitle = "Hospedagem & Banco de Dados",
                    amount = 1240.00,
                    status = "PAGO",
                    isAlert = false,
                    category = "Software & Nuvem",
                    iconType = "RECEIPT",
                    dueDate = "Vence em 24 de Nov"
                ),
                DebitEntity(
                    title = "Aluguel Comercial - Escritório",
                    subtitle = "Central Park Empreendimentos",
                    amount = 3200.00,
                    status = "PENDENTE",
                    isAlert = false,
                    category = "Infraestrutura",
                    iconType = "HOME",
                    dueDate = "Vence em 4 dias"
                ),
                DebitEntity(
                    title = "Energia Elétrica Empresarial",
                    subtitle = "Companhia Estadual de Energia",
                    amount = 142.50,
                    status = "AGENDADO",
                    isAlert = false,
                    category = "Infraestrutura",
                    iconType = "BANK",
                    dueDate = "Débito Automático: 15 Nov"
                ),
                DebitEntity(
                    title = "Internet Fibra Corporativa",
                    subtitle = "FiberOptics Telecom",
                    amount = 89.90,
                    status = "PENDENTE",
                    isAlert = true,
                    category = "Telecomunicações",
                    iconType = "RECEIPT",
                    dueDate = "Aviso: Reajuste anual"
                ),
                DebitEntity(
                    title = "Assinatura Figma Team",
                    subtitle = "Licenciamento de Design",
                    amount = 180.00,
                    status = "PAGO",
                    isAlert = false,
                    category = "Software & Nuvem",
                    iconType = "CARD",
                    dueDate = "Pago em 15 de Out"
                )
            )
            debitRepository.saveAll(debits)
            logger.info("Débitos iniciais populados: ${debits.size} registros.")
        }
    }
}
