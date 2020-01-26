package com.darwinit.datascience.data.exporter

import com.darwinit.datascience.data.importer.ImporterArgumentContainer
import com.darwinit.datascience.data.importer.database.Dataset
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVPrinter
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.io.FileWriter

@Component
@ExporterType("csv")
class CSVExporter: Exporter {

    @Autowired
    lateinit var importerArgumentContainer: ImporterArgumentContainer

    override fun export(index: Int, dataset: Dataset) {
        val out=FileWriter(importerArgumentContainer.exporter.csv.path, index>0)
        var csvFormat=CSVFormat.DEFAULT
                      .withDelimiter(importerArgumentContainer.exporter.csv.delimiter.first())
        if(index==0)
            csvFormat=csvFormat.withHeader(*dataset.rows.keys.toTypedArray())

        val csvPrinter=CSVPrinter(out, csvFormat)
        csvPrinter.printRecord(dataset.rows.values)
        csvPrinter.flush()
        logger.trace(dataset.rows.values.joinToString(importerArgumentContainer.exporter.csv.delimiter))
    }

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(CSVExporter::class.java)
    }
}