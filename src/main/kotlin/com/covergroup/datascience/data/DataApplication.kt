package com.covergroup.datascience.data

import com.covergroup.datascience.data.importer.ImporterArgumentContainer
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.scheduling.annotation.EnableScheduling


@SpringBootApplication
@EnableScheduling
open class DataApplication : ApplicationRunner {

    @Autowired
    lateinit var argumentContainer: ImporterArgumentContainer

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(DataApplication::class.java)

        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(DataApplication::class.java, *args)
        }
    }

    override fun run(args: ApplicationArguments?) {
    }
}