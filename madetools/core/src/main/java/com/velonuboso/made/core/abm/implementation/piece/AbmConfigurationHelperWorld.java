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
package com.velonuboso.made.core.abm.implementation.piece;

import com.velonuboso.made.core.common.api.IGlobalConfigurationFactory;
import com.velonuboso.made.core.common.entity.AbmConfigurationEntity;
import com.velonuboso.made.core.common.entity.CommonAbmConfiguration;
import com.velonuboso.made.core.common.util.ObjectFactory;

/**
 *
 * @author Rubén Héctor García (raiben@gmail.com)
 */
public class AbmConfigurationHelperWorld {

    private AbmConfigurationEntity abmConfiguration;
    
    public AbmConfigurationHelperWorld(AbmConfigurationEntity abmConfiguration) {
        this.abmConfiguration = abmConfiguration;
    }

    public AbmConfigurationEntity getAbmConfiguration() {
        return abmConfiguration;
    }

    public void validateTypes() throws Exception {
        IGlobalConfigurationFactory globalConfigurationFactory = 
            ObjectFactory.createObject(IGlobalConfigurationFactory.class);
        CommonAbmConfiguration config = globalConfigurationFactory.getCommonAbmConfiguration();
        
        checkValueInteger(Gene.WORLD_SIZE, config.MIN_WORLD_SIZE, config.MAX_WORLD_SIZE);
        checkValueInteger(Gene.NUMBER_OF_CIRCLES, config.MIN_NUMBER_OF_CIRCLES, config.MAX_NUMBER_OF_CIRCLES);
        checkValueInteger(Gene.NUMBER_OF_TRIANGLES, config.MIN_NUMBER_OF_TRIANGLES, config.MAX_NUMBER_OF_TRIANGLES);
        checkValueInteger(Gene.NUMBER_OF_SQUARES, config.MIN_NUMBER_OF_SQUARES, config.MAX_NUMBER_OF_SQUARES);
        checkValueInteger(Gene.NUMBER_OF_DAYS, config.MIN_NUMBER_OF_DAYS, config.MAX_NUMBER_OF_DAYS);
        checkValueProbability(Gene.PROBABILITY_TO_ADD_SPOT);
        checkValueProbability(Gene.PROBABILITY_TO_REMOVE_SPOT);
    }

    private void checkValueInteger(Gene gene, int from, int to) throws Exception {
        AbmConfigurationUtilities.checkValueInteger(getGene(gene), gene.ordinal(),  from, to);    
    }

    private void checkValueProbability(Gene gene) throws Exception {
        AbmConfigurationUtilities.checkValueProbability(getGene(gene), gene.ordinal());
    }

    private float getGene(Gene gene) {
        return AbmConfigurationUtilities.getGene(gene, abmConfiguration);
    }

    public int getNumberOfDays() {
        return (int) getGene(Gene.NUMBER_OF_DAYS);
    }

    public int getWorldSize() {
        return (int) getGene(Gene.WORLD_SIZE);
    }

    public int getNumberOfSquares() {
        return (int) getGene(Gene.NUMBER_OF_SQUARES);
    }

    public int getNumberOfCircles() {
        return (int) getGene(Gene.NUMBER_OF_CIRCLES);
    }
    
    public int getNumberOfTraingles() {
        return (int) getGene(Gene.NUMBER_OF_TRIANGLES);
    }
    
    public float getProbabilityToAddSpot(){
        return getGene(Gene.PROBABILITY_TO_ADD_SPOT);
    }
    
    public float getProbabilityToRemoveSpot(){
        return getGene(Gene.PROBABILITY_TO_REMOVE_SPOT);
    }
    
    public enum Gene {
        WORLD_SIZE,
        NUMBER_OF_CIRCLES,
        NUMBER_OF_TRIANGLES,
        NUMBER_OF_SQUARES,
        NUMBER_OF_DAYS,
        PROBABILITY_TO_ADD_SPOT,
        PROBABILITY_TO_REMOVE_SPOT,
    }
}
