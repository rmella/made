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
package com.velonuboso.made.core.abm;

import com.velonuboso.made.core.abm.api.IBehaviourTreeNode;
import com.velonuboso.made.core.abm.api.IBlackBoard;
import com.velonuboso.made.core.abm.api.ICharacter;
import com.velonuboso.made.core.abm.api.IMap;
import com.velonuboso.made.core.abm.implementation.BehaviourTreeNode;
import com.velonuboso.made.core.common.api.IProbabilityHelper;
import com.velonuboso.made.core.common.util.ObjectFactory;
import java.util.function.BiPredicate;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 *
 * @author Rubén Héctor García (raiben@gmail.com)
 */
public class BehaviourTreeNodeTest {

    private BehaviourTreeNode node;
    private ICharacter fakeCharacter;
    private IMap fakeMap;

    private IProbabilityHelper fakeProbabilityHelper;
    private IBlackBoard fakeCurrentBlackBoard;
    private IBlackBoard fakeOldBlackBoard;

    private float defaultFakeProbability = 1;

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        fakeCharacter = mock(ICharacter.class);
        fakeMap = mock(IMap.class);

        fakeCurrentBlackBoard = mock(IBlackBoard.class);
        fakeOldBlackBoard = mock(IBlackBoard.class);

        defaultFakeProbability = 0.5f;
        fakeProbabilityHelper = mock(IProbabilityHelper.class);
        stub(fakeProbabilityHelper.getNextProbability(any())).toReturn(defaultFakeProbability);

        ObjectFactory.installMock(IProbabilityHelper.class, fakeProbabilityHelper);
        InitializeNode();
    }

    @After
    public void tearDown() {
        ObjectFactory.removeMock(IProbabilityHelper.class);
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
        node.setAction(null);
        node.run(fakeCurrentBlackBoard, fakeOldBlackBoard);
        fail("Should've thrown exception when action is set to null");
    }

    @Test(expected = Exception.class)
    public void UT_BehaviourTreeNode_run_must_throw_exception_when_the_node_character_is_not_set() {
        node.setCharacter(null);
        node.run(fakeCurrentBlackBoard, fakeOldBlackBoard);
        fail("Should've thrown exception when action is set to null");
    }

    @Test
    public void UT_BehaviourTreeNode_must_execute_predicate_when_run_and_probability_is_1() {
        BiPredicate<IBlackBoard, IBlackBoard> spyOnAction = spy(buildActionThatAlwaysReturnsBoolean(true));
        node.setProbability(1);
        node.setAction(spyOnAction);
        node.run(fakeCurrentBlackBoard, fakeOldBlackBoard);
        verify(spyOnAction).test(fakeCurrentBlackBoard, fakeOldBlackBoard);
    }

    @Test
    public void UT_BehaviourTreeNode_must_return_true_when_predicate_validates_and_probability_is_1() {
        BiPredicate<IBlackBoard, IBlackBoard> spyOnAction = spy(buildActionThatAlwaysReturnsBoolean(true));
        node.setProbability(1);
        node.setAction(spyOnAction);
        boolean success = node.run(fakeCurrentBlackBoard, fakeOldBlackBoard);
        assertTrue("Should've returned success since the provided action always returns true and the probability is 1",
                success);
    }
    
    @Test
    public void UT_BehaviourTreeNode_must_return_false_when_predicate_does_not_validate_and_probability_is_1() {
        BiPredicate<IBlackBoard, IBlackBoard> spyOnAction = spy(buildActionThatAlwaysReturnsBoolean(false));
        node.setProbability(1);
        node.setAction(spyOnAction);
        boolean success = node.run(fakeCurrentBlackBoard, fakeOldBlackBoard);
        assertFalse("Should've returned Not success since the provided action always returns false and the probability is 1",
                success);
    }
    
    @Test
    public void UT_BehaviourTreeNode_must_not_execute_predicate_when_run_and_probability_is_0() {
        BiPredicate<IBlackBoard, IBlackBoard> spyOnAction = spy(buildActionThatAlwaysReturnsBoolean(true));
        node.setProbability(0);
        node.setAction(spyOnAction);
        node.run(fakeCurrentBlackBoard, fakeOldBlackBoard);
        verify(spyOnAction, times(0)).test(fakeCurrentBlackBoard, fakeOldBlackBoard);
    }

    @Test
    public void UT_BehaviourTreeNode_must_return_false_when_predicate_validates_and_probability_is_0() {
        BiPredicate<IBlackBoard, IBlackBoard> spyOnAction = spy(buildActionThatAlwaysReturnsBoolean(true));
        node.setProbability(0);
        node.setAction(spyOnAction);
        boolean success = node.run(fakeCurrentBlackBoard, fakeOldBlackBoard);
        assertFalse("Should've returned NOT success since the provided action always returns true and the probability is 0",
                success);
    }
    
    @Test
    public void UT_BehaviourTreeNode_child_run_when_action_validates_and_node_has_child(){
        BiPredicate<IBlackBoard, IBlackBoard> alwaysTrueAction = buildActionThatAlwaysReturnsBoolean(true);
        node.setProbability(1);
        node.setAction(alwaysTrueAction);
        
        IBehaviourTreeNode child = buildNode(1, true);        
        node.addChildNode(child);
        
        node.run(fakeCurrentBlackBoard, fakeOldBlackBoard);
        verify(child).run(fakeCurrentBlackBoard, fakeOldBlackBoard);
    }
    
    @Test
    public void UT_BehaviourTreeNode_child_not_run_when_action_does_not_validate_and_node_has_child(){
        BiPredicate<IBlackBoard, IBlackBoard> alwaysTrueAction = buildActionThatAlwaysReturnsBoolean(true);
        node.setProbability(0);
        node.setAction(alwaysTrueAction);
        
        IBehaviourTreeNode child = buildNode(1, true);        
        node.addChildNode(child);
        
        node.run(fakeCurrentBlackBoard, fakeOldBlackBoard);
        verify(child, times(0)).run(fakeCurrentBlackBoard, fakeOldBlackBoard);
    }
    
    @Test
    public void UT_BehaviourTreeNode_node_success_when_action_validates_and_child_is_success(){
        BiPredicate<IBlackBoard, IBlackBoard> alwaysTrueAction = buildActionThatAlwaysReturnsBoolean(true);
        node.setProbability(1);
        node.setAction(alwaysTrueAction);
        
        IBehaviourTreeNode child = buildNode(1, true);        
        node.addChildNode(child);
        
        boolean success = node.run(fakeCurrentBlackBoard, fakeOldBlackBoard);
        assertTrue("Should've returned true since the node is run and child successes", success);
    }
    
    @Test
    public void UT_BehaviourTreeNode_node_fails_when_action_validates_and_child_fails(){
        BiPredicate<IBlackBoard, IBlackBoard> alwaysTrueAction = buildActionThatAlwaysReturnsBoolean(true);
        node.setProbability(1);
        node.setAction(alwaysTrueAction);
        
        IBehaviourTreeNode child = buildNode(0, false);        
        node.addChildNode(child);
        
        boolean success = node.run(fakeCurrentBlackBoard, fakeOldBlackBoard);
        assertFalse("Should've returned false since the node is run and child fails", success);
    }

    @Test
    public void UT_BehaviourTreeNode_node_success_when_action_validates_and_second_child_success(){
        BiPredicate<IBlackBoard, IBlackBoard> alwaysTrueAction = buildActionThatAlwaysReturnsBoolean(true);
        node.setProbability(1);
        node.setAction(alwaysTrueAction);
        
        IBehaviourTreeNode firstChild = buildNode(0, false);        
        node.addChildNode(firstChild);
        
        IBehaviourTreeNode secondChild = buildNode(1, true);        
        node.addChildNode(secondChild);
        
        boolean success = node.run(fakeCurrentBlackBoard, fakeOldBlackBoard);
        assertTrue("Should've returned true since the node is run and second child successes", success);
    }

    
    @Test
    public void UT_BehaviourTreeNode_node_fails_when_action_validates_and_all_children_fail(){
        BiPredicate<IBlackBoard, IBlackBoard> alwaysTrueAction = buildActionThatAlwaysReturnsBoolean(true);
        node.setProbability(1);
        node.setAction(alwaysTrueAction);
        
        IBehaviourTreeNode firstChild = buildNode(0, false);        
        node.addChildNode(firstChild);
        
        IBehaviourTreeNode secondChild = buildNode(0, false);        
        node.addChildNode(secondChild);
        
        boolean success = node.run(fakeCurrentBlackBoard, fakeOldBlackBoard);
        assertFalse("Should've returned false since the node is run and all the children fail", success);
    }
    
    private void InitializeNode() {
        node = new BehaviourTreeNode();
        node.setCharacter(fakeCharacter);
    }

    private BiPredicate<IBlackBoard, IBlackBoard> buildActionThatAlwaysReturnsBoolean(boolean value) {
        return new BiPredicate<IBlackBoard, IBlackBoard>() {
            @Override
            public boolean test(IBlackBoard t, IBlackBoard u) {
                return value;
            }
        };
    }
    
    private IBehaviourTreeNode buildNode(float probability, boolean allwaysReturn) {
        BehaviourTreeNode node = spy(new BehaviourTreeNode());
        node.setCharacter(fakeCharacter);
        node.setProbability(probability);
        node.setAction(buildActionThatAlwaysReturnsBoolean(allwaysReturn));
        return node;
    }
}
