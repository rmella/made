/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.velonuboso.made.core.setup;

import java.util.ArrayList;

/**
 *
 * @author rhgarcia
 */
public class FitnessSetup {
    private ArrayList<Class> archetypes;
    private ArrayList<Float> params;
    private ArrayList<Float> fitnessFunctions;

    public FitnessSetup() {
        archetypes = new ArrayList<Class>();
        params = new ArrayList<Float>();
        fitnessFunctions = new ArrayList<Float>();
    }
    
    public void add(Class c, Float param, Float fitness){
        archetypes.add(c);
        params.add(param);
        fitnessFunctions.add(fitness);
    }
    
    public Class getClass(int i){
        return archetypes.get(i);
    }
    
    public Float getParam (int i){
        return params.get(i);
    }
    public Float getFitness (int i){
        return fitnessFunctions.get(i);
    }
    
    public int getSize(){
        return archetypes.size();
    }
}
