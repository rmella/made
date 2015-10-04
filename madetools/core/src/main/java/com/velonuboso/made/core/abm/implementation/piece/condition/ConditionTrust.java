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
import com.velonuboso.made.core.abm.api.condition.IConditionTrust;
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
public class ConditionTrust extends BaseAction implements IConditionTrust {

    @Override
    public boolean testAction(IBlackBoard currentBlackBoard, IBlackBoard oldBlackBoard)
            throws ActionReturnException {

        HashMap<ICharacter, Float> affinityMatrix
                = (HashMap<ICharacter, Float>) currentBlackBoard.getObject(Piece.BLACKBOARD_AFFINITY_MATRIX);
        
        ICharacter friendWithMostAffinity = getFriendWithMostAffinity(affinityMatrix);
        
        if (friendWithMostAffinity==null){
            return false;
        }
        
        storeTargetFriendsCellIntoBlackBoard(currentBlackBoard, friendWithMostAffinity);
        
        writeEvent(friendWithMostAffinity);
        return true;
    }

    private void storeTargetFriendsCellIntoBlackBoard(IBlackBoard currentBlackBoard, ICharacter friendWithMostAffinity) {
        currentBlackBoard.setInt(Piece.BLACKBOARD_TARGET_CELL, getMap().getCell(friendWithMostAffinity));
    }

    private ICharacter getFriendWithMostAffinity(HashMap<ICharacter, Float> affinityMatrix) {
        ICharacter friendWithMostAffinity = affinityMatrix.keySet().stream()
                .max((ICharacter firstCharacter, ICharacter secondCharacter) -> {
                    float affinityWithFirstCharacter = affinityMatrix.get(firstCharacter);
                    float affinityWithSecondCharacter = affinityMatrix.get(secondCharacter);
                    return Float.compare(affinityWithFirstCharacter, affinityWithSecondCharacter);
                })
                .orElse(null);
        return friendWithMostAffinity;
    }

    private void writeEvent(ICharacter friendWithMostAffinity) {
        IEventFactory eventFactory = ObjectFactory.createObject(IEventFactory.class);
        IEvent anticipationEvent = eventFactory.trusts(getCharacter(), friendWithMostAffinity);
        getCharacter().getEventsWriter().add(anticipationEvent);
    }
}
