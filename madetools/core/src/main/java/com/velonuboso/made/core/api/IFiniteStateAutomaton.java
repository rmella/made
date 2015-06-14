/*
 * Copyright (C) 2015 rhgarcia
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

package com.velonuboso.made.core.api;

import java.util.List;

/**
 *
 * @author Rubén Héctor García (raiben@gmail.com)
 */
public interface IFiniteStateAutomaton {

    public boolean isOk();    

    public void addState(String newState, IAction action);

    public void setInitialState(String initialState);
    
    public void setWorld (IWorld world);
    
    public void setCharacter (ICharacter character);

    public void addTransition(String sourceState, String targetState,
            ICondition conditionToAccomplishToGoToTarget,
            float probabilityToGoToTargetWhenConditionAccomplishes,
            IAction actionBeforeMovingToTargetState);

    public List<String> getTargetStatesAsStrings(String sourceState);

    public List<Transition> getTargetStates(String sourceState);

    public String getCurrentState();
    
    public void run();
    
}
