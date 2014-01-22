/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.velonuboso.made.gui;

import javafx.stage.Stage;
import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialogs;

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
        if (instance == null) {
            instance = new MadeLogger();
        }
        return instance;
    }

    
    public void infoLong(String master, String message) {
        System.out.println("INFO: " + message);
        Dialogs.create()
                    .title("Info")
                    .masthead(master)
                    .message(message)
                    .showInformation();
    }
    
    public void info(String message) {
        System.out.println("INFO: " + message);
        Dialogs.create()
                    .title("Info")
                    .message(message)
                    .showInformation();
    }

    public void error(String message, Exception e) {
        System.out.println("ERROR: " + message);
        Action response = Dialogs.create()
                    .title("Error")
                    .showException(e);
    }

    public void warning(String message) {
        System.out.println("WARNING: " + message);
    }

    public void configure(Stage stage) {
        this.stage = stage;
    }

}
