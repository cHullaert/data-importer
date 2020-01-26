package com.darwinit.datascience.data.exporter

import com.darwinit.datascience.data.importer.ImporterArgumentContainer
import com.darwinit.datascience.data.importer.database.Dataset
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component

interface Exporter {
    fun export(index: Int, dataset: Dataset)
}

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class ExporterType(val value: String)

@Component
class ExportDispatcher {

    @Autowired
    private lateinit var applicationContext: ApplicationContext

    @Autowired
    private lateinit var importerArgumentContainer: ImporterArgumentContainer

    private fun export(index: Int, dataset: Dataset, exporter: Exporter) {
        exporter.export(index, dataset)
    }

    fun dispatch(index: Int, dataset: Dataset) {
        val beans=applicationContext.getBeansWithAnnotation(ExporterType::class.java)
        beans.values.forEach {
            if(it is Exporter) {
                val annotation=it.javaClass.getAnnotation(ExporterType::class.java)
                if(annotation.value==importerArgumentContainer.exporter.type)
                    this.export(index, dataset, it)
            }
        }
    }
}