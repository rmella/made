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
import com.velonuboso.made.core.abm.api.condition.IConditionCanReduceEnemySimilarity;
import com.velonuboso.made.core.abm.entity.ActionReturnException;
import com.velonuboso.made.core.abm.implementation.piece.Piece;
import com.velonuboso.made.core.common.api.IEvent;
import com.velonuboso.made.core.common.api.IEventFactory;
import com.velonuboso.made.core.common.util.ObjectFactory;
import java.util.HashMap;

/**
 *
 * @author Rubén Héctor García (raiben@gmail.com)
 */
public class ConditionCanReduceEnemySimilarity extends BaseAction implements IConditionCanReduceEnemySimilarity{

    
    @Override
    public boolean testAction(IBlackBoard currentBlackBoard, IBlackBoard oldBlackBoard)
            throws ActionReturnException {
        ICharacter candidateToPush = getBestCandidateToPush(currentBlackBoard);

        if (candidateToPush != null) {
            storeCandidateToPushIntoBlackboard(candidateToPush, currentBlackBoard);
            writeEvent(candidateToPush);
        }
        return candidateToPush != null;
    }

    private ICharacter getBestCandidateToPush(IBlackBoard blackboard) {
        HashMap<ICharacter, Float> affinityMatrix
                = (HashMap<ICharacter, Float>) blackboard.getObject(Piece.BLACKBOARD_AFFINITY_MATRIX);
        
        ICharacter candidateToPush = affinityMatrix.keySet().stream()
                .filter(targetCharacter -> getCharacter().getShape().wins(targetCharacter.getShape()))
                .filter(targetCharacter -> isEnemy(affinityMatrix, targetCharacter))
                .filter(enemy -> this.getCharacter().getShape().wins(enemy.getShape()))
                .min((ICharacter firstEnemy, ICharacter secondEnemy) -> {
                    return Float.compare(firstEnemy.getColorDifference(), secondEnemy.getColorDifference());
                })
                .orElse(null);
        return candidateToPush;
    }
    
    private boolean isEnemy(HashMap<ICharacter, Float> affinityMatrix, ICharacter targetCharacter) {
        return affinityMatrix.get(targetCharacter)<0;
    }

    private void storeCandidateToPushIntoBlackboard(ICharacter candidate, IBlackBoard blackBoard) {
        blackBoard.setInt(Piece.BLACKBOARD_TARGET_CELL, getMap().getCell(candidate));
    }

    private void writeEvent(ICharacter targetCharacter) {
        IEventFactory eventFactory = ObjectFactory.createObject(IEventFactory.class);
        IEvent canReduceEnemySimilarityevent = eventFactory.canReduceEnemySimilarity(getCharacter(), targetCharacter);
        getCharacter().getEventsWriter().add(canReduceEnemySimilarityevent);
    }
}
