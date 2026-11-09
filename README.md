# Appium Android Mobile Automation Framework

Native Android UI test automation with **Java 17, Appium 2 (UiAutomator2), TestNG and Maven**, using the Page Object Model. The repository includes its own small Android app as the system under test, so the whole pipeline (build app, boot emulator, start Appium, run tests, collect reports) lives in one place and runs in GitHub Actions.

## What is covered

| Area | Scenarios |
|---|---|
| Login (`LoginTests`) | valid login; four invalid combinations via a TestNG `@DataProvider` (wrong password, unknown user, case sensitive password, empty form); recover after a failed attempt |
| Navigation (`NavigationTests`) | app starts on login; log out returns to login; login is required again after logout |
| Profile (`ProfileTests`) | save a name and see confirmation; blank and whitespace only names rejected; Back returns home; saved name pre-fills when the form is reopened |

14 test executions in total.

## Engineering practices shown

- **Page Object Model** with a shared `BasePage` (explicit waits, safe keyboard handling, no `Thread.sleep`, no implicit waits mixed in).
- **Accessibility id locators** (content description) instead of brittle XPath indexes.
- **Environment driven configuration** (`Config.java`): server URL, platform version, device name, optional APK path.
- **Test isolation:** every test gets a fresh Appium session with the app state reset.
- **Diagnostics:** screenshot saved automatically for every failed test, TestNG/Surefire XML reports, Appium server log.
- **CI** (`.github/workflows/android-appium.yml`): job 1 builds the APK with the Gradle wrapper and compiles the tests; job 2 boots an Android API 34 emulator with KVM acceleration, starts Appium and runs the suite, then uploads reports, screenshots and the Appium log even on failure.

## Prerequisites

- JDK 17+, Maven 3.9+
- Android SDK with an emulator image or a physical device with USB debugging (`adb devices` must list it)
- Node.js 20+ for Appium

## Run locally

1. Build the demo app (Gradle wrapper included, no separate Gradle install needed):

   ```bash
   cd android-app
   ./gradlew assembleDebug        # Windows: gradlew.bat assembleDebug
   ```

2. Start an emulator or connect a device, then run everything with one command from the repo root:

   ```bash
   bash scripts/run-appium-tests.sh
   ```

   The script installs Appium 2 and the UiAutomator2 driver if missing, starts the server, runs `mvn clean test` and stops Appium afterwards.

Or run the pieces yourself:

```bash
npm install -g appium@2
appium driver install uiautomator2
appium                                   # terminal 1

cd appium-tests                          # terminal 2
export PLATFORM_VERSION=14               # Android version of your emulator
export APP_PATH="$(pwd)/../android-app/app/build/outputs/apk/debug/app-debug.apk"
mvn test
```

| Variable | Default | Purpose |
|---|---|---|
| `APPIUM_SERVER_URL` | `http://127.0.0.1:4723` | Appium server |
| `PLATFORM_VERSION` | `14` | Android version of the target device |
| `DEVICE_NAME` | `Android Emulator` | Device label |
| `APP_PATH` | empty | APK to install. If empty, the app must already be installed |
| `EXPLICIT_WAIT_SECONDS` | `10` | Timeout for page object waits |

Windows PowerShell: use `$env:PLATFORM_VERSION="14"` instead of `export`.

## Finding locators with Appium Inspector

Locators were chosen with [Appium Inspector](https://github.com/appium/appium-inspector). To explore the app yourself: start the Appium server, open Appium Inspector, set the remote host to `127.0.0.1`, port `4723`, paste the JSON from `docs/appium-inspector-capabilities.json` and start the session. Select an element and read its `accessibility id` in the details panel.

## Reports

- Surefire/TestNG XML and HTML: `appium-tests/target/surefire-reports/`
- Failure screenshots: `appium-tests/target/screenshots/`
- Appium server log: `appium.log` (repo root)

## Demo app

`android-app/` is a single activity app (package `com.rizwan.mobiletestapp`) with a login screen, home screen, profile form and logout. The demo credentials are `qa@example.com` / `Password123!`. It exists only as a test target.

## Project structure

```text
.
├── .github/workflows/android-appium.yml
├── android-app/                     demo app + Gradle wrapper
├── appium-tests/
│   ├── pom.xml, testng.xml
│   └── src/test/java/com/rizwan/mobiletests/
│       ├── Config.java, DriverFactory.java, BaseTest.java
│       ├── LoginTests.java, NavigationTests.java, ProfileTests.java
│       └── pages/                   BasePage, LoginPage, HomePage, ProfilePage
├── docs/appium-inspector-capabilities.json
└── scripts/run-appium-tests.sh
```

## Troubleshooting

- **`SessionNotCreatedException`:** `PLATFORM_VERSION` does not match the device, or no device is attached. Check `adb devices`.
- **App not found / activity not started:** set `APP_PATH` to the built APK, or install it with `adb install -r`.
- **Element not found right after a tap:** raise `EXPLICIT_WAIT_SECONDS` on slow emulators.
