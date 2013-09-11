/*
 * Copyright 2013 Rubén Héctor García <raiben@gmail.com>.
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

import java.util.ArrayList;
import org.jgap.Chromosome;
import org.jgap.Configuration;
import org.jgap.FitnessFunction;
import org.jgap.Gene;
import org.jgap.Genotype;
import org.jgap.IChromosome;
import org.jgap.InvalidConfigurationException;
import org.jgap.impl.DefaultConfiguration;
import org.jgap.impl.DoubleGene;

/**
 * Main class that can run the full experiment.
 *
 * @author Rubén Héctor García <raiben@gmail.com>
 */
public final class App {

    /**
     * maximum number of executions when auto-testing.
     */
    private static final int MAX_EXECUTIONS = 300;
    private static final int MAX_CHROMOSOMES = 30;

    /**
     * mno default constructor.
     */
    private App() {
        // Prevent instantiation
        // Optional: throw an exception e.g. AssertionError
        // if this ever *is* called
    }

    /**
     * main method.
     *
     * @param args main arguments
     */
    public static void main(final String[] args) {
        try {
            App.doMain(args);
        } catch (Exception e) {
            boolean debug = false;
            int i = 0;
            while (!debug && i < args.length) {
                if (args[i].toLowerCase().compareTo("--debug") == 0 ||
                        args[i].toLowerCase().compareTo("-d") == 0) {
                    debug = true;
                }
                i++;
            }
            if (debug) {
                e.printStackTrace();
            } else {
                System.err.println("Error: " + e.getMessage());
                System.err.println("For a full stacktrace please use --debug");
            }
        }
    }

    /**
     * real main method.
     *
     * @param args shell arguments
     * @throws Exception
     */
    public static void doMain(final String[] args) throws Exception {

        Parameters.createInstance(args);

        if (Parameters.getInstance().isHelp()) {
            return;
        }

        // runs the autotest
        if (Parameters.getInstance().isRunAutotest()) {
            runAutoTest();
        } else if (Parameters.getInstance().isRunChromosome()) {
            runChromosome();
        } else if (Parameters.getInstance().isRunExperiment()) {
            runExperiment();
        }
    }

    /**
     * runs the auto-test.
     *
     * @throws InvalidConfigurationException
     */
    private static void runAutoTest() throws InvalidConfigurationException {

        System.out.println(Helper.consoleMsg("Running the autotest"));

        long t0 = System.currentTimeMillis();

        //RatEvaluator e = RatEvaluator.getInstance();

        int maxChromosomes = MAX_CHROMOSOMES;

        ArrayList<Double> paramChromosome = null;
        if (Parameters.getInstance().getChromosome() != null) {
            maxChromosomes = 1;
            paramChromosome = new ArrayList<Double>();
            String[] values = Parameters.getInstance().getChromosome().split(";");
            for (int i = 0; i < values.length; i++) {
                paramChromosome.add(Double.parseDouble(values[i].replace(",", ".")));
            }
            System.out.println(Helper.consoleMsg("Using given chromosome"));
        }

        Configuration conf = new DefaultConfiguration();
        FitnessFunction myFunc = new RatFitnessFunction();

        conf.setFitnessFunction(myFunc);



        RatFitnessFunction rff = new RatFitnessFunction();
        double minExecutionsToLowerError[] = new double[maxChromosomes];

        for (int i = 0; i < maxChromosomes; i++) {

            Gene[] sampleGenes =
                    new Gene[RatFitnessFunction.getGeneNumber()];
            for (int k = 0; k < RatFitnessFunction.getGeneNumber(); k++) {
                sampleGenes[k] = new DoubleGene(conf, 0, 1);
                if (paramChromosome != null) {
                    sampleGenes[k].setAllele(paramChromosome.get(k));
                } else {
                    sampleGenes[k].setAllele(Parameters.getInstance().getRandom().nextDouble());
                }
            }
            IChromosome chromosome = new Chromosome(conf, sampleGenes);

            double[] av = rff.evaluateExecutions(chromosome, MAX_EXECUTIONS);
            double[] relErrors = new double[MAX_EXECUTIONS];
            double m = Helper.getMean(av, MAX_EXECUTIONS);
            for (int j=1; j<MAX_EXECUTIONS; j++){
               double mean = Helper.getMean(av, j+1);
               double var = Helper.getVariance(av, j);
               double error = Math.abs(m-mean);
               double relError = error/mean;
               relErrors[j] = relError;
               System.out.println(Helper.executionsToString(j+1, mean, var, error, relError));
            }
            int j = MAX_EXECUTIONS-2;
            while (j>0 && relErrors[j]<0.05){
                minExecutionsToLowerError[i] = j;
                j--;
            }
            
            System.out.println(Helper.consoleMsg("At least "+minExecutionsToLowerError[i] + " executions"));

            chromosome.setFitnessValue(av[0]);
            
            System.out.println(Helper.chromosomeAndGenerationToString(i, chromosome));
        }

        for (int i = 0; i < maxChromosomes; i++) {
            System.out.println(Helper.consoleMsg("Summary"));
            System.out.println("Tested on "+maxChromosomes+" chromosomes");
            System.out.println(MAX_EXECUTIONS+ " executions per chromosome");
            System.out.println("Best case to lower 0.5 "
                    + "relative error: "
                    + Helper.getMin(minExecutionsToLowerError,
                    minExecutionsToLowerError.length));
            System.out.println("Worst case to lower 0.5 "
                    + "relative error: "
                    + Helper.getMax(minExecutionsToLowerError,
                    minExecutionsToLowerError.length));
            System.out.println("Average case to lower 0.5 "
                    + "relative error: "
                    + Helper.getMean(minExecutionsToLowerError,
                    minExecutionsToLowerError.length));
        }

        System.out.println(Helper.executionTime(t0,
                System.currentTimeMillis()));
    }

    private static void runChromosome() throws InvalidConfigurationException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private static void runExperiment() throws InvalidConfigurationException {
        long t0 = System.currentTimeMillis();

        RatEvaluator e = RatEvaluator.getInstance();

        Configuration conf = new DefaultConfiguration();
        FitnessFunction myFunc = new RatFitnessFunction();

        conf.setFitnessFunction(myFunc);

        Gene[] sampleGenes =
                new Gene[RatFitnessFunction.getGeneNumber()];
        for (int i = 0; i < RatFitnessFunction.getGeneNumber(); i++) {
            sampleGenes[i] = new DoubleGene(conf, 0, 1);
        }

        Chromosome sampleChromosome = new Chromosome(conf, sampleGenes);
        conf.setSampleChromosome(sampleChromosome);

        conf.setPopulationSize(Parameters.getInstance().getPopulation());
        Genotype population = Genotype.randomInitialGenotype(conf);

        // show a sample of a random solution
        RatEnvironment environment1 = new RatEnvironment(
                population.getPopulation().getChromosome(0));
        environment1.runEnvironment(false, false);

        IChromosome firstSolution = population.getPopulation().getChromosome(0);

        System.out.println(Helper.chromosomeAndGenerationToString(-1, firstSolution));

        // start iterating
        IChromosome bestSolutionSoFar = population.getFittestChromosome();
        System.out.println(Helper.chromosomeAndGenerationToString(0, bestSolutionSoFar));

        double fitness = bestSolutionSoFar.getFitnessValue();

        for (int i = 0; i < Parameters.getInstance().getNumberOfGenerations(); i++) {
            population.evolve();
            bestSolutionSoFar = population.getFittestChromosome();

            System.out.println(Helper.chromosomeAndGenerationToString(0, bestSolutionSoFar));

            if (bestSolutionSoFar.getFitnessValue() > fitness) {
                fitness = bestSolutionSoFar.getFitnessValue();
            }

        }


        long t1 = System.currentTimeMillis();
        System.out.println(Helper.executionTime(t0, t1));

        /*
         // show a sample
         RatEnvironment environment2 = new RatEnvironment(bestSolutionSoFar);
         environment2.runEnvironment(true, true);

         System.out.println("SUMMARY 1:");
         System.out.println(environment1.getSummary());
         System.out.println("SUMMARY 2:");
         System.out.println(environment2.getSummary());
         */
    }

    /*
     * String result = RatEvaluator.getInstance().getStringProperty("sample.chromosome");
     result = result.replace(",", ".");
     String[] values = result.split(";");

     long t0 = System.currentTimeMillis();

     RatEvaluator e = RatEvaluator.getInstance();

     Configuration conf = new DefaultConfiguration();
     FitnessFunction myFunc = new RatFitnessFunctionGrqph();

     conf.setFitnessFunction(myFunc);

     Gene[] sampleGenes =
     new Gene[values.length-2];
     for (int i = 0; i < values.length-2 ; i++) {
     sampleGenes[i] = new DoubleGene(conf, 0, 1);
     sampleGenes[i].setAllele(Double.parseDouble(values[i+2]));
     }

     Chromosome givenChromosome = new Chromosome(conf, sampleGenes);

     Double d = givenChromosome.getFitnessValue();

     System.out.println(
     df2.format(-1)+";"
     +df3.format(givenChromosome.getFitnessValue())
     +";"+chromosomeToString(givenChromosome)
     );
     */
}
