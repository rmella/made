/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.velonuboso.made2.ga;

import java.util.ArrayList;

/**
 *
 * @author Ruben
 */
public class GaFitness {

    ArrayList<GaPartialFitness> components = new ArrayList<GaPartialFitness>();
    double total = 0;
    
    public GaFitness() {
        components = new ArrayList<GaPartialFitness>();
        total = 0;
    }
    
    
    public void addPartialFitness (String name, double value){
        GaPartialFitness pf = new GaPartialFitness(name, value);
        components.add(pf);
        total += value;
    }
    
    double getTotal() {
        return total;
    }
    
    public String getPartialFitnessName(int index){
        return components.get(index).getName();
    }
    
    public double getPartialFitnessValue(int index){
        return components.get(index).getValue();
    }
    
}
