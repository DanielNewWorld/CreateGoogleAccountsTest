package CreateAccountTest;
import baseFile.base;
import com.google.common.base.Ascii;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v104.emulation.Emulation;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.*;
import org.testng.annotations.Listeners;
import pageObjects.CreateAccountPage;
import java.io.IOException;
import java.util.Optional;

@Listeners(CreateAccountTest.Listeners.class)
public class CreateGoogleAccountTest extends base {
    CreateAccountPage createAccountPage;// page class from pageObject file
    JavascriptExecutor javascriptExecutor;
    boolean isPasswordValid;

    // this is the method that runs once Before any test(methods)
    @BeforeTest
    public void beforeTest() throws IOException, InterruptedException {
        driver = initializeDriver();// initializing the driver can be modified in base class -> src/main/java/baseFile/base.java
        Thread.sleep(1000);
        driver.manage().deleteAllCookies(); // Cleaning the browser before test
        Thread.sleep(2000);
        driver.navigate().refresh();
    }

    // this is method that runs before Each Test(before each method in test)
    @BeforeMethod(alwaysRun = true)
    public void beforeMethod() throws InterruptedException {
        createAccountPage = new CreateAccountPage(driver);// page object class can be changed -> src/main/java/pageObjects/CreateAccountPage.java
        driver.get(currentURL); // Current URL in test can be modified in base class or -> src/main/resources/data.properties
        Thread.sleep(2000); // little wait time
    }

    // First name starts with capital
    @Test(priority = 1, retryAnalyzer = Retry.class)// Retry will re-Run the test 1 more time if failed
    public void verifyFirstName() {
        // Name, Lastname, Password can be modified in -> src/main/resources/data.properties
        createAccountPage.submitCreateNewAccountForm(properties.getProperty("firstName"), properties.getProperty("lastName"), properties.getProperty("password"));
        Assert.assertTrue(Ascii.isUpperCase(createAccountPage.newNameText.charAt(0)));// This will verify that Name starts from capital
    }

    // Verify that Last name starts with capital
    @Test(priority = 2, retryAnalyzer = Retry.class)
    public void verifyLastName() {
        createAccountPage.submitCreateNewAccountForm(properties.getProperty("firstName"), properties.getProperty("lastName"), properties.getProperty("password"));
        Assert.assertTrue(Ascii.isUpperCase(createAccountPage.newLastnameText.charAt(0)));// This will verify Last name starts from capital
    }

    // Verify that Username must end in @gmail.com
    @Test(priority = 3, retryAnalyzer = Retry.class)
    public void verifyUsername() {
        createAccountPage.submitCreateNewAccountForm(properties.getProperty("firstName"), properties.getProperty("lastName"), properties.getProperty("password"));
        Assert.assertTrue(createAccountPage.newEmailText.contains("@gmail.com"));// verifying new email contains @gmail.com
    }

    // This method will verify Using Option to Use Existing email
    @Test(priority = 4, retryAnalyzer = Retry.class)
    public void verifyOptionToUseExistingEmail() {
        createAccountPage.submitCreateNewAccountForm(properties.getProperty("firstName"), properties.getProperty("lastName"), properties.getProperty("password"));
        Assert.assertTrue(createAccountPage.newEmailText.contains("@gmail.com"));// verifying new email contains @gmail.com
        createAccountPage.optionToUseExistingEmail("test@workemail.com");// using Option to Use Existing email
        Assert.assertEquals(createAccountPage.existingEmailText, "test@workemail.com");
    }

    // This method will verify Password and Conformation
    @Test(priority = 5, retryAnalyzer = Retry.class)
    public void verifyValidPasswordAndConfirmation() {
        createAccountPage.submitCreateNewAccountForm(properties.getProperty("firstName"), properties.getProperty("lastName"), properties.getProperty("password"));
        Assert.assertEquals(createAccountPage.newPasswordText, createAccountPage.conformationText); // verifying Password and Conformation are the same
        if (createAccountPage.newPasswordText.matches(".*[0-9]{1,}.*") &&
                createAccountPage.newPasswordText.matches(".*[@#$]{1,}.*") &&
                createAccountPage.newPasswordText.length() >= 8 &&
                createAccountPage.newPasswordText.matches(".*[A-Z]{1,}.*") &&
                createAccountPage.newPasswordText.matches(".*[a-z]{1,}.*")) {
            isPasswordValid = true;
        } else {
            isPasswordValid = false;
        }
        Assert.assertTrue(isPasswordValid);
    }

    // Verify Invalid Password
    @Test(priority = 6, retryAnalyzer = Retry.class)
    public void verifyInvalidPassword() {
        createAccountPage.submitCreateNewAccountForm(properties.getProperty("firstName"), properties.getProperty("lastName"), "Password");
        Assert.assertEquals(createAccountPage.newPasswordText, createAccountPage.conformationText); // verifying Password and Conformation are the same
        if (createAccountPage.newPasswordText.matches(".*[0-9]{1,}.*") &&
                createAccountPage.newPasswordText.matches(".*[@#$]{1,}.*") &&
                createAccountPage.newPasswordText.length() >= 8 &&
                createAccountPage.newPasswordText.matches(".*[A-Z]{1,}.*") &&
                createAccountPage.newPasswordText.matches(".*[a-z]{1,}.*")) {
            isPasswordValid = true;
        } else {
            isPasswordValid = false;
        }
        Assert.assertFalse(isPasswordValid);
    }

    // Verify that Username must end in @gmail.com
    @Test(priority = 6, retryAnalyzer = Retry.class)
    public void verifyShowOrHidePassword() {
        createAccountPage.submitCreateNewAccountForm(properties.getProperty("firstName"), properties.getProperty("lastName"), properties.getProperty("password"));
        Assert.assertTrue(createAccountPage.getPasswordIsShown().isDisplayed()); // Here we are Verifying that Password is shown
        wait.until(ExpectedConditions.elementToBeClickable(createAccountPage.getShowPasswordCheckbox())); //wait
        javascriptExecutor = (JavascriptExecutor) driver;
        try { //Here we're unchecking the checkbox
            createAccountPage.getShowPasswordCheckbox().sendKeys(Keys.ENTER); // Clicking to Hide password checkbox
        } catch (Exception e) {
            javascriptExecutor.executeScript("arguments[0].click();", createAccountPage.getShowPasswordCheckbox());
        }
        wait.until(ExpectedConditions.visibilityOf(createAccountPage.getPasswordIsHidden()));
        Assert.assertTrue(createAccountPage.getPasswordIsHidden().isDisplayed());// we are Verifying that Password is Hidden
    }

    // Verify SignIn Into Existing Account
    @Test(priority = 7, retryAnalyzer = Retry.class)
    public void verifySignInIntoExistingAccount() {
        wait.until(ExpectedConditions.elementToBeClickable(createAccountPage.getSignInIntoExistingAccount()));
        try {
            createAccountPage.getSignInIntoExistingAccount().click();
        } catch (Exception e) {
            javascriptExecutor.executeScript("arguments[0].click();", createAccountPage.getSignInIntoExistingAccount());
        }
        Assert.assertTrue(driver.getTitle().contains("Sign in"));
        Assert.assertTrue(driver.getCurrentUrl().contains("signin"));
    }

    // Verify user is able to Navigate to next page
    @Test(priority = 8, retryAnalyzer = Retry.class)
    public void verifyFirstAccountCreation() {
        createAccountPage.submitCreateNewAccountForm(properties.getProperty("firstName"), properties.getProperty("lastName"), properties.getProperty("password"));
        wait.until(ExpectedConditions.elementToBeClickable(createAccountPage.getNextButton()));//wait for Next button
        try {
            createAccountPage.getNextButton().click(); // Clicking on Next CTA after filling out the form
        } catch (Exception e) {
            javascriptExecutor.executeScript("arguments[0].click();",  createAccountPage.getNextButton());
        }
        if (driver.findElement(By.xpath("//input[contains(@id,'phoneNumber')]")).isDisplayed()) {
            wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//input[contains(@id,'phoneNumber')]"))));
            WebElement phoneNumberField = driver.findElement(By.xpath("//input[contains(@id,'phoneNumber')]"));
            Assert.assertTrue(phoneNumberField.isDisplayed()); // Verify User is in next page and Phone field displayed
        }else {
            Assert.assertTrue(driver.getCurrentUrl().contains("signup"));// or Verify user is able to Navigate to the Next page
        }
    }
    // Test we are verifying that user can access Mobile version
    @Test(priority = 9, retryAnalyzer = Retry.class)
    public void verifyCreateAccountInMobileVersion(){
        DevTools devTools = ((ChromeDriver)driver).getDevTools();
        devTools.createSession();
        devTools.send(Emulation.setDeviceMetricsOverride(390,
                844,
                100,
                true,
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty()
                ));
        driver.get(currentURL);
        createAccountPage.submitCreateNewAccountForm(properties.getProperty("firstName"), properties.getProperty("lastName"), properties.getProperty("password"));
    }
//    @AfterMethod
//    public void afterTestCase() throws InterruptedException {
//        Thread.sleep(1000);
//        driver.manage().deleteAllCookies();
//        Thread.sleep(2000);
//        driver.navigate().refresh();
//    }
//
//    @AfterTest
//    public void tearDown() throws InterruptedException {
//        Thread.sleep(2000);
//        driver.quit();
//    }
}
