/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.velonuboso.made.gui;

import javafx.stage.Stage;
import org.controlsfx.dialog.Dialog;

/**
 *
 * @author Ruben
 */
public class MadeLogger {

    private static MadeLogger instance;
    private Stage stage = null;

    private MadeLogger() {
    }

    public static MadeLogger getInstance() {
        if (instance == null){
            instance = new MadeLogger();
        }
        return instance;
    }
    
    public void info(String message) {
        System.out.println("INFO: "+message);
    }

    public void error(String message) {
        stage.centerOnScreen();
        System.out.println("ERROR: "+message);
        Dialog d = new Dialog(stage, "Error");
        d.setContent(message);
        d.show();
    }

    public void warning(String message) {
     System.out.println("WARNING: "+message);}

    public void configure(Stage stage) {
        this.stage = stage;
    }
    
}
