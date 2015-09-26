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
import com.velonuboso.made.core.ec.api.IGene;
import com.velonuboso.made.core.ec.api.IIntCrossoverOperator;
import com.velonuboso.made.core.ec.api.IIntGene;
import com.velonuboso.made.core.ec.entity.GeneDefinition;

/**
 *
 * @author Rubén Héctor García (raiben@gmail.com)
 */
public class IntCrossoverOperatorBlxAlpha implements IIntCrossoverOperator{
    @Override
    public IGene crossover(GeneDefinition geneDefinition, IIntGene firstGene, IIntGene secondGene, float blxAlpha) {
        int max = Math.max((int)firstGene.getValue(), (int)secondGene.getValue());
        int min = Math.min((int)firstGene.getValue(), (int)secondGene.getValue());
        
        int difference = max-min;
        
        int maxBoundary = Math.round(max+(difference*blxAlpha));
        maxBoundary = Math.min(maxBoundary, (int) geneDefinition.getMaxValue());
        
        int minBoundary = Math.round(min-(difference*blxAlpha));
        minBoundary = Math.max(minBoundary, (int) geneDefinition.getMinValue());
        
        IProbabilityHelper helper = ObjectFactory.createObject(IProbabilityHelper.class);
        int newValue = helper.getNextInt(minBoundary, maxBoundary);
        
        IIntGene newGene = ObjectFactory.createObject(IIntGene.class);
        newGene.setValue(newValue);
        
        return newGene;
    }
}
