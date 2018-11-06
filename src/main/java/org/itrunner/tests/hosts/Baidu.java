package org.itrunner.tests.hosts;

import org.itrunner.tests.TestBase;
import org.junit.Test;
import org.openqa.selenium.By;

import static org.itrunner.tests.utils.Config.CONFIG;

public class Baidu extends TestBase {

    @Override
    @Test
    public void test() {
        open(CONFIG.getBaiduUrl());

        waitForTitlePresent("百度一下，你就知道");

        nextStep(() -> {
            driver.findElement(By.linkText("登录")).click();
            driver.findElement(By.id("TANGRAM__PSP_10__footerULoginBtn")).click();
            driver.findElement(By.id("TANGRAM__PSP_10__userName")).sendKeys(CONFIG.getBaiduUsername());
            driver.findElement(By.id("TANGRAM__PSP_10__password")).sendKeys(CONFIG.getBaiduPassword());
            driver.findElement(By.id("TANGRAM__PSP_10__submit")).click();
            takeScreenshot("baidu");
        }, "login");
    }
}