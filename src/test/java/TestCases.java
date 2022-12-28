import com.microsoft.playwright.*;
import org.testng.Assert;
import org.testng.annotations.*;

import java.nio.file.Paths;

public class TestCases {
    static Playwright playwright;
    static Browser browser;
    BrowserContext context;
    Page page;
    @BeforeSuite
    static void launchBrowser() {
        playwright = Playwright.create();
        new BrowserType.LaunchOptions().setSlowMo(290);
        browser = playwright.firefox().launch(new BrowserType.LaunchOptions().setHeadless(false));
    }
    @BeforeTest
    void createContextAndPage() {
        context = browser.newContext(new Browser.NewContextOptions().setViewportSize(1920,1080));
        page = context.newPage();
    }

    @AfterTest
    void closeContext() {
        context.close();
    }
    @Test
    public void test01() {
        try {
            page.navigate("https://www.wikipedia.org/");
            page.locator("input[name=\"search\"]").click();
            page.locator("input[name=\"search\"]").fill("playwright");
            page.locator("input[name=\"search\"]").press("Enter");
            Assert.assertEquals("https://en.wikipedia.org/wiki/Playwright", page.url());
            page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get("image.png")));
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Test
    void shouldClickButton() {
        page.navigate("data:text/html,<script>var result;</script><button onclick='result=\"Clicked\"'>Go</button>");
        page.locator("button").click();
        Assert.assertEquals("Clicked", page.evaluate("result"));
    }

    @Test
    void shouldCheckTheBox() {
        page.setContent("<input id='checkbox' type='checkbox'></input>");
        page.locator("input").check();
        Assert.assertTrue((Boolean) page.evaluate("() => window['checkbox'].checked"));
        System.out.println("test shouldCheckTheBox done !");
    }
    @Test
    public void AmazonSearchExample(){

        page.navigate("https://www.amazon.com/");

        page.fill("#twotabsearchtextbox","Superman");
        page.click("input[id=nav-search-submit-button]");

        String text = page.textContent("span.a-color-state");
        Assert.assertTrue(text.contains("Superman"));
    }
    @AfterSuite
    static void closeBrowser() {
        playwright.close();
    }
}
