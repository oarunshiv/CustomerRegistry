package rao.vishnu.customerservice

import org.jetbrains.exposed.v1.core.Slf4jSqlDebugLogger
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import org.jetbrains.exposed.v1.jdbc.addLogger
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

/**
 * Factory object responsible for initializing and configuring the database connection.
 *
 * Reads connection details from environment variables and connects to a PostgreSQL database.
 * Sets up Exposed ORM with logging and ensures the 'Customers' table is created.
 *
 * @throws IllegalStateException if required environment variables are missing.
 */
object DatabaseFactory {
    fun init(dbUrl: String, dbDriver: String, dbUser: String, dbPassword: String, enableLogging: Boolean = false) {
        Database.connect(dbUrl, dbDriver, dbUser, dbPassword)

        transaction {
            if(enableLogging) {
                addLogger(Slf4jSqlDebugLogger)
            }
            SchemaUtils.create(Customers)
        }
    }
}
