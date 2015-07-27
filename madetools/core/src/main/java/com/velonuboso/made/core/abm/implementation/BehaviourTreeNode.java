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
package com.velonuboso.made.core.abm.implementation;

import com.velonuboso.made.core.abm.api.IBehaviourTreeNode;
import com.velonuboso.made.core.abm.api.ICharacter;
import com.velonuboso.made.core.abm.api.IMap;
import com.velonuboso.made.core.common.api.IProbabilityHelper;
import com.velonuboso.made.core.common.util.ObjectFactory;
import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 *
 * @author Rubén Héctor García (raiben@gmail.com)
 */
public class BehaviourTreeNode implements IBehaviourTreeNode {

    private ICharacter character;
    private IMap map;
    private Consumer<IBehaviourTreeNode> action;
    private ArrayList<ChildCondition> childrenConditions;
    private IProbabilityHelper probabilityHelper;

    public BehaviourTreeNode() {
        action = new NullAction();
        childrenConditions = new ArrayList<>();
        map = null;
        character = null;
        probabilityHelper = ObjectFactory.createObject(IProbabilityHelper.class);
    }

    @Override
    public void setMap(IMap map) {
        this.map = map;
    }

    @Override
    public void setCharacter(ICharacter character) {
        this.character = character;
    }

    @Override
    public void setActionWhenRun(Consumer<IBehaviourTreeNode> action) {
        this.action = action;
    }

    @Override
    public void addChildNodeInOrder(Predicate<IBehaviourTreeNode> conditionToRunChildren, 
            float probabilityToRunChildren, IBehaviourTreeNode nodeToRun) {
        
        ChildCondition child = new ChildCondition();
        child.conditionToRunChildren = conditionToRunChildren;
        child.nodeToRun = nodeToRun;
        child.probabilityToRunChildren = probabilityToRunChildren;
        
        childrenConditions.add(child);
    }

    @Override
    public boolean run() {
        checkCorrectInitialization();
        action.accept(this);
        
        boolean success = false;
        int childIndex = 0;
        
        while (!success && childIndex<childrenConditions.size()){
            ChildCondition child = childrenConditions.get(childIndex);
            
            if (isInProbability(child) && conditionValidates(child)){
                 success |= child.nodeToRun.run();
            }
            childIndex++;
        }
        
        return success;
    }

    private boolean conditionValidates(ChildCondition child) {
        return child.conditionToRunChildren.test(this);
    }

    private boolean isInProbability(ChildCondition child) {
        return probabilityHelper.getNextProbability(this.getClass())<child.probabilityToRunChildren;
    }

    private void checkCorrectInitialization() throws RuntimeException {
        checkActionInitialized();
        checkCharacterInitialized();
        checkMapInitialized();
    }

    private void checkMapInitialized() throws RuntimeException {
        if (map == null) {
            throw new RuntimeException("node map is null");
        }
    }

    private void checkCharacterInitialized() throws RuntimeException {
        if (character == null) {
            throw new RuntimeException("node parent is null");
        }
    }

    private void checkActionInitialized() throws RuntimeException {
        if (action == null) {
            throw new RuntimeException("node action is null");
        }
    }

    private class ChildCondition {

        Predicate<IBehaviourTreeNode> conditionToRunChildren = null;
        float probabilityToRunChildren = 0f;
        IBehaviourTreeNode nodeToRun = null;
    }

    private class NullAction implements Consumer<IBehaviourTreeNode> {
        @Override
        public void accept(IBehaviourTreeNode t) {
        }
    };
}
