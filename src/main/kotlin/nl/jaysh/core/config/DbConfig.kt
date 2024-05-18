package nl.jaysh.core.config

import org.jetbrains.exposed.sql.Database
import org.postgresql.ds.PGSimpleDataSource

object DbConfig {
    val setup by lazy {
        val dataSource = PGSimpleDataSource().apply {
            user = "postgres"
            password = "postgres"
            databaseName = "calorietracker"
        }

        Database.connect(datasource = dataSource)
    }
}
