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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

/**
 *
 * @author rhgarcia
 */
public class GaHelper {

    

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
        
        // elitism
        int el = (int) (source.size()* 0.1f); // 10%
        Collections.sort(source, new Comparator<GaIndividual>(){

            @Override
            public int compare(GaIndividual o1, GaIndividual o2) {
                if(o1.getFitness().getTotal()==o2.getFitness().getTotal()){
                    return 0;
                }else if (o1.getFitness().getTotal()>o2.getFitness().getTotal()){
                    return 1;
                }else {
                    return -1;
                }
            }
        });
        
        for (int i = 0; i < el; i++){
            target.add(new GaIndividual(source.get(i).getCopyOfChromosomes()));
        }
        
        for (int i = el; i < source.size(); i++) {
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

    static void crossover(ArrayList<GaIndividual> source, Random random, float probability) {
        for (int i=0; i<source.size(); i+=2){
            if (random.nextFloat()<probability){
                source.get(i).setCrossover();
                source.get(i+1).setCrossover();
                
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

    static void mutate(GaIndividual individual, Random random, float probability) {
        float[] g1 = individual.getChromosomes();
        for (int i=0; i<g1.length; i++){
            if (random.nextFloat()<probability){
                g1[i] = random.nextFloat();
            }
        }
    }

}
