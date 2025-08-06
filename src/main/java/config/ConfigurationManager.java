package config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigurationManager {

    private static ConfigurationManager instance;
    private final static Properties properties = new Properties();

    private ConfigurationManager() {
        try {
            String configPath = "src/test/resources/config.properties";
            FileInputStream input = new FileInputStream(configPath);
            properties.load(input);
        } catch (IOException e) {
            System.err.println("Warning: config.properties not found or failed to load. Relying on environment variables.");
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

    public static String get(String key) {
        // Convert "base.url" to "BASE_URL"
        String envKey = key.toUpperCase().replace(".", "_");
        String envValue = System.getenv(envKey);
        if (envValue != null && !envValue.isEmpty()) return envValue;

        return properties.getProperty(key);
    }

    public static String getBaseUrl() {
        return get("base.url");
    }
}
