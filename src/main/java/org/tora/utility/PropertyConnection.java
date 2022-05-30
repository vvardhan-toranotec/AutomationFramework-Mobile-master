package org.tora.utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class PropertyConnection {
    public Properties getProperties(String fileName) {
        File file = new File(System.getProperty("user.dir") + "/src//" + fileName);
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        Properties properties = new Properties();
        try {
            properties.load(fileInputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return properties;
    }
}
