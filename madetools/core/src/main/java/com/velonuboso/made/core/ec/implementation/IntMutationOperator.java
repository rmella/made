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

import com.velonuboso.made.core.ec.api.IIntGene;
import com.velonuboso.made.core.ec.api.IIntMutationOperator;
import com.velonuboso.made.core.ec.entity.GeneDefinition;

/**
 *
 * @author Rubén Héctor García (raiben@gmail.com)
 */
public class IntMutationOperator implements IIntMutationOperator{

    @Override
    public void mutate(GeneDefinition targetGeneDefinition, IIntGene gene, float distanceParameterMutationDistribution) {
        PolynomialMutationHelper polynomialHelper = new PolynomialMutationHelper();
        gene.setValue(Math.round(polynomialHelper.mutate(targetGeneDefinition, gene.getValue(), distanceParameterMutationDistribution)));
    }
    
}
