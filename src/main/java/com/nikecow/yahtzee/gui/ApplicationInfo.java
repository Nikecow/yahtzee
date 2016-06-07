package com.nikecow.yahtzee.gui;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

public class ApplicationInfo {
    private static ApplicationInfo ourInstance = new ApplicationInfo();
    private static Attributes attributesMap;

    private ApplicationInfo() {
        setGlobalVariables();
    }

    public static ApplicationInfo getInstance() {
        return ourInstance;
    }

    public static String getGlobalVariables(String property) {
        return (attributesMap.getValue(property));
    }

    private void setGlobalVariables() {
        URLClassLoader cl = (URLClassLoader) getClass().getClassLoader();
        try {
            URL url = cl.findResource("META-INF/MANIFEST.MF");
            Manifest manifest = new Manifest(url.openStream());
            attributesMap = manifest.getMainAttributes();
        } catch (IOException e) {
            System.out.println("Exception thrown  :" + e);
        }
    }
}