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
package com.velonuboso.basicmade;

import org.jgap.Chromosome;
import org.jgap.Configuration;
import org.jgap.FitnessFunction;
import org.jgap.Gene;
import org.jgap.Genotype;
import org.jgap.IChromosome;
import org.jgap.impl.DefaultConfiguration;
import org.jgap.impl.DoubleGene;

/**
 * Main class that can run the full experiment.
 *
 * @author Rubén Héctor García <raiben@gmail.com>
 */
public class App {

    /**
     * max allowed evaluation (generations) of the genetic algorithm.
     */
    public static final int MAX_ALLOWED_EVOLUTIONS = 20; //5000;
    /**
     * population size for the genetic algorithm.
     */
    private static final int POPULATION_SIZE = 100; //500;

    /**
     * main method.
     *
     * @param args shell arguments
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {

        Configuration conf = new DefaultConfiguration();
        FitnessFunction myFunc = new MadeFitnessFunction();

        conf.setFitnessFunction(myFunc);

        Gene[] sampleGenes =
                new Gene[MadeFitnessFunction.getGeneNumber()];
        for (int i = 0; i < MadeFitnessFunction.getGeneNumber(); i++) {
            sampleGenes[i] = new DoubleGene(conf, 0, 1);
        }

        Chromosome sampleChromosome = new Chromosome(conf, sampleGenes);
        conf.setSampleChromosome(sampleChromosome);

        conf.setPopulationSize(POPULATION_SIZE);
        Genotype population = Genotype.randomInitialGenotype(conf);

        // show a sample of a random solution
        MadeEnvironment environment = new MadeEnvironment(
                population.getPopulation().getChromosome(0));
        environment.runEnvironment(true);

        // start iterating
        IChromosome bestSolutionSoFar = population.getFittestChromosome();

        System.out.println("Best solution: " + bestSolutionSoFar);
        System.out.println("fitness: " + bestSolutionSoFar.getFitnessValue());

        double fitness = bestSolutionSoFar.getFitnessValue();

        for (int i = 0; i < MAX_ALLOWED_EVOLUTIONS; i++) {
            System.out.println("Iteration: " + i);
            population.evolve();
            bestSolutionSoFar = population.getFittestChromosome();
            if (bestSolutionSoFar.getFitnessValue() > fitness) {
                System.out.println("Best solution: " + bestSolutionSoFar);
                System.out.println("fitness: "
                        + bestSolutionSoFar.getFitnessValue());
                fitness = bestSolutionSoFar.getFitnessValue();
            }
        }

        // show a sample
        environment = new MadeEnvironment(bestSolutionSoFar);
        environment.runEnvironment(true);
    }
}
