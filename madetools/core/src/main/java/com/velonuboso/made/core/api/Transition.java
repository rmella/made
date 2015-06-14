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

import com.velonuboso.made.core.api.ICondition;

/**
 *
 * @author Rubén Héctor García (raiben@gmail.com)
 */
public class Transition implements Comparable<Transition>{
    private String sourceState;
    private String targetState;
    private ICondition condition;
    private float probability;
    private IAction action;

    public Transition(String sourceState, String targetState, ICondition condition,
            float probability, IAction actionBeforeMovingToTargetState) {
        this.sourceState = sourceState;
        this.targetState = targetState;
        this.condition = condition;
        this.probability = probability;
        this.action = actionBeforeMovingToTargetState;
    }

    public ICondition getConditionToGoToTarget() {
        return condition;
    }

    public float getProbabilityToGoToTargetStateWhenConditionAccomplishes() {
        return probability;
    }

    public String getSourceState() {
        return sourceState;
    }

    public String getTargetState() {
        return targetState;
    }

    public IAction getActionBeforeMovingToTargetState() {
        return action;
    }
    
    
    @Override
    public int compareTo(Transition o) {
        if (this.probability > o.probability) return -1;
        if (this.probability < o.probability) return 1;
        return 0;
    }
}
