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
package com.velonuboso.made.core.implementation;

import com.velonuboso.made.core.api.Transition;
import com.velonuboso.made.core.api.IAction;
import com.velonuboso.made.core.api.ICharacter;
import com.velonuboso.made.core.api.ICondition;
import com.velonuboso.made.core.api.IFiniteStateAutomaton;
import com.velonuboso.made.core.api.IWorld;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author Rubén Héctor García (raiben@gmail.com)
 */
public class FiniteStateAutomaton implements IFiniteStateAutomaton {

    private HashMap<String, IAction> states = new HashMap<>();
    private String initialState = null;
    private String currentState = null;
    private ArrayList<Transition> links = new ArrayList<>();
    private IWorld world = null;
    private ICharacter character = null;
    
    public FiniteStateAutomaton() {
    }

    @Override
    public boolean isOk() {
        return !states.isEmpty() && currentState != null &&
                states.containsKey(currentState) && world != null &&
                currentState != null && character != null;
    }

    @Override
    public void addState(String state, IAction action) {
        states.put(state, action);
    }
    
    @Override
    public void setInitialState(String initialState) {
        this.initialState = initialState;
        currentState = initialState;
    }
    
    @Override
    public void setCharacter(ICharacter character) {
        this.character = character;
    }

    @Override
    public void addTransition(String sourceState, String targetState,
            ICondition conditionToAccomplishToGoToTarget,
            float probabilityToGoToTargetWhenConditionAccomplishes,
            IAction actionBeforeMovingToTargetState) {

        Transition newLink = new Transition(sourceState, targetState,
                conditionToAccomplishToGoToTarget,
                probabilityToGoToTargetWhenConditionAccomplishes,
                actionBeforeMovingToTargetState);
        links.add(newLink);
    }

    @Override
    public List<Transition> getTargetStates(String sourceState) {
        return links.stream().filter(
                link -> link.getSourceState().equals(sourceState)
        ).sorted().collect(Collectors.toList());
    }
    
    @Override
    public List<String> getTargetStatesAsStrings(String sourceState) {
        ArrayList<String> linkedStates = new ArrayList<>();
        getTargetStates(sourceState).stream().forEach(
                link -> linkedStates.add(link.getTargetState())
        );
        return linkedStates;
    }

    @Override
    public void setWorld(IWorld world) {
        this.world = world;
    }

    @Override
    public String getCurrentState() {
        return currentState;
    }

    
    @Override
    public void run() {
        if (isOk()){
            states.get(currentState).run(character, world, currentState);
            Iterator<Transition> transitionIterator = getTargetStates(currentState).iterator();
            boolean moved = false;
            
            while (!moved && transitionIterator.hasNext()){
                Transition transition = transitionIterator.next();
                
                if (transition.getConditionToGoToTarget().check(character, world, currentState) && 
                    RandomNumberFactory.getNextProbability()<transition.getProbabilityToGoToTargetStateWhenConditionAccomplishes()){
                    
                    currentState = transition.getTargetState();
                    transition.getActionBeforeMovingToTargetState().run(character, world, currentState);
                    moved = true;
                }
            }
        }
    }

    
}
