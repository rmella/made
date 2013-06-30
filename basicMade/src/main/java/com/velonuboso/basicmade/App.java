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
     * main method.
     *
     * @param args shell arguments
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {

        MadeEvaluator e = MadeEvaluator.getInstance();

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

        conf.setPopulationSize(e.getProperty(e.POPULATION_SIZE));
        Genotype population = Genotype.randomInitialGenotype(conf);

        // show a sample of a random solution
        MadeEnvironment environment1 = new MadeEnvironment(
                population.getPopulation().getChromosome(0));
        environment1.runEnvironment(false);

        // start iterating
        IChromosome bestSolutionSoFar = population.getFittestChromosome();

        System.out.println("Best solution: " + bestSolutionSoFar);
        System.out.println("fitness: " + bestSolutionSoFar.getFitnessValue());

        double fitness = bestSolutionSoFar.getFitnessValue();

        for (int i = 0; i < e.getProperty(e.MAX_ALLOWED_EVOLUTIONS); i++) {
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
        MadeEnvironment environment2 = new MadeEnvironment(bestSolutionSoFar);
        environment2.runEnvironment(false);

        System.out.println("SUMMARY 1:");
        System.out.println(environment1.getSummary());
        System.out.println("SUMMARY 2:");
        System.out.println(environment2.getSummary());

    }
}
