package org.tora.testRunner;


import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.AutomationName;
import io.appium.java_client.remote.IOSMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tora.DeviceOptions;
import org.tora.generic.BaseAll;
import org.tora.utility.PropertyConnection;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.Properties;

public class Hooks extends BaseAll {

    private final Logger logger = LoggerFactory.getLogger(Hooks.class);

    @Before
    public void beforeScenario(Scenario scenario) throws Exception {
        logger.info("The test is just starting...");
        logger.info("The scenario: " + scenario.getName() + " test is BEGINNING...");
        Properties properties = new PropertyConnection().getProperties("global.properties");
        String platForm = properties.getProperty("deviceRealA");
        String appName = properties.getProperty("GeneralStoreApp");
        appiumDriver = initializeDriver(DeviceOptions.lookup(platForm).toString(), appName);
    }

    @After
    public void afterScenario(Scenario scenario) {
        logger.info("The test is almost completed...");
        logger.info("The scenario: " + scenario.getName() + " has FINISHED...");
        System.out.println(scenario.getSourceTagNames());
    }

    public static AppiumDriver initializeDriver(String platformName, String appName) throws Exception {
        DesiredCapabilities cap = new DesiredCapabilities();
        cap.setCapability(MobileCapabilityType.PLATFORM_NAME, platformName);
        URL url = new URL("http://127.0.0.1:4723/wd/hub");

        switch (platformName) {
            case "Android":
                FileInputStream fis = new FileInputStream(System.getProperty("user.dir") + "/src/global.properties");
                Properties prop = new Properties();
                prop.load(fis);
                File appDir = new File("src/main/resources/Apps/");
                File app = new File(appDir, appName);
                cap.setCapability(MobileCapabilityType.DEVICE_NAME, prop.getProperty("deviceEmu"));
                cap.setCapability(MobileCapabilityType.APP, app.getAbsolutePath());
                cap.setCapability(MobileCapabilityType.AUTOMATION_NAME, "uiautomator2");
                cap.setCapability("commandTimeout", 12000);
                return new AndroidDriver(url, cap);

            case "iOS":
                cap.setCapability(MobileCapabilityType.PLATFORM_VERSION, "15.4");
                cap.setCapability(MobileCapabilityType.DEVICE_NAME, "iPhone 13 Pro");
                cap.setCapability(MobileCapabilityType.AUTOMATION_NAME, AutomationName.IOS_XCUI_TEST);
                cap.setCapability(IOSMobileCapabilityType.WDA_LAUNCH_TIMEOUT, 50000);
                cap.setCapability("commandTimeout", 12000);
                cap.setCapability(MobileCapabilityType.APP, "/Users/brushabhanumohapatra/Library/Developer/Xcode/DerivedData/UIKitCatalog-aoxcjftgrmkhocdjgopnvzzdbrgz/Build/Products/Debug-iphonesimulator/UIKitCatalog.app");
                return new IOSDriver(url, cap);
            default:
                throw new Exception("Invallid platform");
        }
    }

    // ************** Many capabilities  will be changed as per cloud service information ******************
    public static AppiumDriver initializeDriverCloud(String platformName, String appName) throws Exception {
        DesiredCapabilities cap = new DesiredCapabilities();
        cap.setCapability(MobileCapabilityType.PLATFORM_NAME, platformName);
        // URL will be changed as provided by cloud service
        URL url = new URL("http://127.0.0.1:4723/wd/hub");

        switch (platformName) {
            case "Android":
                FileInputStream fis = new FileInputStream(System.getProperty("user.dir") + "/src/global.properties");
                Properties prop = new Properties();
                prop.load(fis);
                File appDir = new File("src");
                File app = new File(appDir, prop.getProperty(appName));
                cap.setCapability(MobileCapabilityType.DEVICE_NAME, prop.getProperty("deviceEmu"));
                cap.setCapability(MobileCapabilityType.APP, app.getAbsolutePath());
                cap.setCapability(MobileCapabilityType.AUTOMATION_NAME, "uiautomator2");
                cap.setCapability("commandTimeout", 12000);
                return new AndroidDriver(url, cap);

            case "iOS":
                cap.setCapability(MobileCapabilityType.PLATFORM_VERSION, "15.4");
                cap.setCapability(MobileCapabilityType.DEVICE_NAME, "iPhone 13 Pro");
                cap.setCapability(MobileCapabilityType.AUTOMATION_NAME, AutomationName.IOS_XCUI_TEST);
                cap.setCapability(IOSMobileCapabilityType.WDA_LAUNCH_TIMEOUT, 50000);
                cap.setCapability("commandTimeout", 12000);
                cap.setCapability(MobileCapabilityType.APP, "/Users/brushabhanumohapatra/Library/Developer/Xcode/DerivedData/UIKitCatalog-aoxcjftgrmkhocdjgopnvzzdbrgz/Build/Products/Debug-iphonesimulator/UIKitCatalog.app");
                return new IOSDriver(url, cap);
            default:
                throw new Exception("Invallid platform");
        }
    }
}
