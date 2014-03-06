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


package com.velonuboso.made.core;

import com.velonuboso.made.core.common.Helper;
import com.velonuboso.made.core.setup.FitnessSetup;
import com.velonuboso.made.core.setup.GASetup;
import com.velonuboso.made.core.setup.BaseAgentSetup;
import com.velonuboso.made.core.setup.GlobalSetup;
import com.velonuboso.made.core.interfaces.ExecutionListenerInterface;
import com.velonuboso.made.core.rat.RatEnvironment;
import com.velonuboso.made.core.rat.RatEvaluator;
import com.velonuboso.made.core.rat.RatFitnessFunction;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
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
 *
 * @author raiben@gmail.com
 */
public class Launcher extends Thread {

    private ExecutionListenerInterface listener;
    private GlobalSetup globalSetup;
    private BaseAgentSetup baseAgentSetup;
    private GASetup gaSetup;
    private FitnessSetup fitnessSetup;
    private Random r = new Random();
    private RatEvaluator evaluator = null;
    
    private volatile boolean stop = false;
    private Genotype population = null;
    
    public Launcher(ExecutionListenerInterface listener, GlobalSetup globalSetup, BaseAgentSetup baseAgentSetup, GASetup gaSetup, FitnessSetup fitnessSetup) {
        this.listener = listener;
        this.globalSetup = globalSetup;
        this.baseAgentSetup = baseAgentSetup;
        this.gaSetup = gaSetup;
        this.fitnessSetup = fitnessSetup;
        this.evaluator = new RatEvaluator(fitnessSetup, globalSetup);
    }

    @Override
    public void run() {
        try {
            launch();
        } catch (InvalidConfigurationException ex) {
            Logger.getLogger(Launcher.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    
    public void launch() throws InvalidConfigurationException {

        long t0 = System.currentTimeMillis();

        Configuration conf = new DefaultConfiguration(Long.toString(System.currentTimeMillis()),"");
        RatFitnessFunction myFunc = new RatFitnessFunction(globalSetup, baseAgentSetup, gaSetup, r, listener, evaluator);

        conf.setFitnessFunction(myFunc);


        Gene[] sampleGenes
                = new Gene[myFunc.getGeneNumber()];
        for (int i = 0; i < myFunc.getGeneNumber(); i++) {
            sampleGenes[i] = new DoubleGene(conf, 0, 1);
        }

        Chromosome sampleChromosome = new Chromosome(conf, sampleGenes);
        conf.setSampleChromosome(sampleChromosome);

        conf.setPopulationSize(gaSetup.getTxtPopulation());
        population = Genotype.randomInitialGenotype(conf);

        // show a sample of a random solution
        RatEnvironment environment1 = new RatEnvironment(
                population.getPopulation().getChromosome(0),
                baseAgentSetup,
                globalSetup,
                r,
                listener,
                evaluator
        );
        environment1.runEnvironment(false, false);

        IChromosome firstSolution = population.getPopulation().getChromosome(0);

        listener.log(Helper.chromosomeAndGenerationToString(-1, firstSolution, firstSolution.getFitnessValue()));

        // start iterating
        IChromosome bestSolutionSoFar = population.getFittestChromosome();
        
        double fitness = 0;
        double avg = 0;
            
        if (!stop){
            // calculate the average in the generation
            for (IChromosome ic : population.getChromosomes()) {
                avg += ic.getFitnessValue();
            }
            avg = avg / population.getChromosomes().length;

            listener.log(Helper.chromosomeAndGenerationToString(0, bestSolutionSoFar, avg));
            listener.generation(0, (float) bestSolutionSoFar.getFitnessValue(),
                    (float) avg, Helper.chromosomeToArray(bestSolutionSoFar));

            fitness = bestSolutionSoFar.getFitnessValue();
        }

        int i = 0;
        while (i < gaSetup.getTxtGenerations() && !stop){

            population.evolve();
            if (!stop){
                bestSolutionSoFar = population.getFittestChromosome();
                // calculate the average in the generation
                avg = 0;
                for (IChromosome ic : population.getChromosomes()) {
                    avg += ic.getFitnessValue();
                }
                avg = avg / population.getChromosomes().length;

                listener.log(Helper.chromosomeAndGenerationToString(i + 1, bestSolutionSoFar, avg));
                listener.generation(i + 1, (float) bestSolutionSoFar.getFitnessValue(),
                        (float) avg, Helper.chromosomeToArray(bestSolutionSoFar));

                if (bestSolutionSoFar.getFitnessValue() > fitness) {
                    fitness = bestSolutionSoFar.getFitnessValue();
                }

                float p = (float) (i+1)/(float)(gaSetup.getTxtGenerations());
                listener.progress(p);
            }
            i++;
        }


        long t1 = System.currentTimeMillis();
        listener.log(Helper.executionTime(t0, t1));

        /*
         // show a sample
         RatEnvironment environment2 = new RatEnvironment(bestSolutionSoFar);
         environment2.runEnvironment(true, true);

         listener.log("SUMMARY 1:");
         listener.log(environment1.getSummary());
         listener.log("SUMMARY 2:");
         listener.log(environment2.getSummary());
         */
        
    }
    
    public void launch(IChromosome iChromosome){
        RatEnvironment env = new RatEnvironment(iChromosome, baseAgentSetup, globalSetup, new Random(), listener, evaluator);
        env.runEnvironment(false, false);
        listener.environmentExecuted(env.getAgents());
    }
    
    
    public void friendlyStop(){
        stop = true;
        this.interrupt();
    }
}

