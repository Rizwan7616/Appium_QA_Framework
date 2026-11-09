package com.rizwan.mobiletests;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import java.net.URI;
import java.time.Duration;

public final class DriverFactory {
    private DriverFactory() {}

    public static AndroidDriver createDriver() throws Exception {
        UiAutomator2Options options = new UiAutomator2Options()
            .setPlatformName("Android")
            .setAutomationName("UiAutomator2")
            .setPlatformVersion(Config.platformVersion())
            .setDeviceName(Config.deviceName())
            .setAutoGrantPermissions(true)
            .setNoReset(false)
            .setNewCommandTimeout(Duration.ofSeconds(120));

        if (!Config.appPath().isEmpty()) {
            options.setApp(Config.appPath());
        }
        // Always set package and activity so the right screen is launched whether or not Appium installs the APK.
        options.setAppPackage(Config.APP_PACKAGE).setAppActivity(Config.APP_ACTIVITY);

        // Explicit waits are used in the page objects; implicit waits are deliberately not mixed in.
        return new AndroidDriver(URI.create(Config.serverUrl()).toURL(), options);
    }
}
