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
import com.velonuboso.made.core.ec.api.IFloatCrossoverOperator;
import com.velonuboso.made.core.ec.api.IFloatGene;
import com.velonuboso.made.core.ec.api.IGene;
import com.velonuboso.made.core.ec.entity.GeneDefinition;

/**
 *
 * @author Rubén Héctor García (raiben@gmail.com)
 */
public class FloatCrossoverOperatorBlxAlpha implements IFloatCrossoverOperator{

    @Override
    public IGene crossover(GeneDefinition geneDefinition, IFloatGene firstGene, IFloatGene secondGene, float blxAlpha) {
        float max = Math.max(firstGene.getValue(), secondGene.getValue());
        float min = Math.min(firstGene.getValue(), secondGene.getValue());
        
        float difference = max-min;
        
        float maxBoundary = max+(difference*blxAlpha);
        maxBoundary = Math.min(maxBoundary, geneDefinition.getMaxValue());
        
        float minBoundary = min-(difference*blxAlpha);
        minBoundary = Math.max(minBoundary, geneDefinition.getMinValue());
        
        IProbabilityHelper helper = ObjectFactory.createObject(IProbabilityHelper.class);
        float newValue = helper.getNextFloat(minBoundary, maxBoundary);
        
        IFloatGene newGene = ObjectFactory.createObject(IFloatGene.class);
        newGene.setValue(newValue);
        
        return newGene;
    
    }
    
}
