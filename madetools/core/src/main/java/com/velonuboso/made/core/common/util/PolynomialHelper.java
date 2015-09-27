/*
 * Copyright (C) 2015 Rubén Héctor García (raiben@gmail.com)
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
package com.velonuboso.made.core.common.util;

import com.velonuboso.made.core.common.api.IProbabilityHelper;
import com.velonuboso.made.core.ec.implementation.FloatMutationOperator;

/**
 *
 * @author Rubén Héctor García (raiben@gmail.com)
 */
public class PolynomialHelper {
    
    public float mutate(float minValue, float maxValue, float currentValue,
            float distanceParameterMutationDistribution) {
        
        float randomProbability = getRandomProbability();
        float deltaToMinimum = getDeltaToMinimum(currentValue, minValue, maxValue);
        float deltaToMaximum = getDeltaToMaximum(currentValue, minValue, maxValue);
        float power = getPower(distanceParameterMutationDistribution);
        
        float deltaFinal = (randomProbability <= 0.5) ? 
            getDeltaWhenNumberBelowCurrent(deltaToMinimum, randomProbability, 
                    distanceParameterMutationDistribution, power):
            getDeltaWhenNumberOverCurrent(deltaToMaximum, randomProbability, 
                    distanceParameterMutationDistribution, power);
        
        currentValue = currentValue + deltaFinal * (maxValue - minValue);
        return currentValueBetweenBounds(currentValue, minValue, maxValue);
    }

    private float getDeltaWhenNumberOverCurrent(float deltaToMaximum, float randomProbability, 
            float distanceParameterMutationDistribution, float power) {
        float inverseToDeltaMaximum = getInverseProbability(deltaToMaximum);
        float valueToRaise = (float) (2f * (1f - randomProbability) 
                + 2f * (randomProbability - 0.5) 
                * (Math.pow(inverseToDeltaMaximum, (distanceParameterMutationDistribution + 1f))));
        return (float) (1f - (Math.pow(valueToRaise, power)));
    }

    private float getDeltaWhenNumberBelowCurrent(float deltaToMinimum, float randomProbability, float distanceParameterMutationDistribution, float power) {
        float inverseToDeltaMinimum = getInverseProbability(deltaToMinimum);
        float valueToRaise = (float) (2f * randomProbability 
                + (1f - 2f * randomProbability) 
                * (Math.pow(inverseToDeltaMinimum, (distanceParameterMutationDistribution + 1f))));
        return (float) (Math.pow(valueToRaise, power) - 1.0);
    }

    private float getPower(float distanceParameterMutationDistribution) {
        return 1f / (distanceParameterMutationDistribution + 1f);
    }

    private float getDeltaToMaximum(float currentValue, float minValue, float maxValue) {
        return (maxValue - currentValue) / (maxValue - minValue);
    }

    private float getDeltaToMinimum(float currentValue, float minValue, float maxValue) {
        return (currentValue - minValue) / (maxValue - minValue);
    }

    private float getRandomProbability() {
        IProbabilityHelper helper = ObjectFactory.createObject(IProbabilityHelper.class);
        return helper.getNextProbability(FloatMutationOperator.class);
    }
    
    private float getInverseProbability(float deltaToMinimum) {
        return 1f - deltaToMinimum;
    }
    
    private float currentValueBetweenBounds(float currentValue, float minValue, float maxValue) {
        if (currentValue < minValue) {
            return minValue;
        }
        if (currentValue > maxValue) {
            return maxValue;
        }        
        return currentValue;
    }

}
