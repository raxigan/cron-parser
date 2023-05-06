![GitHub Workflow Status](https://img.shields.io/github/actions/workflow/status/raxigan/cron-parser/ci.yml)

# Cron Expression Parser

The preferable way to run the application is to use provided [cron_parser.kts](cron_parser.kts) script, which
requires [kotlin executable](https://kotlinlang.org/docs/command-line.html) and [kscript](https://kscript.org/) to run. Both tools can be installed via [sdkman](https://sdkman.io/). The
app was tested on JDK 11 and 17.

```shell
# install sdkman
curl -s "https://get.sdkman.io" | bash
```

```shell
# install java (optionally), kotlin & kscript
sdk install java 17.0.7-zulu 
sdk use java 17.0.7-zulu
sdk install kotlin
sdk install kscript
```

### Running the app

Build the project using maven (there's maven wrapper provided):

```shell
./mvnw package -DskipTests
```

* **Run using [kscript](https://kscript.org/) (4.0.0+)**

Run the following command from the project root directory:

```shell
./cron_parser.kts "*/15 0 1,15 * 1-5 /usr/bin/find"
```
or
```shell
kscript cron_parser.kts "*/15 0 1,15 * 1-5 /usr/bin/find"
```

* **Run using [kotlin executable](https://kotlinlang.org/docs/command-line.html)**

then execute the following command from the project root directory:
```shell
kotlin -cp target/cron-parser-0.0.1-SNAPSHOT.jar com.homework.AppKt "*/15 0 1,15 * 1-5 /usr/bin/find"
```

* **Run using IntelliJ IDEA**

Execute [App.kt](src%2Fmain%2Fkotlin%2Fcom%2Fhomework%2FApp.kt) file.
