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
        abmConfigurationEntity = new AbmConfigurationEntity(new float[]{1, 0, 0, 0, 0.5f, 0, 0, 0, 0, 0, 0, 0});
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

    // <editor-fold desc="Fear" defaultstate="collapsed">
    
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
    
    //</editor-fold>
    
    // <editor-fold desc="Anticipation" defaultstate="collapsed">
    
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

    //</editor-fold>
    
    // <editor-fold desc="Can improve friend's similarity" defaultstate="collapsed">
    
    @Test
    public void UT_ConditionCanImproveFriendsSimilarity__when_piece_could_exchange_colors_with_a_friend_and_both_would_benefit_it_can_improve_friend_similarity() {
        Piece mainPiece = buildPiece(0, CharacterShape.CIRCLE, Color.WHITE, Color.BLACK, 0, 0);
        buildPiece(1, CharacterShape.CIRCLE, Color.BLACK, Color.WHITE, 5, 5);
        
        SetConditionAndRun(mainPiece, ObjectFactory.createObject(IConditionCanImproveFriendSimilarity.class));
        assertTrue("Should've called the defaultActionNode since both characters could be happier if they exchange colors", 
                conditionSatisfied);
    }

    @Test
    public void UT_ConditionCanImproveFriendsSimilarity__when_piece_could_exchange_colors_with_a_friend_and_both_would_NOT_benefit_it_cannot_improve_friend_similarity() {
        Piece mainPiece = buildPiece(0, CharacterShape.CIRCLE, Color.WHITE, Color.BLACK, 0, 0);
        buildPiece(1, CharacterShape.CIRCLE, Color.BLACK, Color.BLACK, 5, 5);
        
        SetConditionAndRun(mainPiece, ObjectFactory.createObject(IConditionCanImproveFriendSimilarity.class));
        assertFalse("Shouldn't have called the defaultActionNode since the second character is already happy", 
                conditionSatisfied);
    }
    
    @Test
    public void UT_ConditionCanImproveFriendsSimilarity__when_piece_could_exchange_colors_with_a_piece_but_it_is_an_ememy_it_cannot_improve_friend_similarity() {
        Piece mainPiece = buildPiece(0, CharacterShape.CIRCLE, Color.WHITE, Color.BLACK, 0, 0);
        buildPiece(1, CharacterShape.SQUARE, Color.BLACK, Color.WHITE, 5, 5);
        
        SetConditionAndRun(mainPiece, ObjectFactory.createObject(IConditionCanImproveFriendSimilarity.class));
        assertFalse("Shouldn't have called the defaultActionNode since the second character is an enemy", 
                conditionSatisfied);
    }
    
    //</editor-fold>
    
    // <editor-fold desc="Can improve self-similarity" defaultstate="collapsed">
    
    @Test
    public void UT_ConditionCanImproveSelfSimilarity__when_staining_with_the_spot_would_benefit_the_piece_it_Can_improve_self_similarity() {
        Piece mainPiece = buildPiece(0, CharacterShape.CIRCLE, Color.WHITE, Color.BLACK, 0, 0);
        buildSpot(2, Color.WHITE, 5, 5);
        
        SetConditionAndRun(mainPiece, ObjectFactory.createObject(IConditionCanImproveSelfSimilarity.class));
        assertTrue("Should've called the defaultActionNode since staining with the spot would benefit the piece", 
                conditionSatisfied);
    }

    @Test
    public void UT_ConditionCanImproveSelfSimilarity__when_staining_with_the_spot_would_NOT_benefit_the_piece_it_cannot_improve_self_similarity() {
        Piece mainPiece = buildPiece(0, CharacterShape.CIRCLE, Color.WHITE, Color.BLACK, 0, 0);
        buildSpot(2, Color.BLACK, 5, 5);
        
        SetConditionAndRun(mainPiece, ObjectFactory.createObject(IConditionCanImproveSelfSimilarity.class));
        assertFalse("Shouldn't have called the defaultActionNode since staining with the spot would NOT benefit the piece", 
                conditionSatisfied);
    }
    
    @Test
    public void UT_ConditionCanImproveSelfSimilarity__when_there_is_no_spot_it_cannot_improve_self_similarity() {
        Piece mainPiece = buildPiece(0, CharacterShape.CIRCLE, Color.WHITE, Color.BLACK, 0, 0);
        
        SetConditionAndRun(mainPiece, ObjectFactory.createObject(IConditionCanImproveSelfSimilarity.class));
        assertFalse("Shouldn't have called the defaultActionNode since there is no spot", 
                conditionSatisfied);
    }

    @Test
    public void UT_ConditionCanImproveSelfSimilarity__when_staining_with_the_spot_would_benefit_the_piece_but_the_cell_is_occupied_by_a_piece_that_cannot_displace_it_cannot_improve_self_similarity() {
        Piece mainPiece = buildPiece(0, CharacterShape.CIRCLE, Color.WHITE, Color.BLACK, 0, 0);
        buildSpot(2, Color.BLACK, 5, 5);
        buildPiece(0, CharacterShape.SQUARE, Color.WHITE, Color.BLACK, 5, 5);
        
        SetConditionAndRun(mainPiece, ObjectFactory.createObject(IConditionCanImproveSelfSimilarity.class));
        assertFalse("Shouldn't have called the defaultActionNode since the spot is occupied by a piece that cannot be displaced", 
                conditionSatisfied);
    }
    
    @Test
    public void UT_ConditionCanImproveSelfSimilarity__when_staining_with_the_spot_would_benefit_the_piece_and_the_cell_is_occupied_by_a_piece_that_can_displace_it_can_improve_self_similarity() {
        Piece mainPiece = buildPiece(0, CharacterShape.CIRCLE, Color.WHITE, Color.BLACK, 0, 0);
        buildSpot(2, Color.WHITE, 5, 5);
        buildPiece(0, CharacterShape.TRIANGLE, Color.WHITE, Color.BLACK, 5, 5);
        
        SetConditionAndRun(mainPiece, ObjectFactory.createObject(IConditionCanImproveSelfSimilarity.class));
        assertTrue("Should've called the defaultActionNode since the spot is occupied by a piece that can be displaced", 
                conditionSatisfied);
    }
    
    //</editor-fold>
    
    // <editor-fold desc="Can reduce enemy's similarity" defaultstate="collapsed">
    
    @Test
    public void UT_ConditionCanReduceEnemySimilarity__when_neighbour_is_not_enemy_it_cannot_reduce_enemy_similarity() {
        Piece mainPiece = buildPiece(0, CharacterShape.CIRCLE, Color.WHITE, Color.BLACK, 0, 0);
        buildPiece(1, CharacterShape.CIRCLE, Color.BLACK, Color.WHITE, 5, 5);
        
        SetConditionAndRun(mainPiece, ObjectFactory.createObject(IConditionCanReduceEnemySimilarity.class));
        assertFalse("Shouldn't have called the defaultActionNode since the neighbour is not an enemy", 
                conditionSatisfied);
    }
    
    @Test
    public void UT_ConditionCanReduceEnemySimilarity__when_neighbour_is_an_enemy_but_cannot_lose_it_cannot_reduce_enemy_similarity() {
        Piece mainPiece = buildPiece(0, CharacterShape.CIRCLE, Color.WHITE, Color.BLACK, 0, 0);
        buildPiece(1, CharacterShape.SQUARE, Color.BLACK, Color.WHITE, 5, 5);
        
        SetConditionAndRun(mainPiece, ObjectFactory.createObject(IConditionCanReduceEnemySimilarity.class));
        assertFalse("Shouldn't have called the defaultActionNode since the neighbour can win the main character", 
                conditionSatisfied);
    }

    @Test
    public void UT_ConditionCanReduceEnemySimilarity__when_neighbour_is_an_enemy_and_cannot_win_it_can_reduce_enemy_similarity() {
        Piece mainPiece = buildPiece(0, CharacterShape.CIRCLE, Color.WHITE, Color.BLACK, 0, 0);
        buildPiece(1, CharacterShape.TRIANGLE, Color.BLACK, Color.WHITE, 5, 5);
        
        SetConditionAndRun(mainPiece, ObjectFactory.createObject(IConditionCanReduceEnemySimilarity.class));
        assertTrue("Should've called the defaultActionNode since the neighbour an enemy and can lose against the main character", 
                conditionSatisfied);
    }
    
    //</editor-fold>
    
    // <editor-fold desc="Sadness" defaultstate="collapsed">
    
    @Test
    public void UT_ConditionSadness__when_piece_has_a_joy_level_over_the_threshold_it_doesnt_have_sadness() {
        float JOY_THRESHOLD = 0.1f;
        abmConfigurationEntity = new AbmConfigurationEntity(new float[]{1, 0, 0, 0, JOY_THRESHOLD, 0, 0, 0, 0, 0, 0});
        Piece mainPiece = buildPiece(0, CharacterShape.CIRCLE, Color.WHITE, Color.GRAY, 0, 0);
        SetConditionAndRun(mainPiece, ObjectFactory.createObject(IConditionSadness.class));

        assertFalse("Shouldn't have called the defaultActionNode since the joy level of the piece is over the threshold", conditionSatisfied);
    }

    @Test
    public void UT_ConditionSadness__when_piece_has_a_joy_level_below_the_threshold_it_has_sadness() {
        float JOY_THRESHOLD = 0.9f;
        abmConfigurationEntity = new AbmConfigurationEntity(new float[]{1, 0, 0, 0, JOY_THRESHOLD, 0, 0, 0, 0, 0, 0});
        Piece mainPiece = buildPiece(0, CharacterShape.CIRCLE, Color.WHITE, Color.GRAY, 0, 0);
        SetConditionAndRun(mainPiece, ObjectFactory.createObject(IConditionSadness.class));

        assertTrue("Should've called the defaultActionNode since the joy level of the piece is below the threshold", conditionSatisfied);
    }
    //</editor-fold>
    
    // <editor-fold desc="Surprise" defaultstate="collapsed">
    
    @Test
    public void UT_ConditionSadness__when_piece_has_a_joy_level_and_the_next_turn_it_is_reduced_a_quantity_below_surprise_threshold_it_does_not_have_surprise() {
        float SURPRISE_THRESHOLD = 0.9f;
        abmConfigurationEntity = new AbmConfigurationEntity(new float[]{1, 0, 0, 0, 0.5f, SURPRISE_THRESHOLD, 0, 0, 0, 0, 0});
        
        Piece mainPiece = buildPiece(0, CharacterShape.CIRCLE, Color.WHITE, Color.GRAY, 0, 0);
        SetConditionAndRun(mainPiece, ObjectFactory.createObject(IConditionSurprise.class));  
        assertFalse("Shouldn't have called the defaultActionNode in the first turn", conditionSatisfied);
        
        mainPiece.setBackgroundColor(Color.BLACK);
        SetConditionAndRun(mainPiece, ObjectFactory.createObject(IConditionSurprise.class));
        assertFalse("Shouldn't have called the defaultActionNode since the joy level hasn't been reduced "
                + "a quantity over the threshold", conditionSatisfied);
    }
    
    @Test
    public void UT_ConditionSadness__when_piece_has_a_joy_level_and_the_next_turn_it_is_reduced_a_quantity_over_surprise_threshold_it_has_surprise() {
        float SURPRISE_THRESHOLD = 0.3f;
        abmConfigurationEntity = new AbmConfigurationEntity(new float[]{1, 0, 0, 0, 0.5f, SURPRISE_THRESHOLD, 0, 0, 0, 0, 0});
        
        Piece mainPiece = buildPiece(0, CharacterShape.CIRCLE, Color.WHITE, Color.GRAY, 0, 0);
        SetConditionAndRun(mainPiece, ObjectFactory.createObject(IConditionSurprise.class));  
        assertFalse("Shouldn't have called the defaultActionNode in the first turn", conditionSatisfied);
        
        mainPiece.setBackgroundColor(Color.BLACK);
        SetConditionAndRun(mainPiece, ObjectFactory.createObject(IConditionSurprise.class));
        assertTrue("Should've called the defaultActionNode since the joy level has been reduced "
                + "a quantity over the threshold", conditionSatisfied);
    }
    
    @Test
    public void UT_ConditionSadness__when_piece_has_a_joy_level_and_the_next_turn_it_is_increased_it_cannot_have_surprise() {
        float SURPRISE_THRESHOLD = 0.0f;
        abmConfigurationEntity = new AbmConfigurationEntity(new float[]{1, 0, 0, 0, 0.5f, SURPRISE_THRESHOLD, 0, 0, 0, 0, 0});
        
        Piece mainPiece = buildPiece(0, CharacterShape.CIRCLE, Color.WHITE, Color.GRAY, 0, 0);
        SetConditionAndRun(mainPiece, ObjectFactory.createObject(IConditionSurprise.class));  
        assertFalse("Shouldn't have called the defaultActionNode in the first turn", conditionSatisfied);
        
        mainPiece.setBackgroundColor(Color.WHITE);
        SetConditionAndRun(mainPiece, ObjectFactory.createObject(IConditionSurprise.class));
        assertFalse("Shouldn't have called the defaultActionNode since the joy level has increased", conditionSatisfied);
    }
    
    //</editor-fold>
    
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
    
    // <editor-fold desc="Private methods" defaultstate="collapsed">
    
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
    
    // </editor-fold>
}
