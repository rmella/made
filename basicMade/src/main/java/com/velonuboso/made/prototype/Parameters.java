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
    @Option(name = "--debug", aliases = {"-d"}, usage = "a full backtrace will be shown on error")
    private boolean debug = false;

    @Option(name = "--runAutotest", aliases = {"-ra"}, usage = "the environment is executed "
            + "in autotest mode and calculates the average number of executions "
            + "in a randomly generated environment "
            + "(or given  with the parameter -chromosome) whose tipical "
            + "deviation is under 0.5")
    private boolean runAutotest = false;
    
    @Option(name = "--runChromosome", aliases = {"-rc"}, usage = "the environment executes "
            + "an environment.")
    private boolean runChromosome = false;
    
    @Option(name = "--runExperiment", aliases = {"-re"}, usage = "the environment executes "
            + "an autotest and calculates the average number of executions in a "
            + "randomly generated environment whose tipical "
            + "deviation is under 0.5")
    private boolean runExperiment = false;
    
    @Option(name = "--chromosome", aliases = {"-c"}, usage = "if -autoTest or -runChromosome, "
            + "use this chromosome. "
            + "If runExperiment, place it in the initial population",
            metaVar = "[COMMA_SEPARATED_VALUES]")
    private String chromosome = null;
    
    @Option(name = "--showSheets", aliases = {"-ss"}, usage = "if -runChromosome, all the agents' "
            + "lifes are shown and also a summary")
    private boolean showSheets;
    
    @Option(name = "--saveRatMap", aliases = {"-sm"}, usage = "stores the rat map in a file (graphviz needed)")
    private File saveRatMap = new File(".");
    
    @Option(name = "--generations", aliases = {"-eg"}, usage = "number of generations of the experiment")
    private int numberOfGenerations;
    
    @Option(name = "--population", aliases = {"-ep"}, usage = "if -runExperiment, population of the experiment")
    private int population;
    
    @Option(name = "--numberOfProfiles", aliases = {"-np"}, usage = "number of profiles, default = 1")
    private int numberOfProfiles = 1;
    
    @Option(name = "--numberOfInitialAgents", aliases = {"-ni"}, usage = "number of initial agents, default = 20")
    private int numberOfInitialAgents = 20;
    
    @Option(name = "--numberOfCells", aliases = {"-nc"}, usage = "dimension of the square map")
    private int numberOfCells = 20;
    
    @Option(name = "--numberOfFoodPieces", aliases = {"-nf"}, usage = "number of food pieces each day, default = 20")
    private int numberOfFoodPieces = 20;
    
    @Option(name = "--numberOfDays", aliases = {"-nd"}, usage = "number of days to execute, default = 1000")
    private int numberOfDays = 1000;
    
    @Option(name = "--numberOfExecutions", aliases = {"-ne"}, usage = "if runExperiment, number of times a world is "
            + "executed to retrieve the average")
    private int numberOfExecutions;
    
    @Option(name = "--patterns",  aliases = {"-p"}, usage = "the pattern file name")
    private File patterns = new File(".");

    @Option(name = "--baseDays",  aliases = {"-bd"}, usage = "the pattern file name, default = 200")
    private int baseDays = 200;

    @Option(name = "--baseEnergy",  aliases = {"-be"}, usage = "the pattern file name, default = 5")
    private int baseEnergy = 5;

    @Option(name = "--baseSmell",  aliases = {"-bs"}, usage = "the pattern file name, default = 3")
    private int baseSmell = 3;

    @Option(name = "--baseNutrition",  aliases = {"-bn"}, usage = "the pattern file name, default = 4")
    private int baseNutrition = 4;

    @Option(name = "--baseByte",  aliases = {"-bb"}, usage = "the pattern file name, default = 5")
    private int baseByte = 5;

    @Option(name = "--baseFur",  aliases = {"-bf"}, usage = "the pattern file name, default = 5")
    private int baseFur = 5;

    @Option(name = "--baseAgeToBeAdultFemale",  aliases = {"-btaf"}, usage = "the pattern file name, default = 42")
    private int baseAgeToBeAdultFemale = 42;

    @Option(name = "--baseToBeAdultMale",  aliases = {"-btam"}, usage = "the pattern file name,default = 49")
    private int baseToBeAdultMale = 49;

    @Option(name = "--basePregnancyTime",  aliases = {"-bp"}, usage = "the pattern file name, default = 30")
    private int basePregnancyTime = 30;

    @Option(name = "--help", aliases = "-h", usage = "print help")
    private boolean help = false;


    private static Parameters instance = null;

    private Parameters(String[] args) throws CmdLineException {

        this.parser = new CmdLineParser(this);
        parser.setUsageWidth(80);

        try {
            parser.parseArgument(args);


            int run = 0;

            if (help){
                System.err.println("java SampleMain [options...] arguments...");
                parser.printUsage(System.err);
                System.err.println();
                System.err.println("Example: java SampleMain" + parser.printExample(OptionHandlerFilter.ALL));
                return;
            }

            if (runAutotest) run++;
            if (runChromosome) run++;
            if (runExperiment) run++;

            if (run==0){
                throw new CmdLineException (parser,
                        "One execution mode must be chosen: "
                        + "-runAutotest, -runChromosome or -runExperiment");
            }
            if (run > 2){
                throw new CmdLineException (parser,
                        "Only one execution mode must be chosen: "
                        + "-runAutotest, -runChromosome or -runExperiment");
            }

            if (runExperiment){
                if (numberOfGenerations<=0){
                    throw new CmdLineException (parser,
                            "numberOfGenerations must be set");
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


    public boolean isDebug() {
        return debug;
    }

    public boolean isHelp() {
        return help;
    }

    public boolean isRunAutotest() {
        return runAutotest;
    }

    public boolean isRunChromosome() {
        return runChromosome;
    }

    public boolean isRunExperiment() {
        return runExperiment;
    }

    public boolean isShowSheets() {
        return showSheets;
    }

    public void setNumberOfExecutions(int numberOfExecutions) {
        this.numberOfExecutions = numberOfExecutions;
    }

    public int getBaseAgeToBeAdultFemale() {
        return baseAgeToBeAdultFemale;
    }

    public int getBaseByte() {
        return baseByte;
    }

    public int getBaseDays() {
        return baseDays;
    }

    public int getBaseEnergy() {
        return baseEnergy;
    }

    public int getBaseFur() {
        return baseFur;
    }

    public int getBaseNutrition() {
        return baseNutrition;
    }

    public int getBasePregnancyTime() {
        return basePregnancyTime;
    }

    public int getBaseSmell() {
        return baseSmell;
    }

    public int getBaseToBeAdultMale() {
        return baseToBeAdultMale;
    }

    public int getNumberOfExecutions() {
        return numberOfExecutions;
    }

    
    
}
