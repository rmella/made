/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.velonuboso.made2.ga;

import java.util.Arrays;
import java.util.Random;

/**
 *
 * @author Ruben
 */
public class GaIndividual {

    private float[] chromosomes;
    private GaFitness fitness = null;
    
    public GaIndividual(float[] chromosomes){
        this.chromosomes = chromosomes;
    }
    
    static GaIndividual newRandomChromosome(Random random, int size) {
        float chromosomes[] = new float[size];
        for (int i=0; i<size; i++){
            chromosomes[i] = random.nextFloat();
        }
        GaIndividual ret = new GaIndividual(chromosomes);
        return ret;
    }

    

    boolean isEvaluated() {
        return (fitness != null);
    }

    GaFitness evaluate(Random random) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    GaFitness getFitness() {
        return fitness;
    }

    public float[] getChromosomes(){
        return chromosomes;
    }
    
    public float[] getCopyOfChromosomes(){
        return Arrays.copyOf(chromosomes, chromosomes.length);
    }
}
