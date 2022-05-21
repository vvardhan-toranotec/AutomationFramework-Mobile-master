package org.tora;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum DeviceOptions {
    Android, iOS;

    public static final Logger log = LoggerFactory.getLogger(DeviceOptions.class);

    /**
     * allows the browser selected to be passed in with a case insensitive name
     *
     * @param b - the string name of the browser
     * @return Browser: the enum version of the browser
     * @throws Exception If a browser that is not one specified in the
     *                                Selenium.Browser class is used, this exception will be thrown
     */
    public static DeviceOptions lookup(String b) throws Exception {
        for (DeviceOptions device : DeviceOptions.values()) {
            if (device.name().equalsIgnoreCase(b)) {
                return device;
            }
        }
        throw new Exception("The selected device " + b + " is not an applicable choice");
    }

    public static DeviceOptions getDevice() {
        DeviceOptions device = DeviceOptions.Android;
        if (System.getProperty("device") != null) {
            try {
                device = DeviceOptions.lookup(System.getProperty("device"));
            } catch (Exception e) {
                log.warn("Provided device does not match options. Using ANDROID instead. " + e);
            }
        }
        return device;
    }
}
