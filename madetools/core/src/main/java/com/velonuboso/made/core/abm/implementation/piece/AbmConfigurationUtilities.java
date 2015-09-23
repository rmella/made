/*
 * Copyright (C) 2015 rhgarcia
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
import java.util.function.DoublePredicate;

/**
 *
 * @author Rubén Héctor García (raiben@gmail.com)
 */
public class AbmConfigurationUtilities {

    public static boolean valueIsProbability(float value) {
        return value >= 0 && value <= 1;
    }

    public static void checkValueInteger(float value, int ordinal, int minimumValue, int maximumValue) throws Exception {
        if (valueOutOfRange(value, minimumValue, maximumValue)) {
            throw new Exception("value for gene " + ordinal + " is not between " + minimumValue + " and " + maximumValue);
        }
        if (valueHasDecimals(value)) {
            throw new Exception("value for gene " + ordinal + " is not an integer");
        }
    }

    private static boolean valueHasDecimals(float value) {
        return value != (int)value;
    }

    private static boolean valueOutOfRange(float value, int minimumValue, int maximumValue) {
        return value < minimumValue || value > maximumValue;
    }

    public static void checkValueProbability(float value, int ordinal) throws Exception {
        if (!valueIsProbability(value)) {
            throw new Exception("value for gene " + ordinal + " is not a probability between 0 and 1");
        }
    }
    
    public static float getGene(AbmConfigurationHelperWorld.Gene gene, AbmConfigurationEntity abmConfigurationEntity) {
        return abmConfigurationEntity.getChromosome()[gene.ordinal()];
    }

    public static float getGene(AbmConfigurationHelperPiece.Gene gene, AbmConfigurationEntity abmConfigurationEntity) {
        return abmConfigurationEntity.getChromosome()[gene.ordinal()];
    }
}
