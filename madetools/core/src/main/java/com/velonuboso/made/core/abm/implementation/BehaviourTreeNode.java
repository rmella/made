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

import com.velonuboso.made.core.abm.api.IAction;
import com.velonuboso.made.core.abm.api.IBehaviourTreeNode;
import com.velonuboso.made.core.abm.api.IBlackBoard;
import com.velonuboso.made.core.abm.api.ICharacter;
import com.velonuboso.made.core.abm.api.IMap;
import com.velonuboso.made.core.common.api.IProbabilityHelper;
import com.velonuboso.made.core.common.util.ObjectFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 *
 * @author Rubén Héctor García (raiben@gmail.com)
 */
public class BehaviourTreeNode implements IBehaviourTreeNode {

    private ICharacter character;
    private BiPredicate<IBlackBoard, IBlackBoard> action;
    private float probability;
    private ArrayList<IBehaviourTreeNode> children;
    
    private IProbabilityHelper probabilityHelper;

    public BehaviourTreeNode() {
        action = new NullAction();
        probability = 1f;
        children = new ArrayList<>();
        character = null;
        probabilityHelper = ObjectFactory.createObject(IProbabilityHelper.class);
    }

    @Override
    public void setCharacter(ICharacter character) {
        this.character = character;
    }

    @Override
    public void setAction(BiPredicate<IBlackBoard, IBlackBoard> action) {
        this.action = action;
    }

    @Override
    public BiPredicate<IBlackBoard, IBlackBoard> getAction() {
        return action;
    }
    
    @Override
    public void setProbability(float probability) {
        this.probability = probability;
    }

    @Override
    public void addChildNode(IBehaviourTreeNode child) {
        children.add(child);
    }
    
    @Override
    public boolean run(IBlackBoard currentBlackBoard, IBlackBoard oldBlackBoard) {
        checkCorrectInitialization();
        
        if (!isInProbability() || !action.test(currentBlackBoard, oldBlackBoard)){
            return false;
        }else{
            if (children.isEmpty()){
                return true;
            }else{
                IBehaviourTreeNode executedSuccessfully =  children.stream()
                        .filter(node -> node.run(currentBlackBoard, oldBlackBoard))
                        .findFirst()
                        .orElse(null);
                return executedSuccessfully != null;
            }
        }
    }

    public List<IBehaviourTreeNode> getChildren() {
        return children;
    }
    
    private boolean isInProbability() {
        return probabilityHelper.getNextProbability(this.getClass())<probability;
    }

    private void checkCorrectInitialization() throws RuntimeException {
        checkActionInitialized();
        checkCharacterInitialized();
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

    private class NullAction implements BiPredicate<IBlackBoard,IBlackBoard> {
        @Override
        public boolean test(IBlackBoard currentBlackboard, IBlackBoard oldBlackBoard) {
            return true;
        }
    };
}
