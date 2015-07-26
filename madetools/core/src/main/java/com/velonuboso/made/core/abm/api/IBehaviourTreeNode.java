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
package com.velonuboso.made.core.abm.api;

import com.velonuboso.made.core.abm.entity.BehaviourTreeNodeStatus;
import com.velonuboso.made.core.abm.implementation.BehaviourTreeNode;
import com.velonuboso.made.core.common.util.ImplementedBy;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 *
 * @author Rubén Héctor García (raiben@gmail.com)
 */
@ImplementedBy(targetClass = BehaviourTreeNode.class)
public interface IBehaviourTreeNode {
    void setMap (IMap map);
    void setCharacter (ICharacter character);
    void setActionWhenRun(Consumer<IBehaviourTreeNode> action);
    void addChildrenNodeInOrder(Predicate<IBehaviourTreeNode> conditionToRunChildren, 
            float probabilityToRunChildren, IBehaviourTree nodeToRun);
    BehaviourTreeNodeStatus run(Object ... args);
}
