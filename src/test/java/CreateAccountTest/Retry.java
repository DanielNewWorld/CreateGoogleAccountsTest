package CreateAccountTest;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class Retry implements IRetryAnalyzer {
    int count = 0;
    int maxTry = 1; // this is where you can change on how many times it will re-Run the test when it Failed

    public Retry(){
    }

    public boolean retry(ITestResult result) {
        if (this.count < this.maxTry){
            ++this.count;
            return true;
        }else {
            return false;
        }
    }
}
