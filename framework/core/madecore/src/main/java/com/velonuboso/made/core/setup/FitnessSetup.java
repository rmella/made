/*
 * Copyright 2013 Rubén Héctor García <raiben@gmail.com>.
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


package com.velonuboso.made.core.setup;

import java.util.ArrayList;

/**
 *
 * @author raiben@gmail.com
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
