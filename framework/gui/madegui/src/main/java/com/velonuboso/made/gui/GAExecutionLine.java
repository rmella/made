/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.velonuboso.made.gui;

import java.util.ArrayList;

/**
 *
 * @author rhgarcia
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
