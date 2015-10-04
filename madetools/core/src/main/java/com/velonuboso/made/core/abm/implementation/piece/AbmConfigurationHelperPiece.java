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
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

/**
 *
 * @author Rubén Héctor García (raiben@gmail.com)
 */
public class AbmConfigurationHelperPiece {

    private AbmConfigurationEntity abmConfiguration;

    public AbmConfigurationHelperPiece(AbmConfigurationEntity abmConfiguration) {
        this.abmConfiguration = abmConfiguration;
    }

    public AbmConfigurationEntity getAbmConfiguration() {
        return abmConfiguration;
    }
    
    public float getShapeSimilarityWeight() {
        return getGene(Gene.SHAPE_SIMILARITY_WEIGHT);
    }

    public float getForegroundColorSimilarityWeight() {
        return getGene(Gene.FOREGROUND_COLOR_SIMILARITY_WEIGHT);
    }

    public float getBackgroundColorSimilarityWeight() {
        return getGene(Gene.BACKGROUND_COLOR_SIMILARITY_WEIGHT);
    }

    public float getNeighbourSimilarityForJoyWeight() {
        return getGene(Gene.NEIGHBOUR_SIMILARITY_FOR_JOY_WEIGHT);
    }

    public float getSelfSimilarityForJoyWeight() {
        return 1f - getNeighbourSimilarityForJoyWeight();
    }

    public float getJoyThreshold() {
        return getGene(Gene.JOY_THRESHOLD);
    }

    public float getSurpriseThreshold() {
        return getGene(Gene.SURPRISE_THRESHOLD);
    }

    public float getAnticipationProbability() {
        return getGene(Gene.ANTICIPATION_PROBABILITY);
    }

    public float getFearProbability() {
        return getGene(Gene.FEAR_PROBABILITY);
    }

    public float getSurpriseProbability() {
        return getGene(Gene.SURPRISE_PROBABILITY);
    }

    public float getSadnessProbability() {
        return getGene(Gene.SADNESS_PROBABILITY);
    }

    public float getImprovingFriendSimilarityProbability() {
        return getGene(Gene.IMPROVE_FRIEND_SIMILARITY_PROBABILITY);
    }

    public float getReducingEnemySimilarityProbability() {
        return getGene(Gene.REDUCE_ENEMY_SIMILARITY_PROBABILITY);
    }

    public float getImprovingSelfSimilarityProbability() {
        return getGene(Gene.IMPROVE_SELF_SIMILARITY_PROBABILITY);
    }
    
    public float getColorChangeCrowdingDegree() {
        return getGene(Gene.COLOR_CHANGE_CROWDING_DEGREE_PER_TURN);
    }

    float getTrustProbability() {
        return getGene(Gene.TRUST_PROBABILITY);
    }
    
    private float getGene(Gene gene) {
        return AbmConfigurationUtilities.getGene(gene, abmConfiguration);
    }

    void validateTypes() throws Exception {
        float[] chromosome = abmConfiguration.getChromosome();
        DoubleStream streamOfDoubles = IntStream.range(0, chromosome.length)
                .mapToDouble(index -> chromosome[index]);

        boolean allGenesBetweenZeroAndOne = streamOfDoubles.allMatch(value
                -> AbmConfigurationUtilities.valueIsProbability((float) value));

        if (!allGenesBetweenZeroAndOne) {
            throw new Exception("Not all the genes are between 0 and 1 (inclusive)");
        }
    }

    public enum Gene {
        SHAPE_SIMILARITY_WEIGHT,
        FOREGROUND_COLOR_SIMILARITY_WEIGHT,
        BACKGROUND_COLOR_SIMILARITY_WEIGHT,
        NEIGHBOUR_SIMILARITY_FOR_JOY_WEIGHT,
        JOY_THRESHOLD,
        SURPRISE_THRESHOLD,
        ANTICIPATION_PROBABILITY,
        FEAR_PROBABILITY,
        SURPRISE_PROBABILITY,
        SADNESS_PROBABILITY,
        IMPROVE_FRIEND_SIMILARITY_PROBABILITY,
        REDUCE_ENEMY_SIMILARITY_PROBABILITY,
        IMPROVE_SELF_SIMILARITY_PROBABILITY,
        TRUST_PROBABILITY,
        COLOR_CHANGE_CROWDING_DEGREE_PER_TURN
    }
}
