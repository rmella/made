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

import com.velonuboso.made.core.abm.api.IAction;
import com.velonuboso.made.core.abm.api.IBehaviourTreeNode;
import com.velonuboso.made.core.abm.api.IBlackBoard;
import com.velonuboso.made.core.abm.api.ICharacter;
import com.velonuboso.made.core.abm.api.IEventsWriter;
import com.velonuboso.made.core.abm.api.IMap;
import com.velonuboso.made.core.abm.api.condition.ICondition;
import com.velonuboso.made.core.abm.api.strategy.IStrategyMoveAway;
import com.velonuboso.made.core.abm.entity.CharacterShape;
import com.velonuboso.made.core.abm.implementation.BehaviourTreeNode;
import com.velonuboso.made.core.abm.implementation.ColorSpot;
import com.velonuboso.made.core.abm.implementation.EventsWriter;
import com.velonuboso.made.core.abm.implementation.piece.Piece;
import com.velonuboso.made.core.abm.implementation.piece.strategy.StrategyMoveAway;
import com.velonuboso.made.core.common.api.IEvent;
import com.velonuboso.made.core.common.entity.AbmConfigurationEntity;
import com.velonuboso.made.core.common.implementation.EventFactory;
import com.velonuboso.made.core.common.util.ObjectFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import javafx.scene.paint.Color;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;
import org.mockito.ArgumentMatcher;
import static org.mockito.Mockito.*;
import static org.hamcrest.CoreMatchers.*;

/**
 *
 * @author Rubén Héctor García (raiben@gmail.com)
 */
public class StrategiesTest {

    private AbmConfigurationEntity abmConfigurationEntity;
    private IEventsWriter fakeEventsWriter;
    private IMap map;
    private boolean conditionSatisfied;
    private IBlackBoard fakeBlackboard;

    @Before
    public void setUp() {
        abmConfigurationEntity = new AbmConfigurationEntity(new float[]{1, 0, 0, 0, 0.5f, 0, 0, 0, 0, 0, 0, 0});
        fakeEventsWriter = mock(IEventsWriter.class);
        map = ObjectFactory.createObject(IMap.class);
        map.initialize(10, 10);
        ObjectFactory.cleanAllMocks();
        fakeBlackboard = spy(ObjectFactory.createObject(IBlackBoard.class));
    }

    @Test
    public void UT_StrategyMoveAway_When_there_are_empty_cells_it_moves_successfully_to_an_empty_cell() {
        Piece mainPiece = buildPiece(0, CharacterShape.CIRCLE, Color.BLUE, Color.RED, 0, 0);
        setStrategyAndRun(-1, mainPiece, ObjectFactory.createObject(IStrategyMoveAway.class));
        
        int oldCell = (int)map.getCell(mainPiece);
        setStrategyAndRun(-1, mainPiece, ObjectFactory.createObject(IStrategyMoveAway.class));
        assertThat("Piece position should not change since there's no place to go", 
                (int)map.getCell(mainPiece), is(not(oldCell)));
    }

    @Test
    public void UT_StrategyMoveAway_When_there_is_only_one_empty_cell_it_moves_successfully_to_the_cell() {
        int freeCellX = 1;
        int freeCellY = 1;
        
        Piece mainPiece = buildPiece(0, CharacterShape.CIRCLE, Color.BLUE, Color.RED, 0, 0);
        buildPiece(1, CharacterShape.CIRCLE, Color.BLUE, Color.RED, -1, -1);
        buildPiece(2, CharacterShape.CIRCLE, Color.BLUE, Color.RED, -1, 0);
        buildPiece(3, CharacterShape.CIRCLE, Color.BLUE, Color.RED, -1, 1);
        buildPiece(4, CharacterShape.CIRCLE, Color.BLUE, Color.RED, 0, -1);
        buildPiece(5, CharacterShape.CIRCLE, Color.BLUE, Color.RED, 0, 1);
        buildPiece(6, CharacterShape.CIRCLE, Color.BLUE, Color.RED, 1, -1);
        buildPiece(7, CharacterShape.CIRCLE, Color.BLUE, Color.RED, 1, 0);
                
        setStrategyAndRun(100, mainPiece, ObjectFactory.createObject(IStrategyMoveAway.class));
        
        int expectedCell = map.getCell(freeCellX, freeCellY);
        assertEquals("New piece's position should be the free position", (int)map.getCell(mainPiece), expectedCell);
    }

    @Test
    public void UT_StrategyMoveAway_When_there_is_no_empty_cell_it_fails() {
        Piece mainPiece = buildPiece(0, CharacterShape.CIRCLE, Color.BLUE, Color.RED, 0, 0);
        buildPiece(1, CharacterShape.CIRCLE, Color.BLUE, Color.RED, -1, -1);
        buildPiece(2, CharacterShape.CIRCLE, Color.BLUE, Color.RED, -1, 0);
        buildPiece(3, CharacterShape.CIRCLE, Color.BLUE, Color.RED, -1, 1);
        buildPiece(4, CharacterShape.CIRCLE, Color.BLUE, Color.RED, 0, -1);
        buildPiece(5, CharacterShape.CIRCLE, Color.BLUE, Color.RED, 0, 1);
        buildPiece(6, CharacterShape.CIRCLE, Color.BLUE, Color.RED, 1, -1);
        buildPiece(7, CharacterShape.CIRCLE, Color.BLUE, Color.RED, 1, 0);
        buildPiece(8, CharacterShape.CIRCLE, Color.BLUE, Color.RED, 1, 1);
        
        int oldCell = (int)map.getCell(mainPiece);
        setStrategyAndRun(-1, mainPiece, ObjectFactory.createObject(IStrategyMoveAway.class));
        assertEquals("Piece position should not change since there's no place to go", 
                (int)map.getCell(mainPiece), oldCell);
    }

    @Test
    public void UT_StrategyMoveAway_When_called_successfully_it_writes_to_the_log() {
        Piece mainPiece = buildPiece(0, CharacterShape.CIRCLE, Color.BLUE, Color.RED, 0, 0);
        setStrategyAndRun(-1, mainPiece, ObjectFactory.createObject(IStrategyMoveAway.class));
        verifyEventAddedToFakeEventWriter(EventFactory.MOVES_AWAY, 1);
    }

    @Test
    public void UT_StrategyMoveAway_When_called_unsuccessfully_it_does_not_write_to_the_log() {
        Piece mainPiece = buildPiece(0, CharacterShape.CIRCLE, Color.BLUE, Color.RED, 0, 0);
        buildPiece(1, CharacterShape.CIRCLE, Color.BLUE, Color.RED, -1, -1);
        buildPiece(2, CharacterShape.CIRCLE, Color.BLUE, Color.RED, -1, 0);
        buildPiece(3, CharacterShape.CIRCLE, Color.BLUE, Color.RED, -1, 1);
        buildPiece(4, CharacterShape.CIRCLE, Color.BLUE, Color.RED, 0, -1);
        buildPiece(5, CharacterShape.CIRCLE, Color.BLUE, Color.RED, 0, 1);
        buildPiece(6, CharacterShape.CIRCLE, Color.BLUE, Color.RED, 1, -1);
        buildPiece(7, CharacterShape.CIRCLE, Color.BLUE, Color.RED, 1, 0);
        buildPiece(8, CharacterShape.CIRCLE, Color.BLUE, Color.RED, 1, 1);
        
        setStrategyAndRun(-1, mainPiece, ObjectFactory.createObject(IStrategyMoveAway.class));
        verifyEventAddedToFakeEventWriter(EventFactory.MOVES_AWAY, 0);
    }

    public void UT_StrategyMoveOrDisplace_When_the_piece_is_already_in_the_cell_it_fails() {
    }

    public void UT_StrategyMoveOrDisplace_When_the_cell_is_around_and_free_it_moves_sucesfully_to_the_cell() {
    }

    public void UT_StrategyMoveOrDisplace_When_the_cell_is_around_and_occupied_by_a_piece_that_can_be_won_it_displaces_the_piece_and_moves_to_the_cell() {
    }

    public void UT_StrategyMoveOrDisplace_When_the_cell_is_around_and_occupied_by_a_piece_that_can_be_won_but_is_not__enemy_it_fails() {
    }

    public void UT_StrategyMoveOrDisplace_When_the_cell_is_around_and_occupied_by_a_piece_that_can_be_won_but_does_not_have_free_cells_around_it_fails() {
    }

    public void UT_StrategyMoveOrDisplace_When_the_cell_is_around_and_occupied_by_a_piece_that_cannot_be_won_it_fails() {
    }

    public void UT_StrategyMoveOrDisplace_When_the_cell_is_not_around_and_the_closer_cell_is_free_it_moves_successfully_to_the_piece() {
    }

    public void UT_StrategyMoveOrDisplace_When_the_cell_is_not_around_and_the_closer_cell_is_occupied_by_a_piece_that_can_be_won_it_displaces_the_piece_and_moves_to_the_cell() {
    }

    public void UT_StrategyMoveOrDisplace_When_the_cell_is_not_around_and_the_closer_cell_is_occupied_by_a_piece_that_can_be_won_but_is_not_enemy_it_fails() {
    }

    public void UT_StrategyMoveOrDisplace_When_the_cell_is_not_around_and_the_closer_cell_is_occupied_by_a_piece_that_can_be_won_but_does_not_have_free_cells_around_it_fails() {
    }

    public void UT_StrategyMoveOrDisplace_When_the_cell_is_not_around_and_the_closer_cell_is_occupied_by_a_piece_that_cannot_be_won_it_fails() {
    }

    public void UT_StrategyMoveOrDisplace_When_moves_successfully_it_writes_to_the_log() {
    }

    public void UT_StrategyMoveOrDisplace_When_moves_and_displaces_successfully_it_writes_to_the_log() {
    }

    public void UT_StrategyMoveOrDisplace_When_called_unsuccessfully_it_does_not_write_to_the_log() {
    }

    public void UT_StrategySkipTurn_the_piece_does_not_move_or_stain() {
    }

    public void UT_StrategySkipTurn_When_called_successfully_it_writes_to_the_log() {
    }

    public void UT_StrategyStain_When_the_cell_is_not_around_it_fails() {
    }

    public void UT_StrategyStain_When_the_cell_does_not_have_a_spot() {
    }

    public void UT_StrategyStain_When_the_cell_is_around_and_has_a_spot_the_piece_fills_its_background_with_the_spot_color_succesfully() {
    }

    public void UT_StrategyStain_When_called_succesfully_and_the_random_value_is_bellow_the_probability_the_spot_dissapears() {
    }

    public void UT_StrategyStain_When_called_succesfully_and_the_random_value_is_over_the_probability_the_spot_remains() {
    }

    public void UT_StrategyStain_When_stains_successfully_it_writes_to_the_log() {
    }

    public void UT_StrategyStain_When_the_spot_dissapears_it_writes_to_the_log() {
    }

    public void UT_StrategyStain_When_called_unsuccessfully_it_does_not_write_to_the_log() {
    }

    public void UT_StrategyTransferColor_When_cell_is_not_around_it_fails() {
    }

    public void UT_StrategyTransferColor_When_cell_does_not_have_a_piece_it_fails() {
    }

    public void UT_StrategyTransferColor_When_piece_around_is_an_enemy_it_fails() {
    }

    public void UT_StrategyTransferColor_When_piece_around_is_a_friend_but_cannot_be_won_it_fails() {
    }

    public void UT_StrategyTransferColor_When_piece_around_is_a_friend_it_transfers_bachground_colors_succesfully() {
    }

    public void UT_StrategyTransferColor_When_transfer_color_succesfully_it_writes_to_the_log() {
    }

    public void UT_StrategyTransferColor_When_transfer_color_succesfully_it_does_not_write_to_the_log() {
    }

    private void setStrategyAndRun(int targetCell, Piece mainPiece, IAction action) {
        stub(fakeBlackboard.getInt(eq(Piece.BLACKBOARD_TARGET_CELL))).toReturn(targetCell);
        
        action.setCharacter(mainPiece);
        
        IBehaviourTreeNode node = ObjectFactory.createObject(IBehaviourTreeNode.class);
        node.setCharacter(mainPiece);
        node.setProbability(1);
        node.setAction(action);
        
        mainPiece.setBehaviourTree(node);
        
        ObjectFactory.installMock(IBlackBoard.class, fakeBlackboard);
        conditionSatisfied = mainPiece.run();
        ObjectFactory.removeMock(IBlackBoard.class);
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
    
    private void verifyEventAddedToFakeEventWriter(String beginswith, int times){
        verify(fakeEventsWriter, times(times)).add(argThat(new ArgumentMatcher<IEvent>() {
            @Override
            public boolean matches(Object item) {
                return ((IEvent) item).toLogicalPredicate().startsWith(beginswith);
            }
        }));
    }
}
