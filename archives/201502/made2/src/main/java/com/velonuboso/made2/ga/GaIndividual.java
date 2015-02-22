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
import com.velonuboso.made2.core.rat.RatEnvironment;
import com.velonuboso.made2.core.rat.RatEvaluator;
import com.velonuboso.made2.core.setup.BaseAgentSetup;
import com.velonuboso.made2.core.setup.GASetup;
import com.velonuboso.made2.core.setup.GlobalSetup;
import java.util.Arrays;
import java.util.Random;

/**
 *
 * @author Ruben
 */
public class GaIndividual {

    private float[] chromosomes;
    private GaFitness fitness = null;
    private boolean crossover = false;
    
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

    public void evaluate(Random random, RatEvaluator evaluator,
            GASetup gaSetup,
            BaseAgentSetup baseAgentSetup, GlobalSetup globalSetup,
            ExecutionListenerInterface logger) {
        
        GaFitness f = new GaFitness();
        
        for (int i=0; i<gaSetup.getTxtExecutionsAVG(); i++){
            RatEnvironment env = new RatEnvironment(this, baseAgentSetup, globalSetup, random, logger, evaluator);
            GaFitness f2 = env.runEnvironment(false, false);
            f.add(f2);
        }
        f.divideBy(gaSetup.getTxtExecutionsAVG());
        
        this.fitness = f;
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

    boolean isCrossover() {
        return crossover;
    }

    void setCrossover() {
        crossover = true;
    }
    
}
