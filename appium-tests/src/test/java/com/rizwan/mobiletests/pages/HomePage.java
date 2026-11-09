package com.rizwan.mobiletests.pages;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;

public class HomePage extends BasePage {
    private static final By WELCOME = byText("Welcome, QA Engineer");
    private static final By OPEN_PROFILE = byAccessibilityId("Open profile");
    private static final By LOG_OUT = byAccessibilityId("Log out");

    public HomePage(AndroidDriver driver) {
        super(driver);
    }

    public boolean isDisplayed() {
        return isVisible(WELCOME);
    }

    public void openProfile() {
        tap(OPEN_PROFILE);
    }

    public void logout() {
        tap(LOG_OUT);
    }
}
