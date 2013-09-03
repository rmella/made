/*
 * Copyright 2013 Ruben.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.velonuboso.made.prototype;

import java.io.File;
import java.lang.reflect.Field;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.OptionHandlerFilter;
import org.kohsuke.args4j.spi.OptionHandler;

/**
 *
 * @author Ruben
 */
public class Parameters {

    CmdLineParser parser;
    @Option(name = "--debug", aliases = {"-d"}, usage = "A full backtrace will be shown on error")
    private boolean debug = false;

    @Option(name = "--runAutotest", aliases = {"-ra"}, usage = "The environment is executed "
            + "in autotest mode and calculates the average number of executions "
            + "in a randomly generated environment "
            + "(or given  with the parameter -chromosome) whose tipical "
            + "deviation is under 0.5")
    private boolean runAutotest = false;
    
    @Option(name = "--runChromosome", aliases = {"-rc"}, usage = "The environment executes "
            + "an environment.")
    private boolean runChromosome = false;
    
    @Option(name = "--runExperiment", aliases = {"-re"}, usage = "The environment executes "
            + "an autotest and calculates the average number of executions in a "
            + "randomly generated environment whose tipical "
            + "deviation is under 0.5")
    private boolean runExperiment = false;
    
    @Option(name = "--chromosome", aliases = {"-c"}, usage = "If autotest or runChromosome, "
            + "use this chromosome. "
            + "If runExperiment, place it in the initial population",
            metaVar = "[COMMA_SEPARATED_VALUES]")
    private String chromosome = null;
    
    @Option(name = "--showSheets", aliases = {"-ss"}, usage = "If -runChromosome, all the agents' "
            + "lifes are shown and also a summary")
    private boolean showSheets;
    
    @Option(name = "--saveRatMap", aliases = {"-sm"}, usage = "Stores the rat map in a file (graphviz needed)")
    private File saveRatMap = new File(".");
    
    @Option(name = "--generations", aliases = {"-eg"}, usage = "Number of generations of the experiment")
    private int numberOfGenerations;
    
    @Option(name = "--population", aliases = {"-ep"}, usage = "If -runExperiment, population of the experiment")
    private int population;
    
    @Option(name = "--numberOfProfiles", aliases = {"-np"}, usage = "Number of profiles", required = true)
    private int numberOfProfiles;
    
    @Option(name = "--numberOfInitialAgents", aliases = {"-ni"}, usage = "Number of initial agents", required = true)
    private int numberOfInitialAgents;
    
    @Option(name = "--numberOfCells", aliases = {"-nc"}, usage = "Dimension of the square map", required = true)
    private int numberOfCells;
    
    @Option(name = "--numberOfFoodPieces", aliases = {"-nf"}, usage = "Number of food pieces each day", required = true)
    private int numberOfFoodPieces;
    
    @Option(name = "--numberOfDays", aliases = {"-nd"}, usage = "Number of days to execute", required = true)
    private int numberOfDays;
    
    @Option(name = "--numberOfExecutions", aliases = {"-ne"}, usage = "If runExperiment, number of times a world is "
            + "executed to retrieve the average")
    private int numberOfExecutions;
    
    @Option(name = "--patterns",  aliases = {"-p"}, usage = "the pattern file name")
    private File patterns = new File(".");
    private static Parameters instance = null;

    private Parameters(String[] args) throws CmdLineException {

        this.parser = new CmdLineParser(this);
        parser.setUsageWidth(80);

        try {
            parser.parseArgument(args);


            int run = 0;
            if (runAutotest) run++;
            if (runChromosome) run++;
            if (runExperiment) run++;

            if (run==0){
                throw new CmdLineException (parser,
                        "One execution mode must be chosen: "
                        + "runAutotest, runChromosome or runExperiment");
            }
            if (run > 2){
                throw new CmdLineException (parser,
                        "Only one execution mode must be chosen: "
                        + "runAutotest, runChromosome or runExperiment");
            }

            if (runExperiment){
                if (numberOfGenerations<=0){
                    throw new CmdLineException (parser,
                            "numberOfGeneration must be set");
                }
                if (population<=0){
                    throw new CmdLineException (parser,
                            "population must be set");
                }
                if (numberOfExecutions<=0){
                    throw new CmdLineException (parser,
                            "numberOfExecutions must be set");
                }
            }

        } catch (CmdLineException e) {
            System.err.println(e.getMessage());
            System.err.println("java SampleMain [options...] arguments...");
            parser.printUsage(System.err);
            System.err.println();
            System.err.println("Example: java SampleMain" + parser.printExample(OptionHandlerFilter.ALL));
            throw e;
        }

        if (debug) showSummary();

    }

    public static Parameters createInstance(String[] args) throws CmdLineException {
        if (instance == null) {
            instance = new Parameters(args);
        }
        return instance;
    }

    public static Parameters getInstance() {
        return instance;
    }

    private void showSummary() {
        StringBuilder stb = new StringBuilder();

        stb.append("Current parameters\n");
        Field[] fields = Parameters.class.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
              Option s = fields[i].getAnnotation(Option.class);
              if (s != null) {
                  try {
                      stb.append(" - " + fields[i].getName() + " = "+fields[i].get(this)+"\n");
                  } catch (IllegalArgumentException ex) {
                      Logger.getLogger(Parameters.class.getName()).log(Level.SEVERE, null, ex);
                  } catch (IllegalAccessException ex) {
                      Logger.getLogger(Parameters.class.getName()).log(Level.SEVERE, null, ex);
                  }
              }
        }

        System.out.println(stb);
    }

    public String getChromosome() {
        return chromosome;
    }

    public int getNumberOfCells() {
        return numberOfCells;
    }

    public int getNumberOfDays() {
        return numberOfDays;
    }

    public int getNumberOfFoodPieces() {
        return numberOfFoodPieces;
    }

    public int getNumberOfGenerations() {
        return numberOfGenerations;
    }

    public int getNumberOfInitialAgents() {
        return numberOfInitialAgents;
    }

    public int getNumberOfProfiles() {
        return numberOfProfiles;
    }

    public CmdLineParser getParser() {
        return parser;
    }

    public File getPatterns() {
        return patterns;
    }

    public int getPopulation() {
        return population;
    }

    public File getSaveRatMap() {
        return saveRatMap;
    }

    public int getWorldExecutions() {
        return numberOfExecutions;
    }
    
}
