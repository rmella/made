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

package com.velonuboso.made.core.abm.implementation.piece;

import com.velonuboso.made.core.abm.entity.ActionReturnException;
import com.velonuboso.made.core.abm.api.IBlackBoard;
import com.velonuboso.made.core.abm.api.ICharacter;
import com.velonuboso.made.core.abm.api.IMap;
import com.velonuboso.made.core.abm.api.condition.ICondition;

/**
 *
 * @author Rubén Héctor García (raiben@gmail.com)
 */
public abstract class BaseAction implements ICondition{

    ICharacter character;
    
    @Override
    public void setCharacter(ICharacter character) {
        this.character = character;
    }

    @Override
    public ICharacter getCharacter() {
        return character;
    }
    
    public IMap getMap(){
        return character.getMap();
    }
    
    @Override
    public boolean test(IBlackBoard currentBlackboard, IBlackBoard oldBlackBoard){
        try{
            return testAction(currentBlackboard, oldBlackBoard);
        }catch(ActionReturnException exception){
            return exception.getResult();
        }
    }

    public abstract boolean testAction(IBlackBoard currentBlackboard, IBlackBoard oldBlackBoard)
            throws ActionReturnException;

}
