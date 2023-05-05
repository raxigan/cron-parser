![GitHub Workflow Status](https://img.shields.io/github/actions/workflow/status/raxigan/cron-parser/ci.yml)

# Cron Expression Parser

### Running the app

* **Using [kscript](https://kscript.org/) (4.0.0+)**

Run the following command from project root directory:

```shell
./cron_parser.kts "*/15 0 1,15 * 1-5 /usr/bin/find"
```
or
```shell
kscript cron_parser.kts "*/15 0 1,15 * 1-5 /usr/bin/find"
```

* **Using kotlin binary**

Build the project using maven and execute the following command from project root directory:
```shell
kotlin -cp target/cron-parser-0.0.1-SNAPSHOT.jar com.homework.App "*/15 0 1,15 * 1-5 /usr/bin/find"
```

* **Using IntelliJ IDEA**

Execute [App.kt](src%2Fmain%2Fkotlin%2Fcom%2Fhomework%2FApp.kt) file.
