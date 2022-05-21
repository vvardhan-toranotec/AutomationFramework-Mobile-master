package org.tora.generic;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.URL;
import java.util.Properties;

public class Base {
    public static AppiumDriverLocalService service;
    public static AndroidDriver  androidDriver;

    public AppiumDriverLocalService startServer()
    {

        boolean flag=	checkIfServerIsRunning(4723);
        if(!flag)
        {

            /*service=AppiumDriverLocalService.buildDefaultService();
            service.start();*/
            service = new AppiumServiceBuilder()
                    .withIPAddress("127.0.0.1")
                    .usingPort(4723)
                    .build();
        }
        return service;

    }

    public static boolean checkIfServerIsRunning(int port) {

        boolean isServerRunning = false;
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(port);

            serverSocket.close();
        } catch (IOException e) {
            //If control comes here, then it means that the port is in use
            isServerRunning = true;
        } finally {
            serverSocket = null;
        }
        return isServerRunning;
    }

    public static AndroidDriver capabilities(String appName) throws IOException {
        //System.getProperty("user.dir");
        FileInputStream fis = new FileInputStream(System.getProperty("user.dir") + "/src/global.properties");
        Properties prop = new Properties();
        prop.load(fis);
        File appDir = new File("src");
        File app = new File(appDir, prop.getProperty(appName));
        DesiredCapabilities cap = new DesiredCapabilities();
        cap.setCapability(MobileCapabilityType.DEVICE_NAME, prop.getProperty("device") );
        //cap.setCapability(MobileCapabilityType.DEVICE_NAME, "Android Device");
        cap.setCapability(MobileCapabilityType.APP, app.getAbsolutePath());
        cap.setCapability(MobileCapabilityType.AUTOMATION_NAME, "uiautomator2");
        cap.setCapability("commandTimeout", 12000);
        URL url = new URL("http://127.0.0.1:4723/wd/hub");
        //URL url = new URL("http://0.0.0.0:4723/wd/hub");
        androidDriver = new AndroidDriver(url, cap);
        return androidDriver;

    }

    public static void getScreenShot(String testCaseName) throws IOException {
        File srcFile = ((TakesScreenshot)androidDriver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(srcFile,new File(System.getProperty("user.dir")+"/src/test/Evidence/"+testCaseName+".png"));
    }
}

