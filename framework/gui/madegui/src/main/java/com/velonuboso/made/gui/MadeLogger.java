/*
 * Copyright 2013 Rubén Héctor García <raiben@gmail.com>.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *  
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *  
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.velonuboso.made.gui;

import javafx.stage.Stage;
import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialogs;

/**
 *
 * @author raiben@gmail.com
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
