package com.darwinit.datascience.data.importer

import com.darwinit.datascience.data.exporter.ExportDispatcher
import com.darwinit.datascience.data.importer.database.DatabaseConnection
import com.darwinit.datascience.data.importer.database.Dataset
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class ScheduledTasks {

    @Autowired
    private lateinit var exportDispatcher: ExportDispatcher

    @Autowired
    private lateinit var databaseConnection: DatabaseConnection

    @Autowired
    private lateinit var importerArgumentContainer: ImporterArgumentContainer

    @Scheduled(fixedRate = 10000)
    fun doQuery() {
        databaseConnection.query(importerArgumentContainer.sql.query) {
                it : Sequence<Dataset> -> it.forEachIndexed {
                index, dataset -> exportDispatcher.dispatch(index, dataset)
        }}
    }

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(ScheduledTasks::class.java)
    }
}

