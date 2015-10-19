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
package com.velonuboso.made.core.experiments.implementation;

import com.velonuboso.made.core.common.api.IGlobalConfigurationFactory;
import com.velonuboso.made.core.common.entity.CommonAbmConfiguration;
import com.velonuboso.made.core.common.entity.CommonEcConfiguration;
import com.velonuboso.made.core.common.util.ObjectFactory;
import com.velonuboso.made.core.ec.api.IGeneticAlgorithmListener;
import com.velonuboso.made.core.ec.implementation.listeners.ExcelWriterGeneticAlgorithmListener;
import com.velonuboso.made.core.inference.entity.Trope;
import com.velonuboso.made.core.optimization.api.IOptimizer;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import joptsimple.OptionParser;
import joptsimple.OptionSet;

/**
 *
 * @author Rubén Héctor García (raiben@gmail.com)
 */
public class ExperimentEvostar2016_1 extends BaseExperiment {

    private static final String ARGUMENT_TROPE = "trope";
    private static final String ARGUMENT_TROPE_LIST = "tropeList";
    private static final String ARGUMENT_HELP = "help";
    private static final String OPTION_ALL_TROPES = "ALL";

    private Trope fitnessTrope;

    public ExperimentEvostar2016_1() {
        fitnessTrope = null;
    }

    @Override
    public String getDescription() {
        return "Experiment n.1 for EvoGames 2016: The fitness function is the summary "
                + "of the target trope's occurrences.";
    }

    @Override
    public void run(String[] arguments) {

        OptionParser parser = buildOptionParser();

        OptionSet options = parser.parse(arguments);
        if (options.has(ARGUMENT_TROPE)) {
            String option = options.valueOf(ARGUMENT_TROPE).toString();
            if (option.compareTo(OPTION_ALL_TROPES) == 0) {
                run();
                System.exit(0);
            }

            try {
                fitnessTrope = Trope.valueOf(option);
            } catch (IllegalArgumentException exception) {
                System.out.println("Unrecognised option '" + option + "'");
                printHelp(parser);
                System.exit(0);
            }
            run();
            System.exit(0);
        }
        if (options.has(ARGUMENT_TROPE_LIST)) {
            printTropeList();
            System.exit(0);
        }
        if (options.has(ARGUMENT_HELP)) {
            printHelp(parser);
            System.exit(0);
        }
        printHelp(parser);
    }

    private void run() {
        installMockForExcelWriter();
        IGlobalConfigurationFactory globalConfigurationFactory
                = ObjectFactory.createObject(IGlobalConfigurationFactory.class);

        configureEcModule(globalConfigurationFactory);
        configureAbmModule(globalConfigurationFactory);

        ObjectFactory.createObject(IGeneticAlgorithmListener.class).notifyNewExperimentExecuting(this);

        IOptimizer optimizer = ObjectFactory.createObject(IOptimizer.class);
        optimizer.run();
    }

    private OptionParser buildOptionParser() {
        OptionParser parser = new OptionParser();
        parser.accepts(ARGUMENT_TROPE, "Specific trope to promote").withRequiredArg().ofType(String.class);
        parser.accepts(ARGUMENT_TROPE_LIST, "Retrieves the current list of tropes to promote");
        parser.accepts(ARGUMENT_HELP, "Displays this help").forHelp();
        parser.allowsUnrecognizedOptions();
        return parser;
    }

    private void installMockForExcelWriter() {
        ExcelWriterGeneticAlgorithmListener listener = new ExcelWriterGeneticAlgorithmListener();
        ObjectFactory.installMock(IGeneticAlgorithmListener.class, listener);
    }

    private void configureEcModule(IGlobalConfigurationFactory globalConfigurationFactory) {
        CommonEcConfiguration ecConfig = globalConfigurationFactory.getCommonEcConfiguration();
        ecConfig.MAXIMUM_ITERATIONS = 1000;
        ecConfig.POPULATION_SIZE = 30;
        ecConfig.NUMBER_OF_TRIALS = 15;
        ecConfig.TROPE_TO_PROMOTE = fitnessTrope;
        ecConfig.BLX_ALPHA = 0.5f;
        ecConfig.ETA_DISTANCE_MUTATION_DISTRIBUTION = 20;
        ecConfig.TROPES_TO_FOLLOW_UP = Trope.getTropesInFromMonomyth();
        ecConfig.MAXIMUM_SECONDS_TO_GET_ALL_OCCURRENCES = 600;
    }

    private void configureAbmModule(IGlobalConfigurationFactory globalConfigurationFactory) {
        CommonAbmConfiguration abmConfig = globalConfigurationFactory.getCommonAbmConfiguration();
        abmConfig.MAX_NUMBER_OF_CIRCLES = 16;
        abmConfig.MAX_NUMBER_OF_TRIANGLES = 16;
        abmConfig.MAX_NUMBER_OF_SQUARES = 16;
        abmConfig.MIN_NUMBER_OF_DAYS = 2;
        abmConfig.MAX_NUMBER_OF_DAYS = 128;
        abmConfig.MAX_WORLD_SIZE = 16;
        abmConfig.MIN_WORLD_SIZE = 8;
    }

    private void printHelp(OptionParser parser) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            parser.printHelpOn(byteArrayOutputStream);
            System.out.println(byteArrayOutputStream.toString());
        } catch (IOException ex) {
            Logger.getLogger(ExperimentEvostar2016_1.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void printTropeList() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Trope list:\n");

        Trope[] tropes = Trope.getTropesInFromMonomyth();
        List<String> tropesAsList = Arrays.stream(tropes).map(Trope::toString).collect(Collectors.toList());
        tropesAsList.add(0, OPTION_ALL_TROPES);

        tropesAsList.stream().forEach((trope) -> {
            stringBuilder.append("\t" + trope + "\n");
        });
        System.out.println(stringBuilder.toString());
    }
}
