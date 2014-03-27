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

import java.util.Properties;

/**
 *
 * @author raiben@gmail.com
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

    public GlobalSetup(Properties p) {
        this.foodPerDay = Integer.parseInt(p.getProperty("txtFoodPerDay"));
        this.mapGridDimension = Integer.parseInt(p.getProperty("txtMapGridDimension"));
        this.numberOfInitialAgents = Integer.parseInt(p.getProperty("txtNumberOfInitialAgents"));
        this.numberOfProfiles = Integer.parseInt(p.getProperty("txtNumberOfProfiles"));
        this.numberOfVirtualDays = Integer.parseInt(p.getProperty("txtNumberOfVirtualDays"));
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
