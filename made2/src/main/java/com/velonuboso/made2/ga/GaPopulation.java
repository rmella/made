/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.velonuboso.made2.ga;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Ruben
 */
public class GaPopulation {

    private GaIndividual bestIndividualEver = null;
    private ArrayList<GaIndividual> bestIndividualsPerIteration = new ArrayList<GaIndividual>();
    
    private ArrayList<GaIndividual> individuals;
    private Random random;
    private GaIndividual bestIndividual = null;

    public GaPopulation(Random random, int popSize, int chromSize) {
        this.random = random;
        bestIndividual = null;
        individuals = new ArrayList<GaIndividual>();
        
        for (int i = 0; i < popSize; i++) {
            GaIndividual chrom = GaIndividual.newRandomChromosome(random, chromSize);
            individuals.add(chrom);
        }
    }

    private void evaluate() {
        bestIndividual = null;
        for (int i = 0; i < individuals.size(); i++) {
            GaIndividual c = individuals.get(i);
            c.evaluate(random);

            if (bestIndividual == null) {
                bestIndividual = c;
            } else {
                if (bestIndividual.getFitness().getTotal() < c.getFitness().getTotal()) {
                    bestIndividual = c;
                }
            }
        }
        bestIndividualsPerIteration.add(bestIndividual);
        
        if (bestIndividualEver == null){
            bestIndividualEver = bestIndividual;
        }else if (bestIndividualEver.getFitness().getTotal() < bestIndividual.getFitness().getTotal()){
            bestIndividualEver = bestIndividual;
        }
    }
    
    public GaIndividual getBestIndividual(){
        return bestIndividual;
    }
    
    
    public void evolve(EvolutionListener listener) {
        boolean iterate = true;
        int currentIteration = 0;
        
        while (iterate){
            
            ArrayList<GaIndividual> intermediatePopulation = GaHelper.selection(individuals, random);
            
            GaHelper.crossover(intermediatePopulation, random);
            
            for (int i=0; i<intermediatePopulation.size(); i++){
                GaHelper.mutate(intermediatePopulation.get(i), random);
            }
            
            //Helper.log();
            
            currentIteration ++;
        }
    }
    
}
