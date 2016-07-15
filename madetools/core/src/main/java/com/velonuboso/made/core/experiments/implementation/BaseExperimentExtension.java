/*
 * Copyright (C) 2016 raiben
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
package com.velonuboso.made.core.experiments.implementation;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import joptsimple.OptionParser;
import joptsimple.OptionSet;

/**
 *
 * @author raiben
 */
public abstract class BaseExperimentExtension extends BaseExperiment {

    private static final String ARGUMENT_HELP = "help";

    OptionParser parser;

    public BaseExperimentExtension() {
        parser = new OptionParser();
        
        addSpecificOptions();
        
        parser.allowsUnrecognizedOptions();
        parser.accepts(ARGUMENT_HELP, "Displays this help").forHelp();
    }

    private void addSpecificOptions() {
        HashMap<String, String> mapOfNamesAndDecriptions = getOptionsMapOfNamesAndDescriptions();
        mapOfNamesAndDecriptions.entrySet()
                .forEach(entry -> parser.accepts(entry.getKey(), entry.getValue())
                        .withRequiredArg().ofType(String.class));
    }

    @Override
    public final void run(String[] arguments) {
        OptionSet options = parser.parse(arguments);
        
        doRun(options);
        
        if (options.has(ARGUMENT_HELP)) {
            printHelp();
            System.exit(0);
        }
        printHelp();
    }
    
    private void printHelp() {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            parser.printHelpOn(byteArrayOutputStream);
            System.out.println(byteArrayOutputStream.toString());
        } catch (IOException ex) {
            Logger.getLogger(ExperimentEvostar2016_1.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public abstract HashMap<String, String> getOptionsMapOfNamesAndDescriptions();
    public abstract void doRun(OptionSet options);
}
