/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.velonuboso.made2.ga;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author rhgarcia
 */
public class GaHelper {

    public static float CROSSOVER_PROBABILITY = 0.5f;
    public static float MUTATION_PROBABILITY = 1f/12f;

    /**
     * returns a new arraylist with the size of the source and the (copy of the)
     * best individuals, in terms of their fitness (by tournament).
     * 
     * @param source the Arraylist of individuals
     * @param random the seed
     * @return an array with the (copy of the) selected individuals.
     */
    static ArrayList<GaIndividual> selection(ArrayList<GaIndividual> source, Random random) {
        ArrayList<GaIndividual> target = new ArrayList<GaIndividual>();

        for (int i = 0; i < source.size(); i++) {
            GaIndividual ind1 = source.get(random.nextInt(source.size()));
            GaIndividual ind2 = source.get(random.nextInt(source.size()));
            if (ind1.getFitness().getTotal() > ind2.getFitness().getTotal()) {
                target.add(new GaIndividual(ind1.getCopyOfChromosomes())); // create a copy
            } else {
                target.add(new GaIndividual(ind2.getCopyOfChromosomes())); // create a copy
            }
        }

        return target;

    }

    static void crossover(ArrayList<GaIndividual> source, Random random) {
        for (int i=0; i<source.size(); i+=2){
            if (random.nextFloat()<CROSSOVER_PROBABILITY){
                float[] g1 = source.get(i).getChromosomes();
                float[] g2 = source.get(i+1).getChromosomes();
                
                int index1 = random.nextInt(g1.length);
                int index2 = random.nextInt(g1.length);
                
                if (index1 > index2) {
                    int aux = index1;
                    index1 = index2;
                    index2 = aux;
                }
                
                for (int j=index1; j<index2; j++){
                    float aux = g1[j];
                    g1[j] = g2[j];
                    g2[j] = aux;
                }
            }
        }
    }

    static void mutate(GaIndividual individual, Random random) {
        float[] g1 = individual.getChromosomes();
        for (int i=0; i<g1.length; i++){
            if (random.nextFloat()<MUTATION_PROBABILITY){
                g1[i] = random.nextFloat();
            }
        }
    }

}
