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
package com.velonuboso.made.core.ec.implementation;

import com.velonuboso.made.core.common.api.IProbabilityHelper;
import com.velonuboso.made.core.common.util.ObjectFactory;
import com.velonuboso.made.core.ec.entity.GeneDefinition;

/**
 *
 * @author Rubén Héctor García (raiben@gmail.com)
 */
public class PolynomialMutationHelper {
    
    public float mutate(GeneDefinition targetGeneDefinition, float currentValue,
            float distanceParameterMutationDistribution) {
        
        IProbabilityHelper helper = ObjectFactory.createObject(IProbabilityHelper.class);
        float randomProbability = helper.getNextProbability(FloatMutationOperator.class);
        
        float minValue = targetGeneDefinition.getMinValue();
        float maxValue = targetGeneDefinition.getMaxValue();
        float deltaToMin = (currentValue - minValue) / (maxValue - minValue);
        float deltaToMax = (maxValue - currentValue) / (maxValue - minValue);
        
        float power = 1f / (distanceParameterMutationDistribution + 1f);
        
        float deltaFinal;
        if (randomProbability <= 0.5) {
            float xy = 1f - deltaToMin;
            float val = (float) (2f * randomProbability + (1f - 2f * randomProbability) * (Math.pow(xy, (distanceParameterMutationDistribution + 1f))));
            deltaFinal = (float) (Math.pow(val, power) - 1.0);
        } else {
            float xy = 1f - deltaToMax;
            float val = (float) (2f * (1f - randomProbability) + 2f * (randomProbability - 0.5) * (Math.pow(xy, (distanceParameterMutationDistribution + 1f))));
            deltaFinal = (float) (1f - (Math.pow(val, power)));
        }
        
        currentValue = currentValue + deltaFinal * (maxValue - minValue);
        if (currentValue < minValue) {
            currentValue = minValue;
        }
        if (currentValue > maxValue) {
            currentValue = maxValue;
        }
        return currentValue;
    }
}
