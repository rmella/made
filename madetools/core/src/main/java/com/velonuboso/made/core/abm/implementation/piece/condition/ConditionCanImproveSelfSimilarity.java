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

import com.velonuboso.made.core.abm.implementation.piece.BaseAction;
import com.velonuboso.made.core.abm.api.IBlackBoard;
import com.velonuboso.made.core.abm.api.ICharacter;
import com.velonuboso.made.core.abm.api.IColorSpot;
import com.velonuboso.made.core.abm.api.condition.IConditionCanImproveSelfSimilarity;
import com.velonuboso.made.core.abm.entity.ActionReturnException;
import com.velonuboso.made.core.abm.implementation.piece.Piece;
import com.velonuboso.made.core.abm.implementation.piece.PieceUtilities;
import com.velonuboso.made.core.common.api.IEvent;
import com.velonuboso.made.core.common.api.IEventFactory;
import com.velonuboso.made.core.common.util.ObjectFactory;
import java.util.function.Function;

/**
 *
 * @author Rubén Héctor García (raiben@gmail.com)
 */
public class ConditionCanImproveSelfSimilarity extends BaseAction implements IConditionCanImproveSelfSimilarity {

    @Override
    public boolean testAction(IBlackBoard currentBlackBoard, IBlackBoard oldBlackBoard)
            throws ActionReturnException {

        IColorSpot spot = getBestColorSpot(currentBlackBoard);
        if (spot != null) {
            storeSpotCellIntoBlackboard(spot, currentBlackBoard);
            writeEvent(spot);
        }
        return spot != null;
    }

    private IColorSpot getBestColorSpot(IBlackBoard blackboard) {

        Function<Integer, IColorSpot> cellToSpot = new Function<Integer, IColorSpot>() {
            @Override
            public IColorSpot apply(Integer cell) {
                return getMap().getColorSpot(cell);
            }
        };

        return getMap().getCells()
                .stream()
                .filter(cell -> isSpotInCell(cell) && !isWinnerCharacterInCell(cell))
                .map(cellToSpot)
                .filter(spot -> {
                    return similarityWithSpot(spot) > 1-getCharacter().getColorDifference();
                })
                .max((IColorSpot firstSpot, IColorSpot secondSpot) -> {
                    return Float.compare(similarityWithSpot(firstSpot), similarityWithSpot(secondSpot));
                })
                .orElse(null);
    }

    private float similarityWithSpot(IColorSpot spot) {
        return 1-PieceUtilities.calculateColorDifference(spot.getColor(), getCharacter().getForegroundColor());
    }

    private boolean isWinnerCharacterInCell(Integer cell) {
        ICharacter characterInSpot = getMap().getCharacter(cell);
        return characterInSpot != null && characterInSpot.getShape().wins(getCharacter().getShape());
    }

    private boolean isSpotInCell(Integer cell) {
        return getMap().getColorSpot(cell) != null;
    }

    private void writeEvent(IColorSpot spot) {
        IEventFactory eventFactory = ObjectFactory.createObject(IEventFactory.class);
        IEvent anticipationEvent = eventFactory.canImproveSelfSimilarity(getCharacter(), spot);
        getCharacter().getEventsWriter().add(anticipationEvent);
    }

    private void storeSpotCellIntoBlackboard(IColorSpot spot, IBlackBoard blackBoard) {
        blackBoard.setInt(Piece.BLACKBOARD_TARGET_CELL, getMap().getCell(spot));
    }
}
