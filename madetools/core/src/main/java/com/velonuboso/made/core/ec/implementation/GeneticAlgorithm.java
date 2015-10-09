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
package com.velonuboso.made.core.ec.implementation;

import com.velonuboso.made.core.common.util.ObjectFactory;
import com.velonuboso.made.core.ec.api.IGeneticAlgorithm;
import com.velonuboso.made.core.ec.api.IGeneticAlgorithmListener;
import com.velonuboso.made.core.ec.api.IIndividual;
import com.velonuboso.made.core.ec.api.IPopulation;
import com.velonuboso.made.core.ec.api.ITerminationCondition;
import com.velonuboso.made.core.ec.entity.GeneDefinition;
import com.velonuboso.made.core.ec.entity.IndividualDefinition;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

/**
 *
 * @author Rubén Héctor García (raiben@gmail.com)
 */
public class GeneticAlgorithm implements IGeneticAlgorithm {

    private List<IGeneticAlgorithmListener> listeners;
    private IndividualDefinition definition;
    private int populationSize;
    private int maximumIterations;
    private float blxAlpha;
    private float distanceParameterMutationDistribution;

    private static final IndividualDefinition EmptyDefinition = new IndividualDefinition(new GeneDefinition[0]);

    public GeneticAlgorithm() {
        listeners = new ArrayList<>();
        definition = EmptyDefinition;
        populationSize = 0;
        maximumIterations = 0;
        blxAlpha = 0;
        distanceParameterMutationDistribution = 0;
    }

    @Override
    public void addListener(IGeneticAlgorithmListener listener) {
        listeners.add(listener);
    }

    @Override
    public void configure (IndividualDefinition definition, int populationSize, int maximumIterations, 
            float blxAlpha, float distanceParameterMutationDistribution) {
        this.listeners = listeners;
        this.definition = definition;
        this.populationSize = populationSize;
        this.maximumIterations = maximumIterations;
        this.blxAlpha = blxAlpha;
        this.distanceParameterMutationDistribution = distanceParameterMutationDistribution;
    }

    @Override
    public IIndividual run() {
        
        ITerminationCondition condition = ObjectFactory.createObject(ITerminationCondition.class);
        condition.setMaximumIterations(maximumIterations);

        IPopulation population = buildInitialPopulation();
        IIndividual bestIndividual = population.getBestIndividual();
        
        int iteration = 0;
        while (!condition.mustFinish(iteration, bestIndividual)) {
            IPopulation matingPool = population.selectMatingPool();
            IPopulation newGeneration = matingPool.createOffspring(blxAlpha, distanceParameterMutationDistribution);
            
            IIndividual bestIndividualInGeneration = newGeneration.getBestIndividual();
            if (bestIndividualInGeneration.getCurrentFitness().compareTo(bestIndividual.getCurrentFitness())>0){
                bestIndividual = bestIndividualInGeneration;
            }
            
            notifyAllListeners(iteration, bestIndividual, newGeneration);
            
            population = newGeneration;
            iteration++;
        }
        
        return bestIndividual;
    }

    private void notifyAllListeners(int iteration, IIndividual bestIndividual, IPopulation newGeneration) {
        listeners.stream().forEach((listener) -> {
            listener.notifyIterationSummary(iteration, bestIndividual,
                    bestIndividual.getCurrentFitness(),
                    newGeneration.getAverageFitness());
        });
    }

    private IPopulation buildInitialPopulation() {
        IPopulation population = ObjectFactory.createObject(IPopulation.class);
        IntStream.range(0, populationSize).forEach(index -> addNewRandomIndividualToPopulation(population));
        return population;
    }

    private void addNewRandomIndividualToPopulation(IPopulation population) {
        IIndividual individual = ObjectFactory.createObject(IIndividual.class);
        individual.setRandomGenes(definition);
        population.add(individual);
    }
}
