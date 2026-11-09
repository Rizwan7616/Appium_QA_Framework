package com.rizwan.mobiletests.pages;

import io.appium.java_client.android.AndroidDriver;
import java.time.Duration;
import org.openqa.selenium.By;

public class ProfilePage extends BasePage {
    private static final By NAME = byAccessibilityId("profile-name");
    private static final By SAVE = byAccessibilityId("Save profile");
    private static final By ERROR = byAccessibilityId("profile-error");
    private static final By BACK = byAccessibilityId("Back");
    private static final By SAVED_MESSAGE = byText("Profile updated successfully");
    private static final By BACK_TO_HOME = byAccessibilityId("Back to home");

    public ProfilePage(AndroidDriver driver) {
        super(driver);
    }

    public boolean isDisplayed() {
        return isVisible(byText("Update your display name"));
    }

    public void saveName(String name) {
        type(NAME, name);
        hideKeyboardIfShown();
        tap(SAVE);
    }

    public void saveBlank() {
        tap(SAVE);
    }

    public String errorText() {
        return textOfNonEmpty(ERROR);
    }

    public boolean savedMessageVisible() {
        return isVisible(SAVED_MESSAGE);
    }

    public boolean savedMessageAbsent() {
        return isGone(SAVED_MESSAGE, Duration.ofSeconds(2));
    }

    public boolean savedNameShown(String name) {
        return isVisible(byText("Saved name: " + name));
    }

    public String nameFieldValue() {
        return textOf(NAME);
    }

    public void back() {
        tap(BACK);
    }

    public void backToHomeFromConfirmation() {
        tap(BACK_TO_HOME);
    }
}
