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

package com.velonuboso.made2.core.setup;

import java.util.Properties;
import java.util.Set;

/**
 *
 * @author raiben@gmail.com
 */
public class GASetup {

    private int txtExecutionsAVG;
    private int txtGenerations;
    private int txtPopulation;
    float txtCrossoverProbability;
    float txtMutationProbability;
    int txtStopCriteriaPeriod;
    float txtStopCriteriarelativeDifference;

    public GASetup(int txtExecutionsAVG, int txtGenerations, 
            int txtPopulation, float txtCrossoverProbability,
            float txtMutationProbability, int txtStopCriteriaPeriod,
            float txtStopCriteriarelativeDifference) {
        this.txtExecutionsAVG = txtExecutionsAVG;
        this.txtGenerations = txtGenerations;
        this.txtPopulation = txtPopulation;
        this.txtCrossoverProbability = txtCrossoverProbability;
        this.txtMutationProbability = txtMutationProbability;
        this.txtStopCriteriaPeriod = txtStopCriteriaPeriod;
        this.txtStopCriteriarelativeDifference = txtStopCriteriarelativeDifference;
    }

    public GASetup(Properties p) {
        this.txtExecutionsAVG = Integer.parseInt(p.getProperty("txtExecutionsAVG"));
        this.txtGenerations  = Integer.parseInt(p.getProperty("txtGenerations"));
        this.txtPopulation  = Integer.parseInt(p.getProperty("txtPopulation"));
        this.txtCrossoverProbability  = Float.parseFloat(p.getProperty("txtCrossoverProbability"));
        this.txtMutationProbability  = Float.parseFloat(p.getProperty("txtMutationProbability"));
        this.txtStopCriteriaPeriod  = Integer.parseInt(p.getProperty("txtStopCriteriaPeriod"));
        this.txtStopCriteriarelativeDifference  = Float.parseFloat(p.getProperty("txtStopCriteriarelativeDifference"));
        
    }

    
    
    public int getTxtExecutionsAVG() {
        return txtExecutionsAVG;
    }

    public int getTxtGenerations() {
        return txtGenerations;
    }

    public int getTxtPopulation() {
        return txtPopulation;
    }

    public float getTxtCrossoverProbability() {
        return txtCrossoverProbability;
    }

    public float getTxtMutationProbability() {
        return txtMutationProbability;
    }

    public int getTxtStopCriteriaPeriod() {
        return txtStopCriteriaPeriod;
    }

    public float getTxtStopCriteriarelativeDifference() {
        return txtStopCriteriarelativeDifference;
    }

    public void setTxtCrossoverProbability(float txtCrossoverProbability) {
        this.txtCrossoverProbability = txtCrossoverProbability;
    }

    public void setTxtExecutionsAVG(int txtExecutionsAVG) {
        this.txtExecutionsAVG = txtExecutionsAVG;
    }

    public void setTxtGenerations(int txtGenerations) {
        this.txtGenerations = txtGenerations;
    }

    public void setTxtMutationProbability(float txtMutationProbability) {
        this.txtMutationProbability = txtMutationProbability;
    }

    public void setTxtPopulation(int txtPopulation) {
        this.txtPopulation = txtPopulation;
    }

    public void setTxtStopCriteriaPeriod(int txtStopCriteriaPeriod) {
        this.txtStopCriteriaPeriod = txtStopCriteriaPeriod;
    }

    public void setTxtStopCriteriarelativeDifference(float txtStopCriteriarelativeDifference) {
        this.txtStopCriteriarelativeDifference = txtStopCriteriarelativeDifference;
    }

}
