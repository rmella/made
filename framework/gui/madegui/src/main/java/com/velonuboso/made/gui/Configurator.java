/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.velonuboso.made.gui;

import com.sun.javafx.fxml.PropertyNotFoundException;
import com.velonuboso.made.gui.MadeLogger;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.HashSet;
import java.util.Properties;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author Ruben
 */
public class Configurator {

    public static final String MADEGUI_PROPERTIES = "madegui.properties";

    private static Configurator instance = null;
    private Properties prop = null;
    private Properties propLocal = null;
    private Properties propBase = null;
    private File f = null;
    private boolean baseFound = false;
    private boolean localFound = false;

    private Configurator() {
        prop = new Properties();
        propLocal = new Properties();
        propBase = new Properties();
    }

    public static Configurator getInstance() {
        if (instance == null) {
            instance = new Configurator();
        }
        return instance;
    }

    public void autoConfigure() throws Exception {

        prop = new Properties();

        String basepath = System.getProperty("user.dir");

        URL url = this.getClass().getClassLoader().getResource(MADEGUI_PROPERTIES);
        if (url != null) {
            propBase.load(url.openStream());
            localFound = true;
        }
        f = new File(basepath + File.separator + MADEGUI_PROPERTIES);
        if (f.exists()) {
            propLocal.load(new FileInputStream(f));
            baseFound = true;
        }

        if (localFound) {
            copyProperties(propLocal, prop);
        } else if (baseFound) {
            copyProperties(propBase, prop);
            storeProperties();
            MadeLogger.getInstance().infoLong(
                    "Is this the first time you run the application?\n",
                    "No local configuration found."
                    + "The default configuration has been "
                    + "copied to your home");
        }

        if (!baseFound) {
            throw new PropertyNotFoundException("Base properties not found");
        }

    }

    private void copyProperties(Properties source, Properties target) {
        for (String key : source.stringPropertyNames()) {
            target.put(key, source.getProperty(key));
        }
    }

    public String get(String key) throws PropertyNotFoundException {
        String ret = prop.getProperty(key);
        if (ret == null) {
            ret = propBase.getProperty(key);
            if (ret == null){
                throw new PropertyNotFoundException("Property \'"+key+"\' not found ");
            }else{
                prop.put(key, propBase.getProperty(key));
                try{
                    storeProperties();
                }catch(Exception e){
                    throw new PropertyNotFoundException(e);
                }
            }
        }
        return ret;
    }
    
    public void set(String key, String value) throws Exception {
        prop.setProperty(key, value);
        try{
            storeProperties();
        }catch(Exception e){
            throw new PropertyNotFoundException(e);
        }
    }

    public void storeProperties() throws FileNotFoundException, IOException {
        prop.store(new FileOutputStream(f), new Date().toString());
    }
    public void storeProperties(File fTarget) throws FileNotFoundException, IOException {
        prop.store(new FileOutputStream(fTarget), new Date().toString());
    }
    
    public void loadProperties(File fSource) throws FileNotFoundException, IOException {
        prop.load(new FileInputStream(fSource));
        prop.store(new FileOutputStream(f), new Date().toString());
    }

}
