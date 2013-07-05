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

import java.text.DecimalFormat;
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

        conf.setPopulationSize(e.getProperty(e.POPULATION_SIZE));
        Genotype population = Genotype.randomInitialGenotype(conf);

        // show a sample of a random solution
        RatEnvironment environment1 = new RatEnvironment(
                population.getPopulation().getChromosome(0));
        environment1.runEnvironment(false);

        IChromosome firstSolution = population.getPopulation().getChromosome(0);
        System.out.println(
                df2.format(-1)+";"
                +df3.format(firstSolution.getFitnessValue())
                +";"+printIChromosome(firstSolution)
                );


        // start iterating
        IChromosome bestSolutionSoFar = population.getFittestChromosome();
        System.out.println(
                df2.format(0)+";"
                +df3.format(bestSolutionSoFar.getFitnessValue())
                +";"+printIChromosome(bestSolutionSoFar)
                );
        
        double fitness = bestSolutionSoFar.getFitnessValue();

        for (int i = 0; i < e.getProperty(e.MAX_ALLOWED_EVOLUTIONS); i++) {
            population.evolve();
            bestSolutionSoFar = population.getFittestChromosome();

            System.out.println(
                df2.format(i+1)
                +";"+df3.format(bestSolutionSoFar.getFitnessValue())
                +";"+printIChromosome(bestSolutionSoFar)
                );
            
            if (bestSolutionSoFar.getFitnessValue() > fitness) {
                fitness = bestSolutionSoFar.getFitnessValue();
            }

        }

        long t1 = System.currentTimeMillis();

        System.out.println("Tiempo de ejecución: "+(t1-t0)+"ms");

        // show a sample
        RatEnvironment environment2 = new RatEnvironment(bestSolutionSoFar);
        environment2.runEnvironment(true);

        System.out.println("SUMMARY 1:");
        System.out.println(environment1.getSummary());
        System.out.println("SUMMARY 2:");
        System.out.println(environment2.getSummary());

    }

    static DecimalFormat df = new DecimalFormat("#.###");
    static DecimalFormat df2 = new DecimalFormat("###");
    static DecimalFormat df3 = new DecimalFormat("###.#####");


    public static String printIChromosome(IChromosome ic){
        StringBuilder str = new StringBuilder();
        Gene[] genes = ic.getGenes();
        boolean first = true;
        for (Gene gene : genes) {
            str.append(df.format((Double)gene.getAllele()));
            if (!first){
                str.append(";");
            }else{
                first = false;
            }
        }

        return str.toString();
    }
}
