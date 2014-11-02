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

import java.util.HashMap;
import java.util.TreeSet;

/**
 *
 * @author Ruben
 */
public class GaFitness {

    TreeSet<String> componentsNames = null;
    HashMap<String, Double> components = null;
    double total = 0;
    
    public GaFitness() {
        components = new HashMap<String, Double>();
        componentsNames = new TreeSet<String>();
        total = 0;
    }
    
    
    public void addPartialFitness (String name, double value){
        double currentVal = 0;
        if (components.containsKey(name)){
            currentVal = components.get(name);
        }else{
            componentsNames.add(name);
        }
        components.put(name, currentVal+value);
        total += value;
    }
    
    double getTotal() {
        return total;
    }
    
    public String getPartialFitnessName(int index){
        return (String) components.keySet().toArray()[index];
    }
    
    public double getPartialFitnessValue(int index){
        return components.get((String)components.keySet().toArray()[index]);
    }
    public double getPartialFitnessValue(String name){
        return components.get(name);
    }

    void add(GaFitness f2) {
        Object keyset[] = f2.components.keySet().toArray();
        for (int i=0; i<keyset.length; i++){
            this.addPartialFitness((String)keyset[i], f2.components.get((String)keyset[i]));
        }
    }

    void divideBy(float divisor) {
        Object keyset[] = components.keySet().toArray();
        for (int i=0; i<keyset.length; i++){
            double val = components.get((String)keyset[i]);
            val = val / divisor;
            components.put((String)keyset[i], val);
        }
        total = total / divisor;
    }
    
    public String[] getComponentsNamesInOrder(){
        String[] ret = new String[componentsNames.size()];
        return componentsNames.toArray(ret);
    }
    
}
