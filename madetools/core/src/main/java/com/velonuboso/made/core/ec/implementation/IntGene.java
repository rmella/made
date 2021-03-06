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

import com.velonuboso.made.core.common.util.ObjectFactory;
import com.velonuboso.made.core.ec.api.ICrossoverOperator;
import com.velonuboso.made.core.ec.api.IGene;
import com.velonuboso.made.core.ec.api.IIntCrossoverOperator;
import com.velonuboso.made.core.ec.api.IIntGene;
import com.velonuboso.made.core.ec.api.IIntMutationOperator;
import com.velonuboso.made.core.ec.entity.GeneDefinition;

/**
 *
 * @author Rubén Héctor García (raiben@gmail.com)
 */
public class IntGene implements IIntGene {
    int value;

    public IntGene() {
        value = 0;
    }
    
    @Override
    public float getValue() {
        return (float)value;
    }

    @Override
    public void setValue(float value) {
        this.value = (int)value;
    }

    @Override
    public String toString() {
        return String.valueOf((int)value);
    }

    @Override
    public IGene crossover(GeneDefinition targetGeneDefinition, IGene targetGene, float blxAlpha) {
        IIntCrossoverOperator operator = ObjectFactory.createObject(IIntCrossoverOperator.class);
        return operator.crossover(targetGeneDefinition, this, (IIntGene) targetGene, blxAlpha); 
    }

    @Override
    public void mutate(GeneDefinition targetGeneDefinition, float distanceParameterMutationDistribution) {
        IIntMutationOperator operator = ObjectFactory.createObject(IIntMutationOperator.class);
        operator.mutate(targetGeneDefinition, this, distanceParameterMutationDistribution);}
}
