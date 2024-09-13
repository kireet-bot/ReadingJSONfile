package org.example;
import java.io.FileNotFoundException;
import java.net.URL;
import java.io.InputStream;
import java.util.Enumeration;

public class PathCheck {
    public static void main(String[] args) throws Exception {
        // Print all resources available to ClassLoader
        ClassLoader classLoader = PathCheck.class.getClassLoader();
        Enumeration<URL> resources = classLoader.getResources(".");
        while (resources.hasMoreElements()) {
            System.out.println("Resource: " + resources.nextElement());
        }

        // Load input.json from resources folder
        InputStream inputStream = classLoader.getResourceAsStream("input.json");
        if (inputStream == null) {
            throw new FileNotFoundException("File not found: input.json");
        }

    }
}
