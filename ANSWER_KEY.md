# Answer key: library replacement workshop (instructors only)

Verified: the starter and this solution both pass the same 10 tests, unchanged, on JDK 17 with Gradle 9.7.1 and Spring Boot 3.5.7. The starter's dependency report lists `joda-time:joda-time:2.1`; the solution's report contains none of the three old libraries. Only the automated tests were run: the app was not started by hand and Bruno was not used.

## Expected changes

| Task | Starter | Solution |
|---|---|---|
| C2 dates | `org.joda.time` (`Days.daysBetween`, `DateTimeConstants.SATURDAY`, `DateTimeFormat`) and a conversion from `java.time.LocalDate` | `java.time` only: `ChronoUnit.DAYS.between`, `DayOfWeek`, `DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy", Locale.ENGLISH)`. The conversion method disappears. |
| C3 logging | `org.apache.log4j.Logger`, string concatenation, `log4j.properties`, `log4j:log4j:1.2.17` | `org.slf4j.Logger` + `LoggerFactory`, `{}` placeholder, `log4j.properties` deleted, logging settings in `application.properties`, no new dependency |
| C4 CSV | `au.com.bytecode.opencsv.CSVWriter` from `net.sf.opencsv:opencsv:2.3` | `com.opencsv.CSVWriter` from `com.opencsv:opencsv:5.9` (a package rename). Apache Commons CSV also works with `QuoteMode.ALL` and `setRecordSeparator("\n")`. |
| C6 cleanup | three old coordinates in `build.gradle` | one new dependency, `com.opencsv:opencsv:5.9` |

The solution project is in this folder. To try it, run `gradlew.bat test` here.

## Gotchas students will hit
- **CSV defaults.** The old writer quotes every field and uses `\n`. Apache Commons CSV defaults to quoting only when needed and `\r\n`, so the export tests fail until `QuoteMode.ALL` and the `\n` separator are set. The opencsv 5 defaults happened to match in this project's tests.
- **`long` vs `int`.** `ChronoUnit.DAYS.between` returns a `long`.
- **Weekday numbers.** Joda's `getDayOfWeek()` is a number (Saturday is 6). `java.time` returns an enum.
- **Locale.** The date label test needs `Locale.ENGLISH`. Leaving it out can pass on an English machine and fail on another.
- **Logging config.** Deleting `log4j.properties` without setting `logging.*` properties leaves Spring Boot's defaults, which still pass the log test. The log test depends on the exact message text `Employee created: id=<id>`.
- **The Joda version.** It is pinned to 2.1 on purpose, a very old release. Newer Joda-Time versions are still released, so "unmaintained" is not fully accurate for the whole project. The honest answer is "in maintenance mode, and its own authors recommend `java.time`". This makes a useful discussion point for C1.
- **Do not assume the audit.** Students must find release dates themselves. Check the dates on Maven Central yourself before the session, since I did not verify them.

## Transitive dependency worth showing (stretch S2)
The solution's dependency report shows `com.opencsv:opencsv:5.9` pulling in `commons-beanutils:1.9.4`, which pulls in `commons-collections:3.2.2` (the older 3.x line of Commons Collections). Students doing S2 should find this. Whether to act on it, for example by excluding it, is a judgement call to discuss.

## Marking hints
- `grep -rE "org.joda|org.apache.log4j|au.com.bytecode" src` returns nothing, and `log4j.properties` is gone.
- `gradlew.bat dependencies --configuration runtimeClasspath` shows none of `joda-time`, `log4j:log4j`, `net.sf.opencsv`.
- `git diff --stat` shows no change to `src/test`.
- The audit table has real release years and a stated project status for each library.
- The Bruno baseline and the final responses match, including the CSV text.
