package nl.jaysh.core.config

import org.jetbrains.exposed.sql.Database
import org.postgresql.ds.PGSimpleDataSource

object DbConfig {
    fun setup(
        dbUser: String = "postgres",
        dbPassword: String = "postgres",
        dbName: String = "calorietracker",
    ) {
        val dataSource = PGSimpleDataSource().apply {
            user = dbUser
            password = dbPassword
            databaseName = dbName
        }

        Database.connect(datasource = dataSource)
    }
}
