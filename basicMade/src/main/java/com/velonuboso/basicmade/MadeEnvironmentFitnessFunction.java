/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.velonuboso.basicmade;

import org.jgap.FitnessFunction;
import org.jgap.IChromosome;

/**
 *
 * @author Ruben
 */
public class MadeEnvironmentFitnessFunction extends FitnessFunction{

    static int getGeneNumber() {
        return MadeEnvironment.NUMBER_OF_PROFILES*MadeAgent.NUMBER_OF_FEATURES;
    }

    public static int AVERAGE = 10;
    
    @Override
    protected double evaluate(IChromosome ic) {
        double ret = 0;
        for (int i=0; i<AVERAGE; i++){
            MadeEnvironment env = new MadeEnvironment(ic);
            ret += env.runEnvironment(false);
        }
        return ret / (double)AVERAGE;
    }
    
}
