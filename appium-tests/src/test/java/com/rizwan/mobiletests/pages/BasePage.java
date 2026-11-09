package com.rizwan.mobiletests.pages;

import com.rizwan.mobiletests.Config;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/** Shared, explicit wait based helpers for every page object. */
public abstract class BasePage {
    protected final AndroidDriver driver;
    private final WebDriverWait wait;

    protected BasePage(AndroidDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Config.explicitWait());
    }

    /** Locator for an element exposed through its content description (accessibility id). */
    protected static By byAccessibilityId(String id) {
        return AppiumBy.accessibilityId(id);
    }

    /** Locator for an element by its exact visible text. */
    protected static By byText(String text) {
        return AppiumBy.androidUIAutomator("new UiSelector().text(\"" + text + "\")");
    }

    protected WebElement waitVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected void tap(By locator) {
        wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
    }

    protected void type(By locator, String text) {
        WebElement field = waitVisible(locator);
        field.clear();
        field.sendKeys(text);
    }

    protected String textOf(By locator) {
        return waitVisible(locator).getText();
    }

    /** Waits until the element has non empty text, then returns it. Avoids racing UI updates after a tap. */
    protected String textOfNonEmpty(By locator) {
        ExpectedCondition<String> hasText = d -> {
            String text = d.findElement(locator).getText();
            return text == null || text.isEmpty() ? null : text;
        };
        return wait.until(hasText);
    }

    /** True if the element becomes visible within the timeout. Never throws on absence. */
    protected boolean isVisible(By locator, Duration timeout) {
        try {
            new WebDriverWait(driver, timeout).until(ExpectedConditions.visibilityOfElementLocated(locator));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    protected boolean isVisible(By locator) {
        return isVisible(locator, Config.explicitWait());
    }

    /** True if the element is absent (or hidden) after the timeout. */
    protected boolean isGone(By locator, Duration timeout) {
        try {
            return new WebDriverWait(driver, timeout).until(ExpectedConditions.invisibilityOfElementLocated(locator));
        } catch (TimeoutException e) {
            return false;
        }
    }

    /** Closes the soft keyboard only when it is really showing, so the BACK key is never sent to the app by mistake. */
    protected void hideKeyboardIfShown() {
        try {
            if (driver.isKeyboardShown()) {
                driver.hideKeyboard();
            }
        } catch (Exception ignored) {
            // Keyboard state is best effort; the following tap will fail loudly if something is really wrong.
        }
    }
}
