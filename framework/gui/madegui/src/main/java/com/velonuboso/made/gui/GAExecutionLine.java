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

package com.velonuboso.made.gui;

import java.util.ArrayList;

/**
 *
 * @author raiben@gmail.com
 */
public class GAExecutionLine {
    private int id;
    private float maxFitness;
    private float avgFitness;
    private ArrayList<Double> chromosome;

    public GAExecutionLine(int id, float maxFitness, float avgFitness, ArrayList<Double> chromosome) {
        this.id = id;
        this.maxFitness = maxFitness;
        this.avgFitness = avgFitness;
        this.chromosome = chromosome;
    }

    public float getAvgFitness() {
        return avgFitness;
    }

    public int getId() {
        return id;
    }

    public float getMaxFitness() {
        return maxFitness;
    }

    public ArrayList<Double> getChromosome() {
        return chromosome;
    }
    
}
