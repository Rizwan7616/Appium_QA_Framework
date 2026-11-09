package com.rizwan.mobiletests;

import static com.rizwan.mobiletests.Config.VALID_EMAIL;
import static com.rizwan.mobiletests.Config.VALID_PASSWORD;

import com.rizwan.mobiletests.pages.HomePage;
import com.rizwan.mobiletests.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class LoginTests extends BaseTest {

    @Test(description = "Valid credentials open the home screen")
    public void validCredentialsOpenHomeScreen() {
        new LoginPage(driver).login(VALID_EMAIL, VALID_PASSWORD);
        Assert.assertTrue(new HomePage(driver).isDisplayed(), "Home screen should be visible after valid login");
    }

    @DataProvider(name = "invalidCredentials")
    public Object[][] invalidCredentials() {
        return new Object[][] {
            {VALID_EMAIL, "incorrect"},
            {"unknown@example.com", VALID_PASSWORD},
            {VALID_EMAIL, "password123!"},   // password is case sensitive
            {"", ""}                         // nothing entered
        };
    }

    @Test(dataProvider = "invalidCredentials", description = "Invalid credentials show a validation message and stay on login")
    public void invalidCredentialsShowError(String email, String password) {
        LoginPage login = new LoginPage(driver);
        login.login(email, password);
        Assert.assertEquals(login.errorText(), "Invalid email or password");
        Assert.assertTrue(login.isDisplayed(), "User should remain on the login screen");
    }

    @Test(description = "A failed attempt followed by correct credentials succeeds")
    public void canSignInAfterFailedAttempt() {
        LoginPage login = new LoginPage(driver);
        login.login(VALID_EMAIL, "incorrect");
        Assert.assertEquals(login.errorText(), "Invalid email or password");
        login.enterCredentials(VALID_EMAIL, VALID_PASSWORD).submit();
        Assert.assertTrue(new HomePage(driver).isDisplayed(), "Home screen should open on the second attempt");
    }
}
