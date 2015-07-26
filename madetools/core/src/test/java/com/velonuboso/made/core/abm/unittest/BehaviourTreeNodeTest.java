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
package com.velonuboso.made.core.abm.unittest;

import com.velonuboso.made.core.abm.api.IBehaviourTree;
import com.velonuboso.made.core.abm.api.IBehaviourTreeNode;
import com.velonuboso.made.core.abm.api.ICharacter;
import com.velonuboso.made.core.abm.api.IMap;
import com.velonuboso.made.core.abm.entity.BehaviourTreeNodeStatus;
import com.velonuboso.made.core.abm.implementation.BehaviourTreeNode;
import com.velonuboso.made.core.abm.implementation.piece.PieceBehaviourTree;
import com.velonuboso.made.core.common.util.ObjectFactory;
import edu.umd.cs.findbugs.annotations.ExpectWarning;
import java.util.function.Consumer;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 *
 * @author rhgarcia
 */
public class BehaviourTreeNodeTest {

    private BehaviourTreeNode node;
    private Consumer<IBehaviourTreeNode> fakeConsumer;
    private ICharacter fakeCharacter;
    private IMap fakeMap;
    
    
    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        fakeConsumer = mock(Consumer.class);
        fakeCharacter = mock(ICharacter.class);
        fakeMap = mock(IMap.class);
        
        node = new BehaviourTreeNode();
        node.setCharacter(fakeCharacter);
        node.setMap(fakeMap);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void UT_BehaviourTreeNode_must_be_created_correctly_with_ObjectFactory() {
        Class EXPECTED_CLASS = BehaviourTreeNode.class;

        IBehaviourTreeNode behaviourTreeNode = ObjectFactory.createObject(IBehaviourTreeNode.class);
        assertEquals("ObjectFactory should've created a " + EXPECTED_CLASS.getSimpleName()
                + " as default implementation", EXPECTED_CLASS, behaviourTreeNode.getClass());
    }
    
    @Test(expected = Exception.class)
    public void UT_BehaviourTreeNode_run_must_throw_exception_when_the_node_action_is_set_to_null() {
        node.setActionWhenRun(null);
        node.run();
        fail("Should've thrown exception when action is set to null");
    }
    
    @Test(expected = Exception.class)
    public void UT_BehaviourTreeNode_run_must_throw_exception_when_the_node_map_is_not_set() {
        node.setMap(null);
        node.run();
        fail("Should've thrown exception when action is set to null");
    }
    
    @Test(expected = Exception.class)
    public void UT_BehaviourTreeNode_run_must_throw_exception_when_the_node_character_is_not_set() {
        node.setCharacter(null);
        node.run(fakeCharacter);
        fail("Should've thrown exception when action is set to null");
    }
    
    @Test
    public void UT_BehaviourTreeNode_must_execute_consumer_when_run() {
        node.setActionWhenRun(fakeConsumer);
        node.run();
        try{
            verify(fakeConsumer).accept(node);
        }catch(Exception e){
            fail("Should've called the action set for the tree node");
        }
    }
    
    public void UT_BehaviourTreeNode_run_must_run_children_when_condition_when_the_node_character_is_not_set() {
        //TODO
    }
}
