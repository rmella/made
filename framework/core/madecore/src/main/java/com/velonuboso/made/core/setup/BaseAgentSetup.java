/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.velonuboso.made.core.setup;

import com.sun.org.apache.bcel.internal.generic.GETFIELD;

/**
 *
 * @author rhgarcia
 */
public class BaseAgentSetup {

    private int baseAgeToBeAdultFemale;
    private int baseAgeToBeAdultMale;
    private int baseBite;
    private int baseEnergy;
    private int baseFur;
    private int baseLifeInDays;
    private int baseNutrition;
    private int baseSmell;
    private int basePregnancyDays;

    public BaseAgentSetup(int baseAgeToBeAdultFemale, int baseAgeToBeAdultMale, int baseBite, int baseEnergy, int baseFur, int baseLifeInDays, int baseNutrition, int baseSmell, int basePregnancyDays) {
        this.baseAgeToBeAdultFemale = baseAgeToBeAdultFemale;
        this.baseAgeToBeAdultMale = baseAgeToBeAdultMale;
        this.baseBite = baseBite;
        this.baseEnergy = baseEnergy;
        this.baseFur = baseFur;
        this.baseLifeInDays = baseLifeInDays;
        this.baseNutrition = baseNutrition;
        this.baseSmell = baseSmell;
        this.basePregnancyDays = basePregnancyDays;
    }

    public int getBaseAgeToBeAdultFemale() {
        return baseAgeToBeAdultFemale;
    }

    public int getBaseAgeToBeAdultMale() {
        return baseAgeToBeAdultMale;
    }

    public int getBaseBite() {
        return baseBite;
    }

    public int getBaseEnergy() {
        return baseEnergy;
    }

    public int getBaseFur() {
        return baseFur;
    }

    public int getBaseLifeInDays() {
        return baseLifeInDays;
    }

    public int getBaseNutrition() {
        return baseNutrition;
    }

    public int getBasePregnancyDays() {
        return basePregnancyDays;
    }

    public int getBaseSmell() {
        return baseSmell;
    }

    
}
