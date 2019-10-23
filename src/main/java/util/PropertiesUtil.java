package util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtil {

    private Properties properties;

    public Properties getProperties(String fileName) throws IOException {
        InputStream inputStream = null;
        this.properties = new Properties();

        try {
            inputStream = getClass().getClassLoader().getResourceAsStream(fileName);

            if (inputStream != null) {
                this.properties.load(inputStream);
            } else throw new FileNotFoundException("property file '" + fileName + "' not found in the classpath");
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        } finally {
            inputStream.close();
        }

        return this.properties;
    }
}
