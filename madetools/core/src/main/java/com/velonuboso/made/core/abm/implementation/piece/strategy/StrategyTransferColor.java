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
import com.velonuboso.made.core.abm.api.strategy.IStrategyTransferColor;
import com.velonuboso.made.core.abm.entity.ActionReturnException;
import com.velonuboso.made.core.abm.entity.CharacterShape;
import com.velonuboso.made.core.abm.implementation.piece.BaseAction;
import com.velonuboso.made.core.abm.implementation.piece.Piece;
import com.velonuboso.made.core.common.api.IEvent;
import com.velonuboso.made.core.common.api.IEventFactory;
import com.velonuboso.made.core.common.util.ObjectFactory;
import java.util.List;
import javafx.scene.paint.Color;

/**
 *
 * @author Rubén Héctor García (raiben@gmail.com)
 */
public class StrategyTransferColor extends BaseAction implements IStrategyTransferColor {

    @Override
    public boolean testAction(IBlackBoard currentBlackboard, IBlackBoard oldBlackBoard) throws ActionReturnException {
        int characterCell = getMap().getCell(getCharacter());
        List<Integer> cells = getMap().getCellsAround(characterCell, 1);
        
        int friendCell = currentBlackboard.getInt(Piece.BLACKBOARD_TARGET_CELL);
        if (!cells.contains(friendCell)){
            return false;
        }
        
        ICharacter friend = getMap().getCharacter(friendCell);
        if (friend==null){
            return false;
        }
        
        if (friend.getShape().wins(getCharacter().getShape())){
            return false;
        }
        
        exchangeColors(getCharacter(), friend);
        writeEvent();
        return true;
    }

    private void exchangeColors(ICharacter character, ICharacter friend) {
        Color auxiliaryColor = character.getBackgroundColor();
        character.setBackgroundColor(friend.getBackgroundColor());
        friend.setBackgroundColor(auxiliaryColor);
    }
    
    private void writeEvent() {
        IEventFactory factory = ObjectFactory.createObject(IEventFactory.class);
        IEvent event = factory.transfersColor(getCharacter());
        getCharacter().getEventsWriter().add(event);
    }
}
