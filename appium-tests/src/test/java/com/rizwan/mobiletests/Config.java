package com.rizwan.mobiletests;

import java.time.Duration;

/** Central place for environment driven settings. */
public final class Config {
    private Config() {}

    public static final String APP_PACKAGE = "com.rizwan.mobiletestapp";
    public static final String APP_ACTIVITY = "com.rizwan.mobiletestapp.MainActivity";

    public static final String VALID_EMAIL = "qa@example.com";
    public static final String VALID_PASSWORD = "Password123!";

    public static String serverUrl() {
        return env("APPIUM_SERVER_URL", "http://127.0.0.1:4723");
    }

    public static String platformVersion() {
        return env("PLATFORM_VERSION", "14");
    }

    public static String deviceName() {
        return env("DEVICE_NAME", "Android Emulator");
    }

    /** Optional absolute path to the APK. When set, Appium installs it; otherwise the app must already be installed. */
    public static String appPath() {
        return env("APP_PATH", "");
    }

    public static Duration explicitWait() {
        return Duration.ofSeconds(Long.parseLong(env("EXPLICIT_WAIT_SECONDS", "10")));
    }

    private static String env(String name, String fallback) {
        String value = System.getenv(name);
        return value == null || value.isBlank() ? fallback : value;
    }
}
