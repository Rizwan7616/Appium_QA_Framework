package com.rizwan.mobiletests;

import io.appium.java_client.android.AndroidDriver;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import org.openqa.selenium.OutputType;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public class BaseTest {
    protected AndroidDriver driver;

    @BeforeMethod(alwaysRun = true)
    public void setUp() throws Exception {
        driver = DriverFactory.createDriver();
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        if (driver == null) return;
        try {
            if (!result.isSuccess()) {
                saveScreenshot(result.getName());
            }
        } catch (Exception e) {
            System.err.println("Could not save failure screenshot: " + e.getMessage());
        } finally {
            driver.quit();
            driver = null;
        }
    }

    private void saveScreenshot(String testName) throws Exception {
        Path dir = Path.of(System.getProperty("screenshotDir", "target/screenshots"));
        Files.createDirectories(dir);
        String safe = testName.replaceAll("[^a-zA-Z0-9_-]", "_");
        Path destination = dir.resolve(safe + "-" + System.currentTimeMillis() + ".png");
        File source = driver.getScreenshotAs(OutputType.FILE);
        Files.copy(source.toPath(), destination, StandardCopyOption.REPLACE_EXISTING);
    }
}
