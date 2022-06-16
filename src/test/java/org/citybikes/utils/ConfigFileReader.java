package org.citybikes.utils;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class ConfigFileReader {
    Properties properties;

    public Properties loadEnvironmentProperties() throws IOException {
        if (properties != null) {
            return properties;
        }
        System.out.println(ConfigFileReader.class);
        String testEnv = System.getProperty("env", "qa");
        List<String> applicableEnv = Arrays.asList("qa", "dev", "local");
        if (!applicableEnv.contains(testEnv)) {
            throw new IllegalArgumentException("Please provide a valid argument");
        }
        System.out.println("testEnv   " + testEnv);
        String fileName = "src/test/resources/configs/application-" + testEnv + ".properties";
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        new Properties().load(reader);
        properties = new Properties();
        properties.load(reader);
        System.out.println(properties);
        return properties;
    }
}
