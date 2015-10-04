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
package com.velonuboso.made.core.abm.implementation.piece.strategy;

import com.velonuboso.made.core.abm.api.IBlackBoard;
import com.velonuboso.made.core.abm.api.ICharacter;
import com.velonuboso.made.core.abm.api.strategy.IStrategyGiveTurn;
import com.velonuboso.made.core.abm.api.strategy.IStrategyTransferColor;
import com.velonuboso.made.core.abm.entity.ActionReturnException;
import com.velonuboso.made.core.abm.entity.CharacterShape;
import com.velonuboso.made.core.abm.implementation.piece.BaseAction;
import com.velonuboso.made.core.abm.implementation.piece.Piece;
import com.velonuboso.made.core.common.api.IEvent;
import com.velonuboso.made.core.common.api.IEventFactory;
import com.velonuboso.made.core.common.util.ObjectFactory;
import java.util.List;
import java.util.Set;
import javafx.scene.paint.Color;

/**
 *
 * @author Rubén Héctor García (raiben@gmail.com)
 */
public class StrategyGiveTurn extends BaseAction implements IStrategyGiveTurn {

    @Override
    public boolean testAction(IBlackBoard currentBlackboard, IBlackBoard oldBlackBoard) throws ActionReturnException {
        int characterCell = getMap().getCell(getCharacter());
        int friendCell = currentBlackboard.getInt(Piece.BLACKBOARD_TARGET_CELL);
        
        ICharacter friend = getMap().getCharacter(friendCell);
        if (friend==null){
            return false;
        }
        giveTurn(friend, currentBlackboard);
        
        writeEvent(friend);
        return true;
    }

    private void giveTurn(ICharacter friend, IBlackBoard currentBlackBoard) {
        getMap().addExtraTurnTo(friend);
    }
    
    private void writeEvent(ICharacter friend) {
        IEventFactory factory = ObjectFactory.createObject(IEventFactory.class);
        IEvent event = factory.givesTurn(getCharacter(), friend);
        getCharacter().getEventsWriter().add(event);
    }
}
