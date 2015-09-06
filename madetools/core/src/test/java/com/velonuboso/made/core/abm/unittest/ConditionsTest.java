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

import com.sun.org.apache.bcel.internal.generic.AALOAD;
import com.sun.org.apache.xpath.internal.Arg;
import com.velonuboso.made.core.abm.api.IBlackBoard;
import com.velonuboso.made.core.abm.api.IEventsWriter;
import com.velonuboso.made.core.abm.api.IMap;
import com.velonuboso.made.core.abm.api.condition.IConditionFear;
import com.velonuboso.made.core.abm.entity.CharacterShape;
import com.velonuboso.made.core.abm.implementation.BehaviourTreeNode;
import com.velonuboso.made.core.abm.implementation.BlackBoard;
import com.velonuboso.made.core.abm.implementation.piece.Piece;
import com.velonuboso.made.core.abm.implementation.piece.condition.ConditionFear;
import com.velonuboso.made.core.common.api.IEvent;
import com.velonuboso.made.core.common.entity.AbmConfigurationEntity;
import com.velonuboso.made.core.common.implementation.EventFactory;
import com.velonuboso.made.core.common.util.ObjectFactory;
import javafx.scene.paint.Color;
import org.hamcrest.BaseMatcher;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.ArgumentMatcher;
import static org.mockito.Mockito.*;


/**
 *
 * @author Rubén Héctor García (raiben@gmail.com)
 */
public class ConditionsTest {
    
    private AbmConfigurationEntity abmConfigurationEntity;
    private IEventsWriter fakeEventsWriter;
    private IMap map;
    private BehaviourTreeNode defaultActionNode;
    private boolean conditionSatisfied;
    
    @Before
    public void setUp() {
        abmConfigurationEntity = new AbmConfigurationEntity(new float[]{0,0,0,0,0,0,0,0,0,0,0,0});
        fakeEventsWriter = mock(IEventsWriter.class);
        map = ObjectFactory.createObject(IMap.class);
        map.initialize(10, 10);
        defaultActionNode = new BehaviourTreeNode();
        conditionSatisfied = false;
        defaultActionNode.setActionWhenRun(blackboard -> {
                conditionSatisfied = true;
            }
        );
        ObjectFactory.cleanAllMocks();
    }
    
    @Test
    public void UT_ConditionFear__when_piece_has_no_adjacent_pieces_the_piece_has_no_fear() {
        Piece mainPiece = buildPiece(0, CharacterShape.CIRCLE, Color.WHITE, Color.BLACK, 0, 0);
        buildPiece(1, CharacterShape.CIRCLE, Color.GREEN, Color.BLACK, 5, 5);
        
        SetBehaviourConditionFearAndRun(mainPiece);
        
        assertFalse("Shouldn't have called the defaultActionNode since the adjacent circular piece cannot move it", conditionSatisfied);
    }

    @Test
    public void UT_ConditionFear__when_piece_has_adjacent_pieces_that_cannot_win_it_has_no_fear() {
        Piece mainPiece = buildPiece(0, CharacterShape.CIRCLE, Color.WHITE, Color.BLACK, 0, 0);
        buildPiece(1, CharacterShape.CIRCLE, Color.GREEN, Color.BLACK, 1, 0);
        
        SetBehaviourConditionFearAndRun(mainPiece);
        
        assertFalse("Shouldn't have called the defaultActionNode since the adjacent circular piece cannot move it", conditionSatisfied);
    }
    
    @Test
    public void UT_ConditionFear__when_piece_has_adjacent_pieces_that_can_win_it_has_fear() {
        Piece mainPiece = buildPiece(0, CharacterShape.CIRCLE, Color.WHITE, Color.BLACK, 0, 0);
        buildPiece(1, CharacterShape.SQUARE, Color.GREEN, Color.BLACK, 1, 0);
        
        SetBehaviourConditionFearAndRun(mainPiece);
        
        assertTrue("Should've called the defaultActionNode since the adjacent square piece can move it", conditionSatisfied);
    }
    
    @Test
    public void UT_ConditionFear__when_piece_has_has_fear_it_stores_the_source_of_fear_in_the_blackboard() {
        Piece mainPiece = buildPiece(0, CharacterShape.CIRCLE, Color.WHITE, Color.BLACK, 0, 0);
        buildPiece(1, CharacterShape.SQUARE, Color.GREEN, Color.BLACK, 1, 0);
        
        IBlackBoard fakeBlackboard = mock(IBlackBoard.class);
        ObjectFactory.installMock(IBlackBoard.class, fakeBlackboard);
        SetBehaviourConditionFearAndRun(mainPiece);
        ObjectFactory.removeMock(IBlackBoard.class);
        
        verify(fakeBlackboard).setInt(eq(Piece.BLACKBOARD_SCARIEST_ENEMY_CELL), anyInt());
    }
    
    @Test
    public void UT_ConditionFear__when_piece_has_has_fear_it_writes_to_the_log() {
        Piece mainPiece = buildPiece(0, CharacterShape.CIRCLE, Color.WHITE, Color.BLACK, 0, 0);
        buildPiece(1, CharacterShape.SQUARE, Color.GREEN, Color.BLACK, 1, 0);
        SetBehaviourConditionFearAndRun(mainPiece);
        verify(fakeEventsWriter).add(argThat(new ArgumentMatcher<IEvent>() {
            @Override
            public boolean matches(Object item) {
                return ((IEvent)item).toLogicalPredicate().startsWith(EventFactory.HAS_FEAR);
            }
        }));
    }
    
    private void SetBehaviourConditionFearAndRun(Piece mainPiece) {
        final IConditionFear conditionFear = ObjectFactory.createObject(IConditionFear.class);
        conditionFear.setCharacter(mainPiece);
        
        BehaviourTreeNode rootNode = new BehaviourTreeNode();
        rootNode.setCharacter(mainPiece);
        defaultActionNode.setCharacter(mainPiece);
        rootNode.addChildNodeInOrder(conditionFear, 1, defaultActionNode);
        
        mainPiece.setBehaviourTree(rootNode);
        
        mainPiece.run();
    }

    private Piece buildPiece(int id, CharacterShape shape, Color foreground, Color background, int posX, int posY){
        Piece piece = new Piece();
        piece.setId(id);
        piece.setAbmConfiguration(abmConfigurationEntity);
        piece.setEventsWriter(fakeEventsWriter);
        piece.setShape(shape);
        piece.setBackgroundColor(background);
        piece.setForegroundColor(foreground);
        map.putCharacter(piece, map.getCell(posX, posY));
        return piece;
    }
}
