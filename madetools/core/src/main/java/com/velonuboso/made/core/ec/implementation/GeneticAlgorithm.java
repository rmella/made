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

import com.velonuboso.made.core.common.api.IGlobalConfigurationFactory;
import com.velonuboso.made.core.common.entity.CommonEcConfiguration;
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
    CommonEcConfiguration config;
    private static final IndividualDefinition EmptyDefinition = new IndividualDefinition(new GeneDefinition[0]);

    public GeneticAlgorithm() {
        IGlobalConfigurationFactory globalConfigurationFactory = 
            ObjectFactory.createObject(IGlobalConfigurationFactory.class);
        config = globalConfigurationFactory.getCommonEcConfiguration();
        
        listeners = new ArrayList<>();
        definition = EmptyDefinition;
    }

    @Override
    public void addListener(IGeneticAlgorithmListener listener) {
        listeners.add(listener);
    }

    @Override
    public void configure (IndividualDefinition definition) {
        this.listeners = listeners;
        this.definition = definition;
    }

    @Override
    public IIndividual run() {
        
        
        ITerminationCondition condition = ObjectFactory.createObject(ITerminationCondition.class);

        IPopulation population = buildInitialPopulation();
        IIndividual bestIndividualEver = population.getBestIndividual();
        int iteration = 0;
        notifyAllListeners(iteration, bestIndividualEver, population.getAverageFitness(), 
                population.getStandardDeviation());
            
        
        while (!condition.mustFinish(iteration, bestIndividualEver)) {
            IPopulation matingPool = population.selectMatingPool();
            IPopulation newGeneration = matingPool.createOffspring(
                    config.BLX_ALPHA, config.ETA_DISTANCE_MUTATION_DISTRIBUTION);
            
            IIndividual bestIndividualInGeneration = newGeneration.getBestIndividual();
            if (bestIndividualInGeneration.getCurrentFitness().compareTo(bestIndividualEver.getCurrentFitness())>0){
                bestIndividualEver = bestIndividualInGeneration;
            }
            float populationAverage = newGeneration.getAverageFitness();
            float populationStandardDeviation = newGeneration.getStandardDeviation();
            
            notifyAllListeners(iteration, bestIndividualEver, populationAverage, populationStandardDeviation);
            
            population = newGeneration;
            iteration++;
        }
        
        return bestIndividualEver;
    }

    private void notifyAllListeners(int iteration, IIndividual bestIndividualEver, float populationAverage, float populationStandardDeviation) {
        listeners.stream().forEach((listener) -> {
            listener.notifyIterationSummary(iteration, bestIndividualEver,
                    populationAverage, populationStandardDeviation);
        });
    }

    private IPopulation buildInitialPopulation() {
        IPopulation population = ObjectFactory.createObject(IPopulation.class);
        IntStream.range(0, config.POPULATION_SIZE).forEach(index -> addNewRandomIndividualToPopulation(population));
        return population;
    }

    private void addNewRandomIndividualToPopulation(IPopulation population) {
        IIndividual individual = ObjectFactory.createObject(IIndividual.class);
        individual.setRandomGenes(definition);
        population.add(individual);
    }
}
