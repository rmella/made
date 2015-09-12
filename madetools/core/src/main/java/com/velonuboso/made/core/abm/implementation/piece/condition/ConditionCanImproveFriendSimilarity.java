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
package com.velonuboso.made.core.abm.implementation.piece.condition;

import com.velonuboso.made.core.abm.api.IBlackBoard;
import com.velonuboso.made.core.abm.api.ICharacter;
import com.velonuboso.made.core.abm.api.IColorSpot;
import com.velonuboso.made.core.abm.api.condition.IConditionCanImproveFriendSimilarity;
import com.velonuboso.made.core.abm.implementation.piece.Piece;
import com.velonuboso.made.core.abm.implementation.piece.PieceUtilities;
import com.velonuboso.made.core.common.api.IEvent;
import com.velonuboso.made.core.common.api.IEventFactory;
import com.velonuboso.made.core.common.util.ObjectFactory;
import java.util.Comparator;
import java.util.HashMap;
import javafx.scene.paint.Color;

/**
 *
 * @author Rubén Héctor García (raiben@gmail.com)
 */
public class ConditionCanImproveFriendSimilarity extends BaseCondition implements IConditionCanImproveFriendSimilarity {

    @Override
    public boolean test(IBlackBoard blackboard) {
        ICharacter candidateToExchangeColors = getBestCandidateToExchangeColor(blackboard);

        if (candidateToExchangeColors != null) {
            storeCandidateToExchangeColorsIntoBlackboard(candidateToExchangeColors, blackboard);
            writeEvent(candidateToExchangeColors);
        }
        return candidateToExchangeColors != null;
    }

    private ICharacter getBestCandidateToExchangeColor(IBlackBoard blackboard) {
        HashMap<ICharacter, Float> affinityMatrix
                = (HashMap<ICharacter, Float>) blackboard.getObject(Piece.BLACKBOARD_AFFINITY_MATRIX);
        
        ICharacter candidateToExchangeColors = affinityMatrix.keySet().stream()
                .filter(targetCharacter -> !targetCharacter.getShape().wins(this.character.getShape()))
                .max((ICharacter firstCharacter, ICharacter secondCharacter) -> {
                    float benefitWithFirstCharacter = getColorExchangeBenefit(firstCharacter);
                    float benefitWithSecondCharacter = getColorExchangeBenefit(secondCharacter);
                    return Float.compare(benefitWithFirstCharacter, benefitWithSecondCharacter);
                })
                .orElse(null);
        return candidateToExchangeColors;
    }

    private float getColorExchangeBenefit(ICharacter targetCharacter) {
        float currentSumOfSimilarities = getCurrentSumOfSimilarities(targetCharacter);
        float futureSumOfSimilarities = getFutureSumOfSimilarities(targetCharacter);
        return futureSumOfSimilarities - currentSumOfSimilarities;
    }

    private float getFutureSumOfSimilarities(ICharacter targetCharacter) {
        float futureSimilarityInCharacter = 1 - PieceUtilities.calculateColorDifference(
                character.getForegroundColor(), targetCharacter.getBackgroundColor());
        float futureSimilarityInTargetCharacter = 1 - PieceUtilities.calculateColorDifference(
                targetCharacter.getForegroundColor(), character.getBackgroundColor());
        return futureSimilarityInCharacter + futureSimilarityInTargetCharacter;
    }

    private float getCurrentSumOfSimilarities(ICharacter targetCharacter) {
        float currentSimilarityInCharacter = 1 - character.getColorDifference();
        float currentSimilarityInTargetCharacter = 1 - targetCharacter.getColorDifference();
        return currentSimilarityInCharacter + currentSimilarityInTargetCharacter;
    }

    private void storeCandidateToExchangeColorsIntoBlackboard(ICharacter candidate, IBlackBoard blackBoard) {
        blackBoard.setInt(Piece.BLACKBOARD_CHARACTER_CELL, getMap().getCell(candidate));
    }

    private void writeEvent(ICharacter targetCharacter) {
        IEventFactory eventFactory = ObjectFactory.createObject(IEventFactory.class);
        IEvent anticipationEvent = eventFactory.canImproveFriendSimilarity(character, targetCharacter);
        character.getEventsWriter().add(anticipationEvent);
    }
}
