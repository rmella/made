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
package com.velonuboso.made.core.optimization.implementation;

import com.velonuboso.made.core.abm.implementation.piece.AbmConfigurationHelper;
import com.velonuboso.made.core.abm.implementation.piece.AbmConfigurationHelperWorld;
import com.velonuboso.made.core.common.util.ObjectFactory;
import com.velonuboso.made.core.ec.api.IGeneticAlgorithm;
import com.velonuboso.made.core.ec.api.IGeneticAlgorithmListener;
import com.velonuboso.made.core.ec.api.IIndividual;
import com.velonuboso.made.core.ec.entity.GeneDefinition;
import com.velonuboso.made.core.ec.entity.GeneType;
import com.velonuboso.made.core.ec.entity.IndividualDefinition;
import com.velonuboso.made.core.ec.implementation.GeneticAlgorithm;
import com.velonuboso.made.core.optimization.api.*;
import java.util.Arrays;

/**
 *
 * @author Rubén Héctor García (raiben@gmail.com)
 */
public class Optimizer implements IOptimizer {

    private IGeneticAlgorithm geneticAlgorithm;

    private static final int NUMBER_OF_GENES = 52;
    private static final int GENE_WORLD_SIZE = 0;
    private static final int GENE_NUMBER_OF_CIRCLES = 1;
    private static final int GENE_NUMBER_OF_TRIANGLES = 2;
    private static final int GENE_NUMBER_OF_SQUARES = 3;
    private static final int GENE_NUMBER_OF_DAYS = 4;
    private static final int GENE_BEGIN_INDEX_OF_FLOATS = 5;
    
    public Optimizer() {
        geneticAlgorithm = null;
    }

    @Override
    public void configure(int populationSize, int maximumIterations, float blxAlpha, float distanceParameterMutationDistribution) {
        geneticAlgorithm = ObjectFactory.createObject(IGeneticAlgorithm.class);
        IndividualDefinition definition = getIndividualDefinition();
        geneticAlgorithm.configure(definition, populationSize, maximumIterations, blxAlpha, distanceParameterMutationDistribution);
        geneticAlgorithm.addListener(ObjectFactory.createObject(IGeneticAlgorithmListener.class));
    }

    private IndividualDefinition getIndividualDefinition() {
        GeneDefinition[] definitions = new GeneDefinition[NUMBER_OF_GENES];
        
        definitions[GENE_WORLD_SIZE] = new GeneDefinition(GeneType.INTEGER,
                AbmConfigurationHelperWorld.MIN_WORLD_SIZE,
                AbmConfigurationHelperWorld.MAX_WORLD_SIZE);
        definitions[GENE_NUMBER_OF_CIRCLES] = new GeneDefinition(GeneType.INTEGER,
                AbmConfigurationHelperWorld.MIN_NUMBER_OF_CIRCLES,
                AbmConfigurationHelperWorld.MAX_NUMBER_OF_CIRCLES);
        definitions[GENE_NUMBER_OF_TRIANGLES] = new GeneDefinition(GeneType.INTEGER,
                AbmConfigurationHelperWorld.MIN_NUMBER_OF_TRIANGLES,
                AbmConfigurationHelperWorld.MAX_NUMBER_OF_TRIANGLES);
        definitions[GENE_NUMBER_OF_SQUARES] = new GeneDefinition(GeneType.INTEGER,
                AbmConfigurationHelperWorld.MIN_NUMBER_OF_SQUARES,
                AbmConfigurationHelperWorld.MAX_NUMBER_OF_SQUARES);
        definitions[GENE_NUMBER_OF_DAYS] = new GeneDefinition(GeneType.INTEGER,
                AbmConfigurationHelperWorld.MIN_NUMBER_OF_DAYS,
                AbmConfigurationHelperWorld.MAX_NUMBER_OF_DAYS);
        Arrays.fill(definitions, GENE_BEGIN_INDEX_OF_FLOATS, NUMBER_OF_GENES, new GeneDefinition(GeneType.FLOAT, 0, 1));
        
        IndividualDefinition definition = new IndividualDefinition(definitions);
        return definition;
    }

}
