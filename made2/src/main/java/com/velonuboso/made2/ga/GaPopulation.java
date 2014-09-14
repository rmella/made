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
    
    private ArrayList<GaIndividual> individuals;
    private Random random;
    private GaIndividual bestIndividual = null;

    public GaPopulation(Random random) {
        this.random = random;
        bestIndividual = null;
        individuals = new ArrayList<GaIndividual>();
    }

    public GaPopulation(Random random, int size) {
        this(random);
        for (int i = 0; i < size; i++) {
            GaIndividual chrom = GaIndividual.newRandomChromosome(random);
            individuals.add(chrom);
        }
    }

    public void evaluate() {
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
        
        if (bestIndividualEver == null){
            bestIndividualEver = bestIndividual;
        }else if (bestIndividualEver.getFitness().getTotal() < bestIndividual.getFitness().getTotal()){
            bestIndividualEver = bestIndividual;
        }
    }
    
    public GaIndividual getBestIndividual(){
        return bestIndividual;
    }
    
    
    public void evolve() {
        
    }
    
}
