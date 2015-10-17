/*
 * Copyright (C) 2015 Rubén Héctor García (raiben@gmail.com)
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
package com.velonuboso.made.core;

import com.velonuboso.made.core.experiments.implementation.BaseExperiment;
import com.velonuboso.made.core.experiments.implementation.ExperimentEvostar2016;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import org.reflections.Reflections;

/**
 *
 * @author Rubén Héctor García (raiben@gmail.com)
 */
public class Runner {

    private static final String ARGUMENT_EXPERIMENT = "experiment";
    private static final String ARGUMENT_EXPERIMENT_LIST = "experimentList";
    private static final String ARGUMENT_HELP = "help";

    private String[] arguments;
    
    private static Class<? extends BaseExperiment>[] experimentsAvailable = new Class[]{
        ExperimentEvostar2016.class
    };

    public static void main(String[] arguments) {
        Runner runner = new Runner(arguments);
        runner.run();
    }

    public Runner(String[] arguments) {
        this.arguments = arguments;
    }

    public void run() {
        Reflections.log = null;

        printVersion();

        OptionParser parser = buildOptionParser();

        OptionSet options = parser.parse(arguments);
        if (options.has(ARGUMENT_EXPERIMENT)) {
            runExperiment(options.valueOf(ARGUMENT_EXPERIMENT).toString());
            System.exit(0);
        }
        if (options.has(ARGUMENT_EXPERIMENT_LIST)) {
            printExperimentList();
            System.exit(0);
        }
        if (options.has(ARGUMENT_HELP)) {
            printHelp(parser);
            System.exit(0);
        }
        printHelp(parser);
    }

    private OptionParser buildOptionParser() {
        OptionParser parser = new OptionParser();
        parser.accepts(ARGUMENT_EXPERIMENT, "Pre selected experiment").withRequiredArg().ofType(String.class);
        parser.accepts(ARGUMENT_EXPERIMENT_LIST, "Retrieves the current list of available experiments");
        parser.accepts(ARGUMENT_HELP, "Displays this help").forHelp();
        parser.allowsUnrecognizedOptions();
        return parser;
    }

    private void printExperimentList() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Experiment list:\n");

        Arrays.stream(experimentsAvailable).forEach((experimentClass) -> {
            try {
                BaseExperiment experiment = (BaseExperiment) experimentClass.getConstructor().newInstance();
                String line = experiment.getCodeName() + ": " + experiment.getDescription();
                stringBuilder.append("\t" + line + "\n");
            } catch (Exception e) {
            }
        });
        System.out.println(stringBuilder.toString());
    }

    private void printVersion() {
        String title = Runner.class.getPackage().getImplementationTitle();
        String version = Runner.class.getPackage().getImplementationVersion();
        System.out.println("Running MADE " + title + " v" + version + " ...");
    }

    private void printHelp(OptionParser parser) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            parser.printHelpOn(byteArrayOutputStream);
            System.out.println(byteArrayOutputStream.toString());
        } catch (IOException ex) {
            Logger.getLogger(Runner.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void runExperiment(String experimentCode) {
        Class experimentClass = Arrays.stream(experimentsAvailable)
                .filter((Class<? extends BaseExperiment> filteredexperimentClass) -> {
                    try {
                        return filteredexperimentClass.getConstructor().newInstance().getCodeName().compareTo(experimentCode) == 0;
                    } catch (Exception ex) {
                        Logger.getLogger(Runner.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    return false;
                })
                .findFirst()
                .orElse(null);

        if (experimentClass != null) {
            try {
                BaseExperiment experiment = (BaseExperiment) experimentClass.getConstructor().newInstance();
                experiment.run(arguments);
            } catch (Exception ex) {
                Logger.getLogger(Runner.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }
}
