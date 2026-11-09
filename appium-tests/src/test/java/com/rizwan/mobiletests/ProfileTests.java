package com.rizwan.mobiletests;

import static com.rizwan.mobiletests.Config.VALID_EMAIL;
import static com.rizwan.mobiletests.Config.VALID_PASSWORD;

import com.rizwan.mobiletests.pages.HomePage;
import com.rizwan.mobiletests.pages.LoginPage;
import com.rizwan.mobiletests.pages.ProfilePage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ProfileTests extends BaseTest {

    private ProfilePage signInAndOpenProfile() {
        new LoginPage(driver).login(VALID_EMAIL, VALID_PASSWORD);
        HomePage home = new HomePage(driver);
        Assert.assertTrue(home.isDisplayed(), "Precondition: user should be signed in");
        home.openProfile();
        ProfilePage profile = new ProfilePage(driver);
        Assert.assertTrue(profile.isDisplayed(), "Precondition: profile form should be open");
        return profile;
    }

    @Test(description = "User can save a display name")
    public void userCanSaveProfileName() {
        ProfilePage profile = signInAndOpenProfile();
        profile.saveName("Rizwan QA");
        Assert.assertTrue(profile.savedMessageVisible(), "Success message should be displayed");
        Assert.assertTrue(profile.savedNameShown("Rizwan QA"), "Confirmation should echo the saved name");
    }

    @Test(description = "Blank name is rejected")
    public void blankProfileNameIsRejected() {
        ProfilePage profile = signInAndOpenProfile();
        profile.saveBlank();
        Assert.assertEquals(profile.errorText(), "Name is required");
        Assert.assertTrue(profile.savedMessageAbsent(), "Success message must not appear for a blank name");
    }

    @Test(description = "Whitespace only name is treated as blank")
    public void whitespaceOnlyNameIsRejected() {
        ProfilePage profile = signInAndOpenProfile();
        profile.saveName("    ");
        Assert.assertEquals(profile.errorText(), "Name is required");
    }

    @Test(description = "Back button returns to the home screen without saving")
    public void backReturnsToHome() {
        ProfilePage profile = signInAndOpenProfile();
        profile.back();
        Assert.assertTrue(new HomePage(driver).isDisplayed(), "Home screen should be visible after Back");
    }

    @Test(description = "A saved name is pre-filled when the profile is reopened")
    public void savedNameIsPrefilledOnReopen() {
        ProfilePage profile = signInAndOpenProfile();
        profile.saveName("Rizwan QA");
        Assert.assertTrue(profile.savedMessageVisible(), "Precondition: name should be saved");

        profile.backToHomeFromConfirmation();
        HomePage home = new HomePage(driver);
        Assert.assertTrue(home.isDisplayed(), "Home screen should be visible after confirmation");
        home.openProfile();
        Assert.assertEquals(new ProfilePage(driver).nameFieldValue(), "Rizwan QA");
    }
}
