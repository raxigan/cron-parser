![GitHub Workflow Status](https://img.shields.io/github/actions/workflow/status/raxigan/cron-parser/ci.yml)

# Cron Expression Parser

### Running the app

* **Using [kscript](https://kscript.org/) (4.0.0+) - depends on [kotlin executable](https://kotlinlang.org/docs/command-line.html)**

Run the following command from the project root directory:

```shell
./cron_parser.kts "*/15 0 1,15 * 1-5 /usr/bin/find"
```
or
```shell
kscript cron_parser.kts "*/15 0 1,15 * 1-5 /usr/bin/find"
```

* **Using [kotlin executable](https://kotlinlang.org/docs/command-line.html)**

Build the project using maven (provided maven wrapper might be used) and execute the following command from the project root directory:
```shell
kotlin -cp target/cron-parser-0.0.1-SNAPSHOT.jar com.homework.AppKt "*/15 0 1,15 * 1-5 /usr/bin/find"
```

* **Using IntelliJ IDEA**

Execute [App.kt](src%2Fmain%2Fkotlin%2Fcom%2Fhomework%2FApp.kt) file.
