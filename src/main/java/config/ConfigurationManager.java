package config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigurationManager {

    private static ConfigurationManager instance;
    private final Properties properties = new Properties();

    private ConfigurationManager() {
        try {
            String configPath = "src/test/resources/config.properties";
            FileInputStream input = new FileInputStream(configPath);
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load configuration file", e);
        }
    }

    public static ConfigurationManager getInstance() {
        if (instance == null) {
            synchronized (ConfigurationManager.class) {
                if (instance == null) {
                    instance = new ConfigurationManager();
                }
            }
        }
        return instance;
    }

    public String get(String key) {
        String envValue = System.getenv(key);
        if (envValue != null) return envValue;

        return properties.getProperty(key);
    }

    public String getBaseUrl() {
        return get("base.url");
    }

    public int getTimeout() {
        return Integer.parseInt(get("timeout"));
    }

}
