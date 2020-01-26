package com.covergroup.datascience.data.importer

import com.covergroup.application.arguments.ArgumentContainer
import org.springframework.stereotype.Component

@Component
@ArgumentContainer
data class ImporterArgumentContainer(var exporter: Exporter=Exporter(),
                                     var server: Server = Server(),
                                     var user: User = User(),
                                     var sql: SQL = SQL(),
                                     var help: Boolean=false)

data class CSV(var delimiter: String=",", var path: String="/tmp/employees.csv")
data class Exporter(var type: String="csv", var csv: CSV=CSV())
data class User(var name: String="root", var password: String="my-secret-pw")
data class SQL(var driver: String="com.mysql.cj.jdbc.Driver",
               var database: String="cover",
               var type: String="mysql",
               var query: String="select * from employees")
data class Server(var hostname: String="127.0.0.1", var port: Int=3306)