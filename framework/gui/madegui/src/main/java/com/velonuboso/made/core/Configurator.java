/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.velonuboso.made.core;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.HashSet;
import java.util.Properties;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author Ruben
 */
public class Configurator {

    private static Configurator instance = null;

    private Configurator() {
        
    }

    public static Configurator getInstance() {
        if (instance == null) {
            instance = new Configurator();
        }
        return instance;
    }

    public void autoConfigure() throws Exception {
        Properties prop = new Properties();
        
        prop = new Properties();
        try {
            String basepath = System.getProperty("user.dir");
            File f = new File(basepath + File.separator + "madegui.conf");
            if (f.exists()) {
                prop.load(new FileInputStream(f));
            } else {
                URL url = this.getClass().getClassLoader().getResource("madegui.conf");
                if (url == null){
                    throw new Exception("Default config file not found");
                }else{
                    prop.load(url.openStream());
                    FileUtils.copyFile(new File(url.getFile()), f);
                }
            }

            HashSet<String> classNames = new HashSet<String>();
        } catch (Exception e) {
            throw e;
        }
    }

}
