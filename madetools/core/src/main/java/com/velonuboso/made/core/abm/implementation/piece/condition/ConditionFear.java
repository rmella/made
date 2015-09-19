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
import com.velonuboso.made.core.abm.api.condition.IConditionFear;
import com.velonuboso.made.core.abm.implementation.piece.Piece;
import com.velonuboso.made.core.common.api.IEvent;
import com.velonuboso.made.core.common.api.IEventFactory;
import com.velonuboso.made.core.common.util.ObjectFactory;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Rubén Héctor García (raiben@gmail.com)
 */
public class ConditionFear extends BaseAction implements IConditionFear {
    
    @Override
    public boolean test(IBlackBoard currentBlackBoard, IBlackBoard oldBlackBoard) {
        ICharacter enemy = getAdjacentEnemy(currentBlackBoard);
        if (enemy != null){
            storeEnemyCellIntoBlackboard(enemy, currentBlackBoard);
            writeEvent(enemy);
        }
        return enemy != null;
    }

    private ICharacter getAdjacentEnemy(IBlackBoard blackBoard) {
        int currentCharacterPosition = getMap().getCell(getCharacter());
        List<Integer> cellsToLookAt = getMap().getCellsAround(currentCharacterPosition, 1);
        HashMap<ICharacter, Float> affinityMatrix
                = (HashMap<ICharacter, Float>) blackBoard.getObject(Piece.BLACKBOARD_AFFINITY_MATRIX);

        ICharacter enemy = cellsToLookAt.stream()
                .map(cell -> getMap().getCharacter(cell))
                .filter(piece -> piece != null && piece != getCharacter() && piece.getShape().wins(getCharacter().getShape()))
                .min((ICharacter firstCharacter, ICharacter secondCharacter) -> {
                    return Float.compare(affinityMatrix.get(firstCharacter),
                            affinityMatrix.get(secondCharacter));
                })
                .orElse(null);

        return enemy;
    }

    private void storeEnemyCellIntoBlackboard(ICharacter enemy, IBlackBoard blackBoard) {
        blackBoard.setInt(Piece.BLACKBOARD_CHARACTER_CELL, getMap().getCell(enemy));
    }
    
    private void writeEvent(ICharacter enemy) {
        IEventFactory eventFactory = ObjectFactory.createObject(IEventFactory.class);
        IEvent fearEvent = eventFactory.hasFear(getCharacter(), enemy);
        getCharacter().getEventsWriter().add(fearEvent);
    }
}
