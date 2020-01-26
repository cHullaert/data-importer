# Quickstart

```bash
    # start a docker mariadb instance
    docker run --name some-mariadb -e MYSQL_ROOT_PASSWORD=my-secret-pw -p 3306:3306 mariadb
    
    # start the application
    gradle bootRun

    # start the application on other mysql database
    gradle bootRun -Pargs=--sql.jdbc=jdbc:mysql://localhost:3306/cover

    # get more informations about parameters with
    gradle bootRun -Pargs=--help
```

# Database connector

For now, we use this connector for database, feel free to add / change one in the build.gradle file

```groovy
    // mysql
    compile group: 'mysql', name: 'mysql-connector-java', version: '8.0.18'
    // postgreSQL
    compile group: 'org.postgresql', name: 'postgresql', version: '42.2.9'
    // access
    compile group: 'net.sf.ucanaccess', name: 'ucanaccess', version: '5.0.0'
```
