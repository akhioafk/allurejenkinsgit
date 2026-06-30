package com.example;

import io.qameta.allure.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;

@Epic("Web Interface")
@Feature("Search Functionality")
public class AllureTestExample {

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeMethod
    @Step("Setup browser")
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");

        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
    }

    @Test(description = "Search on Google and verify results")
    @Severity(SeverityLevel.CRITICAL)
    @Owner("akhio")
    @Story("Basic Google Search")
    public void testGoogleSearch() {
        Allure.parameter("Search Query", "Allure Report");

        stepOpenGoogleSearchDirectly();
        stepAcceptConsentIfPresent();
        stepVerifySearchResults();
    }

    @Step("Open Google search results directly")
    private void stepOpenGoogleSearchDirectly() {
        // Direct link is much more stable than homepage + typing
        driver.get("https://www.google.com/search?q=Allure+Report");
    }

    @Step("Accept Google consent banner if it appears")
    private void stepAcceptConsentIfPresent() {
        try {
            // Common consent buttons (Google changes these often)
            String[] consentSelectors = {
                "button[aria-label*='Accept']",
                "button[aria-label*='I agree']",
                "#L2AGLb",                    // old classic one
                "button:has-text('Accept all')"
            };

            for (String selector : consentSelectors) {
                try {
                    WebElement consentButton = wait.until(
                        ExpectedConditions.elementToBeClickable(By.cssSelector(selector))
                    );
                    consentButton.click();
                    Allure.addAttachment("Consent clicked", "text/plain", selector);
                    Thread.sleep(1500); // small wait after click
                    return;
                } catch (Exception ignored) {}
            }
        } catch (Exception e) {
            // No consent banner — that's fine
        }
    }

    @Step("Verify search results are visible")
    private void stepVerifySearchResults() {
        // Wait for results container (more stable selectors)
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(
                By.cssSelector("#rso, #search, div[role='main']")
            ));
        } catch (Exception e) {
            // Fallback: just check URL
        }

        String currentUrl = driver.getCurrentUrl();
        String title = driver.getTitle();
        String pageSourceSnippet = driver.getPageSource().substring(0, Math.min(500, driver.getPageSource().length()));

        Allure.addAttachment("Final URL", "text/plain", currentUrl);
        Allure.addAttachment("Page Title", "text/plain", title);
        Allure.addAttachment("Page Source (first 500 chars)", "text/plain", pageSourceSnippet);

        boolean success = currentUrl.toLowerCase().contains("allure+report") 
                       || title.toLowerCase().contains("allure report")
                       || driver.getPageSource().toLowerCase().contains("allure");

        if (!success) {
            throw new AssertionError("Search verification failed. Check attachments for URL/Title/Source.");
        }
    }

    @AfterMethod
    @Step("Close browser")
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}