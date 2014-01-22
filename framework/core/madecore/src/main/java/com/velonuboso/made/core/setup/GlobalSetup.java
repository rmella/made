/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.velonuboso.made.core.setup;

/**
 *
 * @author rhgarcia
 */
public class GlobalSetup {

    private int foodPerDay;
    private int mapGridDimension;
    private int numberOfInitialAgents;
    private int numberOfProfiles;
    private int numberOfVirtualDays;

    public GlobalSetup(int foodPerDay, int mapGridDimension, int numberOfInitialAgents, int numberOfProfiles, int numberOfVirtualDays) {
        this.foodPerDay = foodPerDay;
        this.mapGridDimension = mapGridDimension;
        this.numberOfInitialAgents = numberOfInitialAgents;
        this.numberOfProfiles = numberOfProfiles;
        this.numberOfVirtualDays = numberOfVirtualDays;
    }

    public int getFoodPerDay() {
        return foodPerDay;
    }

    public int getMapGridDimension() {
        return mapGridDimension;
    }

    public int getNumberOfInitialAgents() {
        return numberOfInitialAgents;
    }

    public int getNumberOfProfiles() {
        return numberOfProfiles;
    }

    public int getNumberOfVirtualDays() {
        return numberOfVirtualDays;
    }
    
}
