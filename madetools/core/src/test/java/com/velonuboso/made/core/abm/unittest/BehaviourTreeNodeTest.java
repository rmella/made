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
package com.velonuboso.made.core.abm.unittest;

import com.velonuboso.made.core.abm.api.IBehaviourTreeNode;
import com.velonuboso.made.core.abm.api.IBlackBoard;
import com.velonuboso.made.core.abm.api.ICharacter;
import com.velonuboso.made.core.abm.api.IMap;
import com.velonuboso.made.core.abm.implementation.BehaviourTreeNode;
import com.velonuboso.made.core.common.api.IProbabilityHelper;
import com.velonuboso.made.core.common.util.ObjectFactory;
import java.util.function.BiConsumer;
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
 * @author Rubén Héctor García (raiben@gmail.com)
 */
public class BehaviourTreeNodeTest {

    private BehaviourTreeNode node;
    private BiConsumer<IBlackBoard, IBlackBoard> fakeConsumer;
    private ICharacter fakeCharacter;
    private IMap fakeMap;
    private IBehaviourTreeNode fakeNodeFirstChild;
    private IBehaviourTreeNode fakeNodeSecondChild;
    private IProbabilityHelper fakeProbabilityHelper;
    private IBlackBoard fakeBlackBoard;

    private float defaultFakeProbability = 1;

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        fakeConsumer = mock(BiConsumer.class);
        fakeCharacter = mock(ICharacter.class);
        fakeMap = mock(IMap.class);
        fakeNodeFirstChild = mock(IBehaviourTreeNode.class);
        fakeNodeSecondChild = mock(IBehaviourTreeNode.class);
        fakeBlackBoard = mock(IBlackBoard.class);

        defaultFakeProbability = 0;
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
        node.setActionWhenRun(null);
        node.run(fakeBlackBoard, null);
        fail("Should've thrown exception when action is set to null");
    }

    @Test(expected = Exception.class)
    public void UT_BehaviourTreeNode_run_must_throw_exception_when_the_node_character_is_not_set() {
        node.setCharacter(null);
        node.run(fakeBlackBoard, null);
        fail("Should've thrown exception when action is set to null");
    }

    @Test
    public void UT_BehaviourTreeNode_must_execute_consumer_when_run() {
        node.setActionWhenRun(fakeConsumer);
        node.run(fakeBlackBoard, null);
        verify(fakeConsumer).accept(fakeBlackBoard, null);
    }

    @Test
    public void UT_run_must_not_run_children_when_condition_is_false() {
        node.addChildNodeInOrder((x, y) -> false, 1, fakeNodeFirstChild);
        node.run(fakeBlackBoard, null);

        verify(fakeNodeFirstChild, times(0)).run(fakeBlackBoard, null);
    }

    @Test
    public void UT_run_must_not_run_children_when_condition_is_true_and_probability_is_0() {
        node.addChildNodeInOrder((x, y) -> true, 0, fakeNodeFirstChild);
        node.run(fakeBlackBoard, null);

        verify(fakeNodeFirstChild, times(0)).run(fakeBlackBoard, null);
    }

    @Test
    public void UT_run_must_run_children_when_condition_is_true_and_probability_is_1() {
        node.addChildNodeInOrder((x, y) -> true, 1, fakeNodeFirstChild);
        node.run(fakeBlackBoard, null);

        verify(fakeNodeFirstChild).run(fakeBlackBoard, null);
    }

    @Test
    public void UT_run_must_run_second_child_when_first_child_condition_is_false() {
        node.addChildNodeInOrder((x, y) -> false, 1, fakeNodeFirstChild);
        node.addChildNodeInOrder((x, y) -> true, 1, fakeNodeSecondChild);

        node.run(fakeBlackBoard, null);

        verify(fakeNodeFirstChild, times(0)).run(fakeBlackBoard, null);
        verify(fakeNodeSecondChild, times(1)).run(fakeBlackBoard, null);
    }

    private void InitializeNode() {
        node = new BehaviourTreeNode();
        node.setCharacter(fakeCharacter);
    }
}
