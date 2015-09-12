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
import com.velonuboso.made.core.abm.api.IColorSpot;
import com.velonuboso.made.core.abm.api.condition.IConditionAnticipation;
import com.velonuboso.made.core.abm.implementation.piece.Piece;
import com.velonuboso.made.core.abm.implementation.piece.PieceUtilities;
import com.velonuboso.made.core.common.api.IEvent;
import com.velonuboso.made.core.common.api.IEventFactory;
import com.velonuboso.made.core.common.util.ObjectFactory;
import java.util.List;
import javafx.scene.paint.Color;

/**
 *
 * @author Rubén Héctor García (raiben@gmail.com)
 */
public class ConditionAnticipation extends BaseCondition implements IConditionAnticipation {

    @Override
    public boolean test(IBlackBoard currentBlackBoard, IBlackBoard oldBlackBoard) {
        IColorSpot spot = getAdjacentColorSpot(currentBlackBoard);
        if (spot != null) {
            storeSpotCellIntoBlackboard(spot, currentBlackBoard);
            writeEvent(spot);
        }
        return spot != null;
    }

    private IColorSpot getAdjacentColorSpot(IBlackBoard blackBoard) {
        int currentCharacterPosition = getMap().getCell(character);
        List<Integer> cellsToLookAt = getMap().getCellsAround(currentCharacterPosition, 1);

        IColorSpot spot = cellsToLookAt.stream()
                .map(cell -> getMap().getColorSpot(cell))
                .filter(spotIncell -> spotIncell != null 
                        && betterColorThanCharacterBackground(spotIncell)
                        && notOccupiedByOtherCharacter(spotIncell))
                .max((IColorSpot firstSpot, IColorSpot secondSpot) -> {
                    float firstDifference = getSimilarityWithForeground(firstSpot.getColor());
                    float SecondDifference = getSimilarityWithForeground(secondSpot.getColor());
                    return Float.compare(firstDifference, SecondDifference);
                })
                .orElse(null);
        return spot;
    }

    private boolean betterColorThanCharacterBackground(IColorSpot spotIncell) {
        float similarityOfSpot = getSimilarityWithForeground(spotIncell.getColor());
        float similarityOfCurrentBackground = getSimilarityWithForeground(character.getBackgroundColor());
        return similarityOfSpot > similarityOfCurrentBackground;
    }
    
    private float getSimilarityWithForeground(Color color) {
        return 1f - PieceUtilities.calculateColorDifference(color, character.getForegroundColor());
    }

    private boolean notOccupiedByOtherCharacter(IColorSpot spotIncell) {
        int cellOfSpot = getMap().getCell(spotIncell);
        return getMap().getCharacter(cellOfSpot) == null;
    }
    
    private void storeSpotCellIntoBlackboard(IColorSpot spot, IBlackBoard blackBoard) {
        blackBoard.setInt(Piece.BLACKBOARD_SPOT_CELL, getMap().getCell(spot));
    }

    private void writeEvent(IColorSpot spot) {
        IEventFactory eventFactory = ObjectFactory.createObject(IEventFactory.class);
        IEvent anticipationEvent = eventFactory.hasAnticipation(character, spot);
        character.getEventsWriter().add(anticipationEvent);
    }
}
