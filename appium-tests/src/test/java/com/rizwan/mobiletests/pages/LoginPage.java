package com.rizwan.mobiletests.pages;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;

public class LoginPage extends BasePage {
    private static final By EMAIL = byAccessibilityId("email");
    private static final By PASSWORD = byAccessibilityId("password");
    private static final By SIGN_IN = byAccessibilityId("Sign in");
    private static final By ERROR = byAccessibilityId("login-error");
    private static final By TITLE = byText("Sign in to continue");

    public LoginPage(AndroidDriver driver) {
        super(driver);
    }

    public boolean isDisplayed() {
        return isVisible(TITLE);
    }

    public LoginPage enterCredentials(String email, String password) {
        if (!email.isEmpty()) type(EMAIL, email);
        if (!password.isEmpty()) type(PASSWORD, password);
        hideKeyboardIfShown();
        return this;
    }

    public void submit() {
        tap(SIGN_IN);
    }

    public void login(String email, String password) {
        enterCredentials(email, password).submit();
    }

    /** Waits for the validation message and returns it. */
    public String errorText() {
        return textOfNonEmpty(ERROR);
    }
}
