package com.rizwan.mobiletests;

import static com.rizwan.mobiletests.Config.VALID_EMAIL;
import static com.rizwan.mobiletests.Config.VALID_PASSWORD;

import com.rizwan.mobiletests.pages.HomePage;
import com.rizwan.mobiletests.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class NavigationTests extends BaseTest {

    @Test(description = "App starts on the login screen")
    public void appStartsOnLoginScreen() {
        Assert.assertTrue(new LoginPage(driver).isDisplayed(), "Login screen should be the entry point");
    }

    @Test(description = "User can log out and return to the login screen")
    public void userCanLogOutAndReturnToLogin() {
        new LoginPage(driver).login(VALID_EMAIL, VALID_PASSWORD);
        HomePage home = new HomePage(driver);
        Assert.assertTrue(home.isDisplayed(), "Precondition: user should be signed in");

        home.logout();
        Assert.assertTrue(new LoginPage(driver).isDisplayed(), "Login screen should be shown after logout");
    }

    @Test(description = "After logout the previous credentials are not retained and login still validates")
    public void loginRequiredAgainAfterLogout() {
        new LoginPage(driver).login(VALID_EMAIL, VALID_PASSWORD);
        HomePage home = new HomePage(driver);
        Assert.assertTrue(home.isDisplayed(), "Precondition: user should be signed in");
        home.logout();

        LoginPage login = new LoginPage(driver);
        Assert.assertTrue(login.isDisplayed(), "Precondition: login screen should be shown");
        login.submit();
        Assert.assertEquals(login.errorText(), "Invalid email or password");
    }
}
