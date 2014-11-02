/*
 * Copyright 2014 Rubén Héctor García <raiben@gmail.com>.
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

package com.velonuboso.made2.ga;

import com.velonuboso.made2.core.interfaces.ExecutionListenerInterface;
import com.velonuboso.made2.core.rat.RatEvaluator;
import com.velonuboso.made2.core.setup.BaseAgentSetup;
import com.velonuboso.made2.core.setup.GASetup;
import com.velonuboso.made2.core.setup.GlobalSetup;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Ruben
 */
public class GaPopulation {

    private GaIndividual bestIndividualEver = null;
    private ArrayList<GaIndividual> bestIndividualsPerGeneration = new ArrayList<GaIndividual>();
    private ArrayList<GaIndividual> bestIndividualsEver = new ArrayList<GaIndividual>();
    private ArrayList<Double> averageFitnessPerGeneration = new ArrayList<Double>();


    private ArrayList<GaIndividual> individuals;
    private Random random;
    private GaIndividual bestIndividual = null;

    private float crossoverProbability = 0;
    private float mutationProbability = 0.05f;
    private int popSize = 0;
    private int chromSize = 0;
    private RatEvaluator evaluator = null;
    
    private GlobalSetup globalSetup = null;
    private GASetup gaSetup = null;
    private BaseAgentSetup baseAgentSetup = null;   
    
    
    public GaPopulation(Random random, int popSize, int chromSize, 
            float crossoverProbability, float mutationProbability,
            RatEvaluator evaluator,
            GlobalSetup globalSetup,
            GASetup gaSetup,
            BaseAgentSetup baseAgentSetup) {
        
        this.random = random;
        this.crossoverProbability = crossoverProbability;
        this.mutationProbability = mutationProbability;
        this.popSize = popSize;
        this.chromSize = chromSize;
        this.evaluator = evaluator;
        this.gaSetup = gaSetup;
        this.globalSetup = globalSetup;
        this.baseAgentSetup = baseAgentSetup;
        
        bestIndividual = null;
        individuals = new ArrayList<GaIndividual>();

        for (int i = 0; i < popSize; i++) {
            GaIndividual chrom = GaIndividual.newRandomChromosome(random, chromSize);
            individuals.add(chrom);
        }
    }

    private void evaluate(ExecutionListenerInterface logger) {
        bestIndividual = null;
        double avg = 0;
        
        for (int i = 0; i < individuals.size(); i++) {
            GaIndividual c = individuals.get(i);
            c.evaluate(random, evaluator, gaSetup, baseAgentSetup, globalSetup, logger);

            avg += c.getFitness().getTotal();
            if (bestIndividual == null) {
                bestIndividual = c;
            } else {
                if (bestIndividual.getFitness().getTotal() < c.getFitness().getTotal()) {
                    bestIndividual = c;
                }
            }
        }
        bestIndividualsPerGeneration.add(bestIndividual);

        avg /=individuals.size();
        averageFitnessPerGeneration.add(avg);
        
        if (bestIndividualEver == null) {
            bestIndividualEver = bestIndividual;
        } else if (bestIndividualEver.getFitness().getTotal() < bestIndividual.getFitness().getTotal()) {
            bestIndividualEver = bestIndividual;
        }
        bestIndividualsEver.add(bestIndividualEver);
    }

    public GaIndividual getBestIndividual() {
        return bestIndividual;
    }

    private void evolve() {
        ArrayList<GaIndividual> intermediatePopulation
                = GaHelper.selection(individuals, random);

        GaHelper.crossover(intermediatePopulation, random, crossoverProbability);

        for (int i = 0; i < intermediatePopulation.size(); i++) {
            if (!intermediatePopulation.get(i).isCrossover()){
                GaHelper.mutate(intermediatePopulation.get(i), random, mutationProbability);
            }
        }

        individuals = intermediatePopulation;
    }

    public void executeGA(EvolutionListener listener, ExecutionListenerInterface logger, float change, int lastGenerations) {
        boolean stop = false;
        int iteration = 0;

        while (!stop) {
            this.evaluate(logger);

            listener.logIteration(
                    this.bestIndividualsPerGeneration.get(iteration),
                    this.averageFitnessPerGeneration.get(iteration),
                    iteration);

            if (iteration >= lastGenerations) {
                double oldF = this.bestIndividualsEver.get(iteration - lastGenerations).getFitness().getTotal();
                double newF = this.bestIndividualEver.getFitness().getTotal();
                if (oldF >= newF) {
                    stop = true;
                }
            }
            if (!stop) {
                this.evolve();
            }

            iteration++;
        }
    }
}
