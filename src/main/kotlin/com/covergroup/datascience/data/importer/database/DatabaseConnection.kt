package com.covergroup.datascience.data.importer.database

import com.covergroup.datascience.data.importer.ImporterArgumentContainer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.sql.*
import java.util.Properties
import com.github.nwillc.funkjdbc.*
import com.github.nwillc.funkjdbc.ResultsProcessor
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Component
class DatabaseConnection {

    @Autowired
    private lateinit var importerArgumentContainer: ImporterArgumentContainer
    private val logger: Logger = LoggerFactory.getLogger(DatabaseConnection::class.java)

    private val connection: Connection by lazy {
        val connectionProps = Properties()
        connectionProps["user"] = this.importerArgumentContainer.user.name
        connectionProps["password"] = this.importerArgumentContainer.user.password

        Class.forName(this.importerArgumentContainer.sql.driver).newInstance()

        DriverManager.getConnection(createUrl(), connectionProps)
    }

    private fun createUrl(): String {
        return "jdbc:${this.importerArgumentContainer.sql.type}"+
                    "://${this.importerArgumentContainer.server.hostname}:"+
                    "${this.importerArgumentContainer.server.port}/"+
                    this.importerArgumentContainer.sql.database
    }

    private fun datasetExtractor(rs: ResultSet) =
        Dataset((1 until rs.metaData.columnCount+1).map {
            rs.metaData.getColumnName(it) to rs.getString(it)
        }.toMap())

    fun query(sql: String, processor: ResultsProcessor<Dataset, Unit>) {
        return this.connection.query(sql, ::datasetExtractor, processor)
    }
}