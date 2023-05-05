![GitHub Workflow Status](https://img.shields.io/github/actions/workflow/status/raxigan/cron-parser/ci.yml)

# Cron Expression Parser

### Running the app

The easiest way to run the application is to use provided [cron_parser.kts](cron_parser.kts) script. It
requires [kotlin executable](https://kotlinlang.org/docs/command-line.html) and [kscript](https://kscript.org/) installed. Both tools can be installed using [sdkman](https://sdkman.io/):

```shell
sdk install kotlin
sdk install kscript
```

Here's a list of all execution methods:

* **Using [kscript](https://kscript.org/) (4.0.0+)**

Run the following command from the project root directory:

```shell
./cron_parser.kts "*/15 0 1,15 * 1-5 /usr/bin/find"
```
or
```shell
kscript cron_parser.kts "*/15 0 1,15 * 1-5 /usr/bin/find"
```

* **Using [kotlin executable](https://kotlinlang.org/docs/command-line.html)**

Build the project using maven (provided maven wrapper scripts might be used) and execute the following command from the project root directory:
```shell
./mvnw package -DskipTests
kotlin -cp target/cron-parser-0.0.1-SNAPSHOT.jar com.homework.AppKt "*/15 0 1,15 * 1-5 /usr/bin/find"
```

* **Using IntelliJ IDEA**

Execute [App.kt](src%2Fmain%2Fkotlin%2Fcom%2Fhomework%2FApp.kt) file.
