package com.endava.petclinic.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class EnvReader {

    private static final Properties properties = new Properties();

    static {
        InputStream is = EnvReader.class.getClassLoader().getResourceAsStream("env.properties");
        try {
            properties.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getBaseUri() {
        return properties.getProperty("baseUri");
    }

    public static Integer getPort() {
        return Integer.valueOf(properties.getProperty("port"));
    }

    public static String getBasePath() {
        return properties.getProperty("basePath");
    }

    public static String getOwnersPath() {
        return properties.getProperty("ownersPath");
    }
    public static String getPetsPath() {
        return properties.getProperty("petsPath");
    }
    public static String getTypePath() {
        return properties.getProperty("typePath");
    }


}
