package pageObjects;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class CreateAccountPage {
    public String newNameText, newLastnameText, newEmailText, newPasswordText, conformationText, existingEmailText;
    By firstNameField = By.xpath("//input[@id='firstName']");
    By lastNameField = By.xpath("//input[@id='lastName']");
    By usernameField = By.xpath("//input[@id='username']");
    By usernameIsTakenError = By.xpath("//div[contains(@aria-live,'assertive') and @aria-atomic='true']");
    By usernameDomain = By.xpath("//span[contains(@id,'domain')]");
    By passwordField = By.xpath("//input[@type='password' and @aria-label='Password']");
    By confirmField = By.xpath("//input[@type='password' and @aria-label='Confirm']");
    By showPasswordCheckbox = By.xpath("//div[contains(@id,'selection')]");
    By passwordIsShown = By.xpath("//input[@type='text' and @autocomplete='off' and @name='Passwd']");
    By passwordIsHidden = By.xpath("//input[@type='password' and @autocomplete='new-password' and @name='Passwd']");
    WebDriver driver;
    WebDriverWait wait;
    JavascriptExecutor javascriptExecutor;
    public CreateAccountPage(WebDriver driver){
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }
    public WebElement getFirstNameField(){return driver.findElement(firstNameField); }
    public WebElement getLastNameField(){return driver.findElement(lastNameField); }
    public WebElement getUsernameField(){return driver.findElement(usernameField); }
    public WebElement getUsernameDomain(){return driver.findElement(usernameDomain); }
    public WebElement getPasswordField(){return driver.findElement(passwordField); }
    public WebElement getConfirmPassField(){return driver.findElement(confirmField); }
    public WebElement getShowPasswordCheckbox(){return driver.findElement(showPasswordCheckbox); }
    public WebElement getPasswordIsShown(){return driver.findElement(passwordIsShown); }
    public WebElement getOptionToUseExistingEmail(){return driver.findElements(By.xpath("//button[@type='button']")).get(0); }
    public WebElement getSignInIntoExistingAccount(){return driver.findElements(By.xpath("//button[@type='button']")).get(2); }
    public WebElement getNextButton(){return driver.findElements(By.xpath("//button[@type='button']")).get(1); }
    public WebElement getPasswordIsHidden(){return driver.findElement(passwordIsHidden); }
    public WebElement getUsernameIsTakenErrorMsg(){ return driver.findElements(usernameIsTakenError).get(0); }

    // This method Creates google Account
    public void submitCreateNewAccountForm(String firstName, String lastName, String password){
        wait.until(ExpectedConditions.visibilityOf(getFirstNameField())); //wait for first name field
        getFirstNameField().sendKeys(firstName); // Entering "First name" in the field
        wait.until(ExpectedConditions.textToBePresentInElementValue(getFirstNameField(), firstName)); // wait for first name to be entered
        getLastNameField().sendKeys(lastName); // Entering "Last name" in the field
        wait.until(ExpectedConditions.attributeToBeNotEmpty(getUsernameField(), "data-initial-value"));
        newNameText = getFirstNameField().getAttribute("data-initial-value"); //verify Name field input
        newLastnameText = getLastNameField().getAttribute("data-initial-value"); // verify Last name field input
        newEmailText = getUsernameField().getAttribute("data-initial-value") + getUsernameDomain().getText(); // verify Password field input
        getPasswordField().sendKeys(password); // Entering password in "Password" field
        wait.until(ExpectedConditions.attributeToBeNotEmpty(getPasswordField(), "data-initial-value"));
        newPasswordText = getPasswordField().getAttribute("data-initial-value"); // verify Password field input
        getConfirmPassField().sendKeys(password); // Entering password in "Confirm" field
        wait.until(ExpectedConditions.attributeToBeNotEmpty(getConfirmPassField(), "data-initial-value"));
        conformationText =  getConfirmPassField().getAttribute("data-initial-value"); // verify Conformation field input
        wait.until(ExpectedConditions.visibilityOf(getShowPasswordCheckbox()));
        javascriptExecutor = (JavascriptExecutor) driver;
        try {
            getShowPasswordCheckbox().sendKeys(Keys.ENTER);
        } catch (Exception e) {
            javascriptExecutor.executeScript("arguments[0].click();", getShowPasswordCheckbox());
        }
    }
    // This method will verify Option existing Email
    public void optionToUseExistingEmail(String existingEmail){
        wait.until(ExpectedConditions.elementToBeClickable(getOptionToUseExistingEmail()));
        getOptionToUseExistingEmail().click(); // clicking on "Use my current email address instead"
        wait.until(ExpectedConditions.textToBePresentInElementValue(getUsernameField(), ""));
        getUsernameField().sendKeys(existingEmail);
        existingEmailText = getUsernameField().getAttribute("data-initial-value");
    }
}
