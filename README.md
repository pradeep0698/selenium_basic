# Selenium Basics (Java + Maven + TestNG)

A small learning project. Every file is one topic, with comments. Tests run against the free practice site
https://the-internet.herokuapp.com.

## Setup (once)
1. Install **JDK 17+**, **Google Chrome**, and **IntelliJ IDEA Community** (it includes Maven).
2. Copy this `selenium-basics` folder to your Desktop.
3. IntelliJ → **File → Open** → pick the `selenium-basics` folder → wait for Maven to download the libraries.

## Run
- **One test:** open a file in `src/test/java/tests/`, click the green ▶ next to the class or a method.
- **All tests:** right-click `testng.xml` → **Run**.
- **Command line** (needs Maven installed): `mvn test` · no visible browser: `mvn test -Dheadless=true`
- **Slow mode** is on by default (red outline + 1 s pause per click/typing). Full speed: `mvn test -Dslow=0`, or set `SLOW_MS` in `BaseTest.java`.
- Report after a run: `target/surefire-reports/index.html`

## Learn in this order
| # | File | Topic |
|---|------|-------|
| — | `pom.xml` | Maven dependencies (Selenium, TestNG) |
| — | `base/BaseTest.java` | Open/close the browser, implicit wait, headless |
| 1 | `T01_LocatorsTest` | id, name, css, xpath, tag, class, link text; findElements |
| 2 | `T02_ActionsTest` | click, sendKeys, clear, getText, checkboxes, back/forward |
| 3 | `T03_WaitsTest` | Explicit waits (WebDriverWait) — why not Thread.sleep |
| 4 | `T04_DropdownTest` | Select by text / value / index |
| 5 | `T05_AlertsTest` | alert, confirm, prompt |
| 6 | `T06_FramesWindowsTest` | iframes, new tabs/windows |
| 7 | `T07_MouseKeyboardTest` | hover, right-click, keys, JavaScript scroll |
| 8 | `T08_ScreenshotTest` | Save a screenshot |
| 9 | `pages/LoginPage` + `T09_PageObjectLoginTest` | Page Object Model, priority, groups |
| 10 | `T10_DataDrivenTest` | @DataProvider: one test, many data rows |
| 11 | `T11_FailDemoTest` | Fails on purpose: failure report + auto screenshot (`screenshots/FAILED_*.png`) |
| — | `testng.xml` | Choose which tests run (the suite) |
| 12 | `.github/workflows/selenium-tests.yml` | CI: run all tests on every GitHub push |

## Try next (practice)
- Make a test fail on purpose and read the error.
- Add a `SecureAreaPage` page object with a `logout()` method.
- Run only the smoke group: add `<groups><run><include name="smoke"/></run></groups>` inside `<test>` in `testng.xml`.
- Add `parallel="classes" thread-count="3"` to `<suite>` to run tests in parallel.
