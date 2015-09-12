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
import com.velonuboso.made.core.abm.api.condition.IConditionSadness;
import com.velonuboso.made.core.abm.implementation.BlackBoard;
import com.velonuboso.made.core.abm.implementation.piece.Piece;
import com.velonuboso.made.core.abm.implementation.piece.PieceAbmConfigurationHelper;
import com.velonuboso.made.core.abm.implementation.piece.PieceUtilities;
import com.velonuboso.made.core.common.api.IEvent;
import com.velonuboso.made.core.common.api.IEventFactory;
import com.velonuboso.made.core.common.util.ObjectFactory;
import java.util.function.Function;

/**
 *
 * @author Rubén Héctor García (raiben@gmail.com)
 */
public class ConditionSadness extends BaseCondition implements IConditionSadness{

    @Override
    public boolean test(IBlackBoard currentBlackBoard, IBlackBoard oldBlackBoard) {
        if (!isSad(currentBlackBoard)){
            return false;
        }
        writeEvent();
        return true;
    }

    public boolean isSad(IBlackBoard blackBoard){
        PieceAbmConfigurationHelper abmConfigurationHelper = 
                new PieceAbmConfigurationHelper(character.getAbmConfiguration());
        return blackBoard.getFloat(Piece.BLACKBOARD_JOY) < abmConfigurationHelper.getJoyThreshold();
    }
    
    private void writeEvent() {
        IEventFactory eventFactory = ObjectFactory.createObject(IEventFactory.class);
        IEvent sadnessEvent = eventFactory.isSad(character);
        character.getEventsWriter().add(sadnessEvent);
    }
}
