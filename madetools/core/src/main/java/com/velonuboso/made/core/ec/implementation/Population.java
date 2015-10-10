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

import com.velonuboso.made.core.common.api.IProbabilityHelper;
import com.velonuboso.made.core.common.util.ObjectFactory;
import com.velonuboso.made.core.ec.api.IGene;
import com.velonuboso.made.core.ec.api.IIndividual;
import com.velonuboso.made.core.ec.api.IPopulation;
import com.velonuboso.made.core.ec.api.ISelectionOperator;
import com.velonuboso.made.core.ec.entity.GeneDefinition;
import com.velonuboso.made.core.ec.entity.IndividualDefinition;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

/**
 *
 * @author Rubén Héctor García (raiben@gmail.com)
 */
public class Population implements IPopulation {

    ArrayList<IIndividual> individuals;
    IIndividual bestIndividual;
    float averageFitness;
    float standardDeviation;

    public Population() {
        individuals = new ArrayList<>();
        bestIndividual = null;
        averageFitness = 0;
        standardDeviation = 0;
    }

    @Override
    public IIndividual getBestIndividual() {
        individuals.stream().forEach(individual -> individual.reEvaluate());
        
        averageFitness =  (float) individuals.stream()
                .mapToDouble(individual -> individual.getCurrentFitness().getValue().getAverage())
                .average()
                .orElse(0f);
        
        float variance = (float) individuals.stream()
                .mapToDouble(individual -> Math.pow(averageFitness-individual.getCurrentFitness().getValue().getAverage(), 2))
                .sum()/(individuals.size()-1f);
        standardDeviation = (float) Math.sqrt(variance);
        
        bestIndividual =  individuals.stream()
                .max((IIndividual firstIndividual, IIndividual secondIndividual) -> 
                        firstIndividual.getCurrentFitness().compareTo(secondIndividual.getCurrentFitness()))
                .orElse(null);
        
        return bestIndividual;
    }

    @Override
    public float getAverageFitness() {
        return averageFitness;
    }

    @Override
    public float getStandardDeviation() {
        return standardDeviation;
    }
    
    @Override
    public void add(IIndividual individual) {
        individuals.add(individual);
    }

    @Override
    public IPopulation selectMatingPool() {
       ISelectionOperator selectionOperator =  ObjectFactory.createObject(ISelectionOperator.class);
       return selectionOperator.selectMatingPool(this);
    }

    @Override
    public IPopulation createOffspring(float blxAlpha, float distanceParameterMutationDistribution) {
        IProbabilityHelper helper = ObjectFactory.createObject(IProbabilityHelper.class);
        
       IPopulation offspring = ObjectFactory.createObject(IPopulation.class);
       
       IndividualDefinition definition = individuals.get(0).getIndividualDefinition();
       
       for (int individualIndex =0; individualIndex < individuals.size(); individualIndex+=2){
           IGene [] firstParentGenes = individuals.get(individualIndex).getGenes();
           IGene [] secondParentGenes = individuals.get(individualIndex+1).getGenes();
           
           IGene [] firstChildGenes = new IGene[firstParentGenes.length];
           IGene [] secondChildGenes = new IGene[firstParentGenes.length];
           
           int numberOfGenes = firstParentGenes.length;
           for (int geneIndex = 0; geneIndex<numberOfGenes; geneIndex++){
               IGene firstGene = firstParentGenes[geneIndex];
               IGene secondGene = secondParentGenes[geneIndex];
               GeneDefinition geneDefinition = definition.getGeneDefinition()[geneIndex];
               
               firstChildGenes[geneIndex] =  firstGene.crossover(geneDefinition, secondGene, blxAlpha);
               if (helper.getNextProbability(Population.class) < 1f/numberOfGenes){
                   firstChildGenes[geneIndex].mutate(geneDefinition, distanceParameterMutationDistribution);
               }
               
               secondChildGenes[geneIndex] = firstGene.crossover(geneDefinition, secondGene, blxAlpha);
               if (helper.getNextProbability(Population.class) < 1f/numberOfGenes){
                   secondChildGenes[geneIndex].mutate(geneDefinition, distanceParameterMutationDistribution);
               }
           }
           
           
           
           
           IIndividual firstChild = ObjectFactory.createObject(IIndividual.class);
           firstChild.setGenes(definition, firstChildGenes);
           offspring.add(firstChild);
           
           IIndividual secondChild = ObjectFactory.createObject(IIndividual.class);
           secondChild.setGenes(definition, secondChildGenes);
           offspring.add(secondChild);
       }
       
       return offspring;
    }

    @Override
    public List<IIndividual> getIndividuals() {
        return individuals;
    }
}
