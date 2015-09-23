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
import com.velonuboso.made.core.abm.api.IColorSpot;
import com.velonuboso.made.core.abm.api.strategy.IStrategyStain;
import com.velonuboso.made.core.abm.entity.ActionReturnException;
import com.velonuboso.made.core.abm.implementation.piece.BaseAction;
import com.velonuboso.made.core.abm.implementation.piece.AbmConfigurationHelperPiece;
import com.velonuboso.made.core.common.api.IEvent;
import com.velonuboso.made.core.common.api.IEventFactory;
import com.velonuboso.made.core.common.api.IProbabilityHelper;
import com.velonuboso.made.core.common.util.ObjectFactory;

/**
 *
 * @author Rubén Héctor García (raiben@gmail.com)
 */
public class StrategyStain extends BaseAction implements IStrategyStain {

    @Override
    public boolean testAction(IBlackBoard currentBlackboard, IBlackBoard oldBlackBoard) throws ActionReturnException {
        int characterCell = getMap().getCell(getCharacter());
        IColorSpot spot = getMap().getColorSpot(characterCell);
        
        if (spot == null){
            return false;
        }
        
        getCharacter().setBackgroundColor(spot.getColor());
        
        
        writeEvent();
        
        return true;
    }

    private void writeEvent() {
        IEventFactory factory = ObjectFactory.createObject(IEventFactory.class);
        IEvent event = factory.stains(getCharacter());
        getCharacter().getEventsWriter().add(event);
    }
}
