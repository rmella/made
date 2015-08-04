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

/**
 *
 * @author Rubén Héctor García (raiben@gmail.com)
 */
public class PieceAbmConfigurationHelper {
   
    public static final int SHAPE_SIMILARITY_WEIGHT = 0;
    public static final int FOREGROUND_COLOR_SIMILARITY_WEIGHT = 1;
    public static final int BACKGROUND_COLOR_SIMILARITY_WEIGHT = 2;
    
    private AbmConfigurationEntity abmConfiguration;

    public PieceAbmConfigurationHelper(AbmConfigurationEntity abmConfiguration) {
        this.abmConfiguration = abmConfiguration;
    }

    float getShapeSimilarityWeight() {
        return getGene(SHAPE_SIMILARITY_WEIGHT);
    }

    float getForegroundColorSimilarityWeight() {
        return getGene(FOREGROUND_COLOR_SIMILARITY_WEIGHT);
    }

    float getBackgroundColorSimilarityWeight() {
        return getGene(BACKGROUND_COLOR_SIMILARITY_WEIGHT);
    } 

    private float getGene(int position) {
        return abmConfiguration.getChromosome()[position];
    }
}