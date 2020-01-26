package com.darwinit.datascience.data.importer

import com.darwinit.application.arguments.ArgumentContainer
import org.springframework.stereotype.Component

@Component
@ArgumentContainer
data class ImporterArgumentContainer(var exporter: Exporter=Exporter(),
                                     var user: User = User(),
                                     var sql: SQL = SQL(),
                                     var help: Boolean=false)

data class CSV(var delimiter: String=",", var path: String="/tmp/employees.csv")
data class Exporter(var type: String="csv", var csv: CSV=CSV())
data class User(var name: String="root", var password: String="my-secret-pw")
data class SQL(var jdbc: String="jdbc:mysql://localhost:3306/cover",
               var query: String="select * from employees")
