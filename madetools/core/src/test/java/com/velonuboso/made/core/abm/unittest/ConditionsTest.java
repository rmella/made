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

import com.velonuboso.made.core.abm.api.IBlackBoard;
import com.velonuboso.made.core.abm.api.IEventsWriter;
import com.velonuboso.made.core.abm.api.IMap;
import com.velonuboso.made.core.abm.api.condition.ICondition;
import com.velonuboso.made.core.abm.api.condition.IConditionAnticipation;
import com.velonuboso.made.core.abm.api.condition.IConditionCanImproveFriendSimilarity;
import com.velonuboso.made.core.abm.api.condition.IConditionCanImproveSelfSimilarity;
import com.velonuboso.made.core.abm.api.condition.IConditionCanReduceEnemySimilarity;
import com.velonuboso.made.core.abm.api.condition.IConditionFear;
import com.velonuboso.made.core.abm.api.condition.IConditionSadness;
import com.velonuboso.made.core.abm.api.condition.IConditionSurprise;
import com.velonuboso.made.core.abm.entity.CharacterShape;
import com.velonuboso.made.core.abm.implementation.BehaviourTreeNode;
import com.velonuboso.made.core.abm.implementation.BlackBoard;
import com.velonuboso.made.core.abm.implementation.ColorSpot;
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
import org.junit.Ignore;
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
        abmConfigurationEntity = new AbmConfigurationEntity(new float[]{0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0});
        fakeEventsWriter = mock(IEventsWriter.class);
        map = ObjectFactory.createObject(IMap.class);
        map.initialize(10, 10);
        defaultActionNode = new BehaviourTreeNode();
        conditionSatisfied = false;
        defaultActionNode.setActionWhenRun((currentBlackBoard, oldBlackBoard) -> {
            conditionSatisfied = true;
        }
        );
        ObjectFactory.cleanAllMocks();
    }

    @Test
    public void UT_ConditionFear__when_piece_has_no_adjacent_pieces_the_piece_has_no_fear() {
        Piece mainPiece = buildPiece(0, CharacterShape.CIRCLE, Color.WHITE, Color.BLACK, 0, 0);
        buildPiece(1, CharacterShape.CIRCLE, Color.GREEN, Color.BLACK, 5, 5);

        SetConditionAndRun(mainPiece, ObjectFactory.createObject(IConditionFear.class));

        assertFalse("Shouldn't have called the defaultActionNode since the adjacent circular piece cannot move it", conditionSatisfied);
    }

    @Test
    public void UT_ConditionFear__when_piece_has_adjacent_pieces_that_cannot_win_it_has_no_fear() {
        Piece mainPiece = buildPiece(0, CharacterShape.CIRCLE, Color.WHITE, Color.BLACK, 0, 0);
        buildPiece(1, CharacterShape.CIRCLE, Color.GREEN, Color.BLACK, 1, 0);

        SetConditionAndRun(mainPiece, ObjectFactory.createObject(IConditionFear.class));

        assertFalse("Shouldn't have called the defaultActionNode since the adjacent circular piece cannot move it", conditionSatisfied);
    }

    @Test
    public void UT_ConditionFear__when_piece_has_adjacent_pieces_that_can_win_it_has_fear() {
        Piece mainPiece = buildPiece(0, CharacterShape.CIRCLE, Color.WHITE, Color.BLACK, 0, 0);
        buildPiece(1, CharacterShape.SQUARE, Color.GREEN, Color.BLACK, 1, 0);

        SetConditionAndRun(mainPiece, ObjectFactory.createObject(IConditionFear.class));

        assertTrue("Should've called the defaultActionNode since the adjacent square piece can move it", conditionSatisfied);
    }

    @Test
    public void UT_ConditionAnticipation__when_piece_has_no_adjacent_spot_it_has_no_anticipation() {
        Piece mainPiece = buildPiece(0, CharacterShape.CIRCLE, Color.GREEN, Color.BLACK, 0, 0);
        buildSpot(1, Color.GREEN, 3, 3);

        SetConditionAndRun(mainPiece, ObjectFactory.createObject(IConditionAnticipation.class));

        assertFalse("Shouldn't have called the defaultActionNode since there's no adjacent spot", conditionSatisfied);
    }

    @Test
    public void UT_ConditionAnticipation__when_piece_has_adjacent_spot_but_cannot_improve_self_similarity_it_has_no_anticipation() {
        Piece mainPiece = buildPiece(0, CharacterShape.CIRCLE, Color.GREEN, Color.BLACK, 0, 0);
        buildSpot(1, Color.BLACK, 1, 1);

        SetConditionAndRun(mainPiece, ObjectFactory.createObject(IConditionAnticipation.class));

        assertFalse("Shouldn't have called the defaultActionNode since the adjacent spot will not improve similarity",
                conditionSatisfied);
    }

    @Test
    public void UT_ConditionAnticipation__when_piece_has_adjacent_spot_and_can_improve_self_similarity_but_it_is_occupied_it_has_anticipation() {
        Piece mainPiece = buildPiece(0, CharacterShape.CIRCLE, Color.GREEN, Color.BLACK, 0, 0);
        buildPiece(1, CharacterShape.CIRCLE, Color.GREEN, Color.BLACK, 1, 1);
        buildSpot(2, Color.GREEN, 1, 1);

        SetConditionAndRun(mainPiece, ObjectFactory.createObject(IConditionAnticipation.class));

        assertFalse("Shouldn't have called the defaultActionNode since the adjacent spot has currently a piece above",
                conditionSatisfied);
    }

    @Test
    public void UT_ConditionAnticipation__when_piece_has_adjacent_spot_and_can_improve_self_similarity_it_has_anticipation() {
        Piece mainPiece = buildPiece(0, CharacterShape.CIRCLE, Color.GREEN, Color.BLACK, 0, 0);
        buildSpot(2, Color.GREEN, 1, 1);

        SetConditionAndRun(mainPiece, ObjectFactory.createObject(IConditionAnticipation.class));

        assertTrue("Should've called the defaultActionNode since the adjacent spot will improve similarity",
                conditionSatisfied);
    }

    // <editor-fold desc="Storage into blackboard" defaultstate="collapsed">
    
    @Test
    public void UT_ConditionFear__when_piece_has_fear_it_stores_the_source_of_fear_in_the_blackboard() {
        Piece mainPiece = buildPiece(0, CharacterShape.CIRCLE, Color.WHITE, Color.BLACK, 0, 0);
        buildPiece(1, CharacterShape.SQUARE, Color.GREEN, Color.BLACK, 1, 0);
        checkBlackboardWhenConditionIsRun(mainPiece, IConditionFear.class, Piece.BLACKBOARD_CHARACTER_CELL);
    }

    @Test
    public void UT_ConditionAnticipation__when_piece_has_anticipation_it_stores_the_target_spot_in_blackboard() {
        Piece mainPiece = buildPiece(0, CharacterShape.CIRCLE, Color.WHITE, Color.BLACK, 0, 0);
        buildSpot(2, Color.GREEN, 1, 1);
        checkBlackboardWhenConditionIsRun(mainPiece, IConditionAnticipation.class, Piece.BLACKBOARD_SPOT_CELL);
    }
    
    
    @Test
    public void ConditionCanImproveFriendSimilarity__when_piece_can_improve_friend_similarity_it_stores_the_target_friend_in_the_blackboard() {
        Piece mainPiece = buildPiece(0, CharacterShape.CIRCLE, Color.WHITE, Color.BLACK, 0, 0);
        buildPiece(1, CharacterShape.CIRCLE, Color.BLACK, Color.WHITE, 5, 5);
        checkBlackboardWhenConditionIsRun(mainPiece, IConditionCanImproveFriendSimilarity.class, Piece.BLACKBOARD_CHARACTER_CELL);
    }
    
    @Test
    public void UT_ConditionCanImproveSelfSimilarity__when_piece_can_improve_self_similarity_it_stores_the_target_spot_in_blackboard() {
        Piece mainPiece = buildPiece(0, CharacterShape.CIRCLE, Color.WHITE, Color.BLACK, 0, 0);
        buildSpot(2, Color.WHITE, 5, 5);
        checkBlackboardWhenConditionIsRun(mainPiece, IConditionCanImproveSelfSimilarity.class, Piece.BLACKBOARD_SPOT_CELL);
    }

    
    @Test
    public void UT_ConditionCanReduceEnemySimilarity__when_piece_can_reduce_enemy_similarity_it_stores_the_enemy_in_the_blackboard() {
        Piece mainPiece = buildPiece(0, CharacterShape.CIRCLE, Color.WHITE, Color.BLACK, 0, 0);
        buildPiece(1, CharacterShape.TRIANGLE, Color.BLACK, Color.WHITE, 5, 5);
        checkBlackboardWhenConditionIsRun(mainPiece, IConditionCanReduceEnemySimilarity.class, Piece.BLACKBOARD_CHARACTER_CELL);
    }
    
    @Test
    public void UT_ConditionSadness__when_piece_is_it_stores_nothing_in_the_blackboard() {
        Piece mainPiece = buildPiece(0, CharacterShape.CIRCLE, Color.WHITE, Color.BLACK, 0, 0);
        buildSpot(2, Color.WHITE, 5, 5);
        checkBlackboardWhenConditionIsRun(mainPiece, IConditionSadness.class, Piece.BLACKBOARD_SPOT_CELL, 0);
    }
    
    @Ignore
    @Test
    public void UT_ConditionSurprise__when_piece_is_surprised_it_stores_the_source_of_surprise_in_the_blackboard() {
        Piece mainPiece = buildPiece(0, CharacterShape.CIRCLE, Color.WHITE, Color.BLACK, 0, 0);
        buildSpot(2, Color.WHITE, 5, 5);
        checkBlackboardWhenConditionIsRun(mainPiece, IConditionSurprise.class, Piece.BLACKBOARD_CHARACTER_CELL);
    }
    
    //</editor-fold>

    // <editor-fold desc="Log writing" defaultstate="collapsed">
    
    @Test
    public void UT_ConditionFear__when_piece_has_fear_it_writes_to_the_log() {
        Piece mainPiece = buildPiece(0, CharacterShape.CIRCLE, Color.WHITE, Color.BLACK, 0, 0);
        buildPiece(1, CharacterShape.SQUARE, Color.GREEN, Color.BLACK, 1, 0);
        checkLogWhenConditionIsRun(mainPiece, IConditionFear.class, EventFactory.HAS_FEAR);
    }
    
    @Test
    public void UT_ConditionAnticipation__when_piece_has_anticipation_it_writes_to_the_log() {
        Piece mainPiece = buildPiece(0, CharacterShape.CIRCLE, Color.WHITE, Color.BLACK, 0, 0);
        buildSpot(2, Color.GREEN, 1, 1);
        checkLogWhenConditionIsRun(mainPiece, IConditionAnticipation.class, EventFactory.HAS_ANTICIPATION);
    }
    
    @Test
    public void UT_ConditionCanImproveFriendSimilarity__when_piece_can_improve_friend_similarity_it_writes_to_the_log() {
        Piece mainPiece = buildPiece(0, CharacterShape.CIRCLE, Color.WHITE, Color.BLACK, 0, 0);
        buildPiece(1, CharacterShape.CIRCLE, Color.BLACK, Color.WHITE, 5, 5);
        checkLogWhenConditionIsRun(mainPiece, IConditionCanImproveFriendSimilarity.class, EventFactory.CAN_IMPROVE_FRIEND_SIMILARITY);
    }
    
    @Test
    public void UT_ConditionCanImproveSelfSimilarity__when_piece_can_improve_self_similarity_it_writes_to_the_log() {
        Piece mainPiece = buildPiece(0, CharacterShape.CIRCLE, Color.WHITE, Color.BLACK, 0, 0);
        buildSpot(2, Color.WHITE, 5, 5);
        checkLogWhenConditionIsRun(mainPiece, IConditionCanImproveSelfSimilarity.class, EventFactory.CAN_IMPROVE_SELF_SIMILARITY);
    }
    
    
    @Test
    public void UT_ConditionCanReduceEnemySimilarity__when_piece_can_reduce_enemy_similarity_it_writes_to_the_log() {
        Piece mainPiece = buildPiece(0, CharacterShape.CIRCLE, Color.WHITE, Color.BLACK, 0, 0);
        buildPiece(1, CharacterShape.TRIANGLE, Color.BLACK, Color.WHITE, 5, 5);
        checkLogWhenConditionIsRun(mainPiece, IConditionCanReduceEnemySimilarity.class, EventFactory.CAN_REDUCE_ENEMY_SIMILARITY);
    }
    
    @Test
    public void UT_ConditionSadness__when_piece_is_sad_it_writes_to_the_log() {
        abmConfigurationEntity = new AbmConfigurationEntity(new float[]{0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0});
        Piece mainPiece = buildPiece(0, CharacterShape.CIRCLE, Color.WHITE, Color.BLACK, 0, 0);
        buildSpot(2, Color.WHITE, 5, 5);
        checkLogWhenConditionIsRun(mainPiece, IConditionSadness.class, EventFactory.IS_SAD);
    }
    
    @Ignore
    @Test
    public void UT_ConditionSurprise__when_piece_is_surprised_it_writes_to_the_log() {
        Piece mainPiece = buildPiece(0, CharacterShape.CIRCLE, Color.WHITE, Color.BLACK, 0, 0);
        buildSpot(2, Color.WHITE, 5, 5);
        checkLogWhenConditionIsRun(mainPiece, IConditionSurprise.class, EventFactory.IS_SURPRISED);
    }
    
    // </editor-fold>
    
    
    private void checkBlackboardWhenConditionIsRun(Piece targetPiece, Class conditionType, String propertyName){
        checkBlackboardWhenConditionIsRun(targetPiece, conditionType, propertyName, 1);
    }
    
    private void checkBlackboardWhenConditionIsRun(Piece targetPiece, Class conditionType, String propertyName,
            int expectedNumberOfInvocations) {
        IBlackBoard fakeBlackboard = spy(ObjectFactory.createObject(IBlackBoard.class));
        ObjectFactory.installMock(IBlackBoard.class, fakeBlackboard);
        SetConditionAndRun(targetPiece, (ICondition) ObjectFactory.createObject(conditionType));
        ObjectFactory.removeMock(IBlackBoard.class);

        verify(fakeBlackboard,times(expectedNumberOfInvocations)).setInt(eq(propertyName), anyInt());
    }
    
    
    private void checkLogWhenConditionIsRun(Piece targetPiece, Class conditionType, String predicateBeginsWith) {
        SetConditionAndRun(targetPiece, (ICondition) ObjectFactory.createObject(conditionType));
        
        verify(fakeEventsWriter).add(argThat(new ArgumentMatcher<IEvent>() {
            @Override
            public boolean matches(Object item) {
                return ((IEvent) item).toLogicalPredicate().startsWith(predicateBeginsWith);
            }
        }));
    }
    
    private void SetConditionAndRun(Piece mainPiece, ICondition condition) {
        condition.setCharacter(mainPiece);
        BehaviourTreeNode rootNode = new BehaviourTreeNode();
        rootNode.setCharacter(mainPiece);
        defaultActionNode.setCharacter(mainPiece);
        rootNode.addChildNodeInOrder(condition, 1, defaultActionNode);
        mainPiece.setBehaviourTree(rootNode);
        mainPiece.run();
    }

    private Piece buildPiece(int id, CharacterShape shape, Color foreground, Color background, int posX, int posY) {
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

    private void buildSpot(int id, Color color, int posX, int posY) {
        ColorSpot spot = new ColorSpot();
        spot.setId(id);
        spot.setColor(color);
        map.putColorSpot(spot, map.getCell(posX, posY));
    }
}
