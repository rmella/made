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

import com.sun.org.apache.bcel.internal.generic.GETFIELD;

/**
 *
 * @author raiben@gmail.com
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
