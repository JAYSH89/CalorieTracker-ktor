package nl.jaysh.core.config

import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database
import org.postgresql.ds.PGSimpleDataSource

object DbConfig {
    val setup by lazy {
        val dataSource = PGSimpleDataSource().apply {
            user = "postgres"
            password = "postgres"
            databaseName = "calorietracker"
        }

        val flyway = Flyway.configure().dataSource(dataSource).load()
        flyway.migrate()

        Database.connect(datasource = dataSource)
    }
}
