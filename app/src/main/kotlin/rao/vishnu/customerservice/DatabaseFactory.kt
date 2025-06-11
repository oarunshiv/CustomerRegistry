package rao.vishnu.customerservice

import io.github.oshai.kotlinlogging.KotlinLogging
import org.jetbrains.exposed.v1.core.Slf4jSqlDebugLogger
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import org.jetbrains.exposed.v1.jdbc.addLogger
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

private val logger = KotlinLogging.logger {}

object DatabaseFactory {
    fun init() {
        val dbUrl = System.getenv("JDBC_URL") ?: error("JDBC_URL environmental variable not configured")
        val dbUser = System.getenv("DB_USER") ?: error("DB_USER environmental variable not configured")
        val dbPassword = System.getenv("DB_PASSWORD") ?: error("DB_PASSWORD environmental variable not configured")

        Database.connect(
            url = dbUrl,
            driver = "org.postgresql.Driver",
            user = dbUser,
            password = dbPassword
        )

        logger.info { "Connected to PostgreSQL at $dbUrl using $dbUser and $dbPassword" }

        transaction {
            addLogger(Slf4jSqlDebugLogger)
            SchemaUtils.create(Customers)
        }
    }
}
