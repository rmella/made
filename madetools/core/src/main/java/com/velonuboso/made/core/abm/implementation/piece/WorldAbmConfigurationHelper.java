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

import com.velonuboso.made.core.common.entity.AbmConfigurationEntity;
import java.util.Arrays;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

/**
 *
 * @author Rubén Héctor García (raiben@gmail.com)
 */
public class WorldAbmConfigurationHelper {

    public int MIN_WORLD_SIZE = 10;
    public int MAX_WORLD_SIZE = 20;
    public int MIN_NUMBER_OF_CIRCLES = 0;
    public int MAX_NUMBER_OF_CIRCLES = 30;
    public int MIN_NUMBER_OF_TRIANGLES = 0;
    public int MAX_NUMBER_OF_TRIANGLES = 30;
    public int MIN_NUMBER_OF_SQUARES = 0;
    public int MAX_NUMBER_OF_SQUARES = 30;
    public int MIN_NUMBER_OF_DAYS = 1;
    public int MAX_NUMBER_OF_DAYS = 100;

    private AbmConfigurationEntity abmConfiguration;

    public WorldAbmConfigurationHelper(AbmConfigurationEntity abmConfiguration) {
        this.abmConfiguration = abmConfiguration;
    }

    private float getGene(Gene gene) {
        return abmConfiguration.getChromosome()[gene.ordinal()];
    }

    public void validateTypes() throws Exception {
        checkValueInteger(Gene.WORLD_SIZE, MIN_WORLD_SIZE, MAX_WORLD_SIZE);
        checkValueInteger(Gene.NUMBER_OF_CIRCLES, MIN_NUMBER_OF_CIRCLES, MAX_NUMBER_OF_CIRCLES);
        checkValueInteger(Gene.NUMBER_OF_TRIANGLES, MIN_NUMBER_OF_TRIANGLES, MAX_NUMBER_OF_TRIANGLES);
        checkValueInteger(Gene.NUMBER_OF_SQUARES, MIN_NUMBER_OF_SQUARES, MAX_NUMBER_OF_SQUARES);
        checkValueInteger(Gene.NUMBER_OF_DAYS, MIN_NUMBER_OF_DAYS, MAX_NUMBER_OF_DAYS);
        checkValueProbability(Gene.PROBABILITY_TO_ADD_SPOT, 0, 1);
        checkValueProbability(Gene.PROBABILITY_TO_REMOVE_SPOT, 0, 1);
    }

    private void checkValueInteger(Gene gene, int minimumValue, int maximumValue) throws Exception {
        float value = getGene(gene);
        if (value < minimumValue && value > maximumValue) {
            throw new Exception("value for gene " + gene.ordinal() + " is not between " + minimumValue + " and " + maximumValue);
        }
        if (Math.abs(value) != value) {
            throw new Exception("value for gene " + gene.ordinal() + " is not an integer");
        }
    }

    private void checkValueProbability(Gene gene, int minimumValue, int maximumValue) throws Exception {
        float value = getGene(gene);
        if (value < 0 && value > 1) {
            throw new Exception("value for gene " + gene.ordinal() + " is not a probability between 0 and 1");
        }
    }

    public enum Gene {
        WORLD_SIZE,
        NUMBER_OF_CIRCLES,
        NUMBER_OF_TRIANGLES,
        NUMBER_OF_SQUARES,
        NUMBER_OF_DAYS,
        PROBABILITY_TO_ADD_SPOT,
        PROBABILITY_TO_REMOVE_SPOT
    }
}
