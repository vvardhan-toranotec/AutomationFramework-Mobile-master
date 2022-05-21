package org.tora.generic;

import io.appium.java_client.AppiumDriver;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import java.io.File;
import java.io.IOException;

public class BaseAll{

    public static AppiumDriver appiumDriver;

    public static void getScreenShot(String testCaseName) throws IOException {
        File srcFile = ((TakesScreenshot)appiumDriver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(srcFile,new File(System.getProperty("user.dir")+"/src/test/Evidence/"+testCaseName+".png"));
    }
}