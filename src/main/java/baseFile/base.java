package baseFile;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;

public class base {
    public WebDriver driver;
    public WebDriverWait wait;
    public Properties properties;
    public String currentURL;

    public WebDriver initializeDriver() throws IOException {
        properties = new Properties();
        FileInputStream fileInputStream = new FileInputStream(System.getProperty("user.dir") + "\\src\\main\\resources\\data.properties");
        properties.load(fileInputStream);
        String browserName = System.getProperty("browser") != null ? System.getProperty("browser") : properties.getProperty("browser");
        currentURL = properties.getProperty("createAccountURL"); // here we are passing URL that we are testing
        System.out.println("You are using browser = "+browserName);
        switch (browserName) {
            case "chrome" -> {
                //WebDriverManager.chromedriver().setup();
                System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "\\drivers\\chromedriver.exe");
                ChromeOptions options = new ChromeOptions();
                options.addArguments("--incognito"); // we are opening browser in Incognito mode
                DesiredCapabilities cap = new DesiredCapabilities();
                cap.setCapability(ChromeOptions.CAPABILITY, options);
                options.merge(cap);
                driver = new ChromeDriver(options); // here we are passing Incognito mode to the driver
                wait = new WebDriverWait(driver, Duration.ofSeconds(30)); // here we are setting Wait time for up to 30 sec
            }
            case "firefox" -> {
                WebDriverManager.firefoxdriver().setup(); // in case if we want to use FireFox browser
                driver = new FirefoxDriver();
                wait = new WebDriverWait(driver, Duration.ofSeconds(30));
            }
            case "edge" -> {
                //WebDriverManager.edgedriver().setup();
                System.setProperty("webdriver.edge.driver", System.getProperty("user.dir") + "\\drivers\\msedgedriver.exe");
                driver = new EdgeDriver();
                wait = new WebDriverWait(driver, Duration.ofSeconds(30));
            }
        }
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
        driver.manage().window().maximize();
        return driver;
    }

    public String getScreenShotPath(String testCaseName, WebDriver driver) throws IOException {
        TakesScreenshot ts = (TakesScreenshot) driver;
        File source = ts.getScreenshotAs(OutputType.FILE);
        File destinationFile = new File(System.getProperty("user.dir") + "\\reports\\" + testCaseName + ".png");
        FileUtils.copyFile(source, destinationFile);
        return System.getProperty("user.dir") + "\\reports\\" + testCaseName + ".png";
    }
}
