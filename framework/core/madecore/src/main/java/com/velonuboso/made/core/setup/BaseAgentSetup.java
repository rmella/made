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
import java.util.Properties;

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
    
    public BaseAgentSetup(Properties p) {
        this.baseAgeToBeAdultFemale = Integer.parseInt(p.getProperty("txtBaseAgeToBeAdultFemale"));
        this.baseAgeToBeAdultMale = Integer.parseInt(p.getProperty("txtBaseAgeToBeAdultMale"));
        this.baseBite = Integer.parseInt(p.getProperty("txtBaseBite"));
        this.baseEnergy = Integer.parseInt(p.getProperty("txtBaseEnergy"));
        this.baseFur = Integer.parseInt(p.getProperty("txtBaseFur"));
        this.baseLifeInDays = Integer.parseInt(p.getProperty("txtBaseLifeInDays"));
        this.baseNutrition = Integer.parseInt(p.getProperty("txtBaseNutrition"));
        this.baseSmell = Integer.parseInt(p.getProperty("txtBaseSmell"));
        this.basePregnancyDays = Integer.parseInt(p.getProperty("txtbasePregnancyDays"));
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
