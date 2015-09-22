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
import com.velonuboso.made.core.abm.api.IColorSpot;
import com.velonuboso.made.core.abm.api.IEventsWriter;
import com.velonuboso.made.core.abm.api.IMap;
import com.velonuboso.made.core.abm.api.condition.ICondition;
import com.velonuboso.made.core.abm.api.strategy.IStrategyMoveAway;
import com.velonuboso.made.core.abm.api.strategy.IStrategyMoveOrDisplace;
import com.velonuboso.made.core.abm.api.strategy.IStrategySkipTurn;
import com.velonuboso.made.core.abm.api.strategy.IStrategyStain;
import com.velonuboso.made.core.abm.api.strategy.IStrategyTransferColor;
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
        abmConfigurationEntity = new AbmConfigurationEntity(new float[]{1, 0, 0, 0, 0.5f, 0, 0, 0, 0, 0, 0, 0, 0, 0});
        fakeEventsWriter = mock(IEventsWriter.class);
        map = ObjectFactory.createObject(IMap.class);
        map.initialize(10, 10);
        map.setEventsWriter(fakeEventsWriter);
        ObjectFactory.cleanAllMocks();
        fakeBlackboard = spy(ObjectFactory.createObject(IBlackBoard.class));
    }

    @Test
    public void UT_StrategyMoveAway_When_there_are_empty_cells_it_moves_successfully_to_an_empty_cell() {
        Piece mainPiece = buildPiece(0, CharacterShape.CIRCLE, Color.BLUE, Color.RED, 0, 0);

        int oldCell = (int) map.getCell(mainPiece);
        setStrategyAndRun(-1, mainPiece, ObjectFactory.createObject(IStrategyMoveAway.class));
        assertThat("Piece position should change since there are free cells",
                (int) map.getCell(mainPiece), is(not(oldCell)));
        verifyStrategyReturnedTrue();
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
        assertEquals("New piece's position should be the free position", (int) map.getCell(mainPiece), expectedCell);
        verifyStrategyReturnedTrue();
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

        int oldCell = (int) map.getCell(mainPiece);
        setStrategyAndRun(-1, mainPiece, ObjectFactory.createObject(IStrategyMoveAway.class));
        assertEquals("Piece position should not change since there's no place to go",
                (int) map.getCell(mainPiece), oldCell);
        verifyStrategyReturnedFalse();
    }

    @Test
    public void UT_StrategyMoveAway_When_called_successfully_it_writes_to_the_log() {
        Piece mainPiece = buildPiece(0, CharacterShape.CIRCLE, Color.BLUE, Color.RED, 0, 0);
        setStrategyAndRun(-1, mainPiece, ObjectFactory.createObject(IStrategyMoveAway.class));
        verifyEventAddedToFakeEventWriter(EventFactory.MOVES_AWAY, 1);
        verifyStrategyReturnedTrue();
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
        verifyStrategyReturnedFalse();
    }

    @Test
    public void UT_StrategyMoveOrDisplace_When_the_piece_is_already_in_the_cell_it_fails() {
        Piece mainPiece = buildPiece(0, CharacterShape.CIRCLE, Color.BLUE, Color.RED, 1, 1);

        int targetCell = map.getCell(mainPiece);
        setStrategyAndRun(targetCell, mainPiece, ObjectFactory.createObject(IStrategyMoveOrDisplace.class));
        assertEquals("Piece position should not change since there's no place to go",
                targetCell, (int) map.getCell(mainPiece));
        verifyStrategyReturnedFalse();
    }

    @Test
    public void UT_StrategyMoveOrDisplace_When_the_cell_is_around_and_free_it_moves_sucesfully_to_the_cell() {
        Piece mainPiece = buildPiece(0, CharacterShape.CIRCLE, Color.BLUE, Color.RED, 0, 0);

        int targetCell = map.getCell(1, 1);
        setStrategyAndRun(targetCell, mainPiece, ObjectFactory.createObject(IStrategyMoveOrDisplace.class));
        assertEquals("Piece position should change to target",
                targetCell, (int) map.getCell(mainPiece));
        verifyStrategyReturnedTrue();
    }

    @Test
    public void UT_StrategyMoveOrDisplace_When_the_cell_is_around_and_occupied_by_a_piece_with_free_cells_around_it_is_displaced_and_the_piece_moves_to_the_target() {
        Piece mainPiece = buildPiece(0, CharacterShape.CIRCLE, Color.BLUE, Color.RED, 0, 0);
        Piece targetPiece = buildPiece(0, CharacterShape.TRIANGLE, Color.BLUE, Color.RED, 1, 1);

        int targetCell = map.getCell(1, 1);

        setStrategyAndRun(targetCell, mainPiece, ObjectFactory.createObject(IStrategyMoveOrDisplace.class));

        assertEquals("Piece position should be the target change since the target is occupied by a circle",
                targetCell, (int) map.getCell(mainPiece));
        assertThat("Target piece position should change since it has been displaced",
                map.getCell(targetPiece), is(not(targetCell)));

        verifyStrategyReturnedTrue();
    }

    @Test
    public void UT_StrategyMoveOrDisplace_When_the_cell_is_around_and_occupied_by_a_piece_that_has_no_free_cells_around_it_fails() {
        Piece mainPiece = buildPiece(0, CharacterShape.CIRCLE, Color.BLUE, Color.RED, 0, 0);
        Piece targetPiece = buildPiece(0, CharacterShape.TRIANGLE, Color.BLUE, Color.RED, 1, 1);

        buildPiece(0, CharacterShape.TRIANGLE, Color.BLUE, Color.RED, 0, 1);
        buildPiece(0, CharacterShape.TRIANGLE, Color.BLUE, Color.RED, 0, 2);
        buildPiece(0, CharacterShape.TRIANGLE, Color.BLUE, Color.RED, 1, 0);
        buildPiece(0, CharacterShape.TRIANGLE, Color.BLUE, Color.RED, 1, 2);
        buildPiece(0, CharacterShape.TRIANGLE, Color.BLUE, Color.RED, 2, 0);
        buildPiece(0, CharacterShape.TRIANGLE, Color.BLUE, Color.RED, 2, 1);
        buildPiece(0, CharacterShape.TRIANGLE, Color.BLUE, Color.RED, 2, 2);

        int oldCell = map.getCell(0, 0);
        int targetCell = map.getCell(1, 1);
        setStrategyAndRun(targetCell, mainPiece, ObjectFactory.createObject(IStrategyMoveOrDisplace.class));

        assertEquals("Piece position should not change since the target has no cell to be displaced to",
                oldCell, (int) map.getCell(mainPiece));
        assertThat("Target piece position should not change since it has no cell to be displaced to",
                map.getCell(targetPiece), is(targetCell));
        verifyStrategyReturnedFalse();
    }

    @Test
    public void UT_StrategyMoveOrDisplace_When_the_cell_is_not_around_and_the_closer_cell_is_free_it_moves_successfully_to_the_cell() {
        Piece mainPiece = buildPiece(0, CharacterShape.CIRCLE, Color.BLUE, Color.RED, 0, 0);
        Piece pieceThatIsNotInTheWay = buildPiece(0, CharacterShape.TRIANGLE, Color.BLUE, Color.RED, 0, 1);

        int targetCell = map.getCell(2, 2);
        int closerCell = map.getCell(1, 1);
        int unchangedCell = map.getCell(0, 1);

        setStrategyAndRun(targetCell, mainPiece, ObjectFactory.createObject(IStrategyMoveOrDisplace.class));

        assertEquals("Piece position should be the closer cell to the target since it is not occupied",
                closerCell, (int) map.getCell(mainPiece));
        assertThat("A piece that is not in the way os the mani cell should not be moved",
                map.getCell(pieceThatIsNotInTheWay), is(unchangedCell));

        verifyStrategyReturnedTrue();
    }

    @Test
    public void UT_StrategyMoveOrDisplace_When_the_cell_is_not_around_and_the_closer_cell_is_occupied_by_a_piece_with_free_cells_around_it_is_displaced_and_the_piece_moves_to_the_target() {
        Piece mainPiece = buildPiece(0, CharacterShape.CIRCLE, Color.BLUE, Color.RED, 0, 0);
        Piece closerPiece = buildPiece(0, CharacterShape.TRIANGLE, Color.BLUE, Color.RED, 1, 1);

        int closerCell = map.getCell(1, 1);
        int targetCell = map.getCell(2, 2);

        setStrategyAndRun(targetCell, mainPiece, ObjectFactory.createObject(IStrategyMoveOrDisplace.class));

        assertEquals("Piece position should be the target change since the target is occupied by a circle",
                closerCell, (int) map.getCell(mainPiece));
        assertThat("Target piece position should change since it has been displaced",
                map.getCell(closerPiece), is(not(closerCell)));

        verifyStrategyReturnedTrue();
    }

    @Test
    public void UT_StrategyMoveOrDisplace_When_the_cell_is_not_around_and_the_closer_cell_is_occupied_by_a_piece_that_has_no_free_cells_around_it_fails() {
        Piece mainPiece = buildPiece(0, CharacterShape.CIRCLE, Color.BLUE, Color.RED, 0, 0);
        Piece closerPiece = buildPiece(0, CharacterShape.TRIANGLE, Color.BLUE, Color.RED, 1, 1);

        buildPiece(0, CharacterShape.TRIANGLE, Color.BLUE, Color.RED, 0, 1);
        buildPiece(0, CharacterShape.TRIANGLE, Color.BLUE, Color.RED, 0, 2);
        buildPiece(0, CharacterShape.TRIANGLE, Color.BLUE, Color.RED, 1, 0);
        buildPiece(0, CharacterShape.TRIANGLE, Color.BLUE, Color.RED, 1, 2);
        buildPiece(0, CharacterShape.TRIANGLE, Color.BLUE, Color.RED, 2, 0);
        buildPiece(0, CharacterShape.TRIANGLE, Color.BLUE, Color.RED, 2, 1);
        buildPiece(0, CharacterShape.TRIANGLE, Color.BLUE, Color.RED, 2, 2);

        int closerCell = map.getCell(1, 1);
        int targetCell = map.getCell(2, 2);
        int oldCell = map.getCell(0, 0);

        setStrategyAndRun(targetCell, mainPiece, ObjectFactory.createObject(IStrategyMoveOrDisplace.class));

        assertEquals("Piece position should not change since the target has no cell to be displaced to",
                oldCell, (int) map.getCell(mainPiece));
        assertThat("Target piece position should not change since it has no cell to be displaced to",
                map.getCell(closerPiece), is(closerCell));
        verifyStrategyReturnedFalse();
    }

    @Test
    public void UT_StrategyMoveOrDisplace_When_moves_successfully_it_writes_to_the_log() {
        Piece mainPiece = buildPiece(0, CharacterShape.CIRCLE, Color.BLUE, Color.RED, 0, 0);

        int targetCell = map.getCell(1, 1);
        setStrategyAndRun(targetCell, mainPiece, ObjectFactory.createObject(IStrategyMoveOrDisplace.class));
        verifyEventAddedToFakeEventWriter(EventFactory.MOVES, 1);
        verifyEventAddedToFakeEventWriter(EventFactory.DISPLACES, 0);
        verifyStrategyReturnedTrue();
    }

    @Test
    public void UT_StrategyMoveOrDisplace_When_moves_and_displaces_successfully_it_writes_to_the_log() {
        Piece mainPiece = buildPiece(0, CharacterShape.CIRCLE, Color.BLUE, Color.RED, 0, 0);
        buildPiece(0, CharacterShape.TRIANGLE, Color.BLUE, Color.RED, 1, 1);
        int targetCell = map.getCell(1, 1);

        setStrategyAndRun(targetCell, mainPiece, ObjectFactory.createObject(IStrategyMoveOrDisplace.class));
        verifyEventAddedToFakeEventWriter(EventFactory.MOVES, 1);
        verifyEventAddedToFakeEventWriter(EventFactory.DISPLACES, 1);
        verifyStrategyReturnedTrue();
    }

    @Test
    public void UT_StrategyMoveOrDisplace_When_called_unsuccessfully_it_does_not_write_to_the_log() {
        Piece mainPiece = buildPiece(0, CharacterShape.CIRCLE, Color.BLUE, Color.RED, 0, 0);
        buildPiece(0, CharacterShape.TRIANGLE, Color.BLUE, Color.RED, 1, 1);

        buildPiece(0, CharacterShape.TRIANGLE, Color.BLUE, Color.RED, 0, 1);
        buildPiece(0, CharacterShape.TRIANGLE, Color.BLUE, Color.RED, 0, 2);
        buildPiece(0, CharacterShape.TRIANGLE, Color.BLUE, Color.RED, 1, 0);
        buildPiece(0, CharacterShape.TRIANGLE, Color.BLUE, Color.RED, 1, 2);
        buildPiece(0, CharacterShape.TRIANGLE, Color.BLUE, Color.RED, 2, 0);
        buildPiece(0, CharacterShape.TRIANGLE, Color.BLUE, Color.RED, 2, 1);
        buildPiece(0, CharacterShape.TRIANGLE, Color.BLUE, Color.RED, 2, 2);

        int targetCell = map.getCell(1, 1);
        setStrategyAndRun(targetCell, mainPiece, ObjectFactory.createObject(IStrategyMoveOrDisplace.class));
        verifyEventAddedToFakeEventWriter(EventFactory.MOVES, 0);
        verifyEventAddedToFakeEventWriter(EventFactory.DISPLACES, 0);
        verifyStrategyReturnedFalse();
    }

    @Test
    public void UT_StrategySkipTurn_the_piece_does_not_move_or_stain() {
        Piece mainPiece = buildPiece(0, CharacterShape.CIRCLE, Color.BLUE, Color.RED, 0, 0);

        int oldCell = map.getCell(0, 0);
        setStrategyAndRun(-1, mainPiece, ObjectFactory.createObject(IStrategySkipTurn.class));

        assertEquals("Piece position should not change when skipping turn",
                oldCell, (int) map.getCell(mainPiece));
        verifyStrategyReturnedTrue();
    }

    @Test
    public void UT_StrategySkipTurn_When_called_successfully_it_writes_to_the_log() {
        Piece mainPiece = buildPiece(0, CharacterShape.CIRCLE, Color.BLUE, Color.RED, 0, 0);

        int oldCell = map.getCell(0, 0);
        setStrategyAndRun(-1, mainPiece, ObjectFactory.createObject(IStrategySkipTurn.class));

        verifyEventAddedToFakeEventWriter(EventFactory.SKIPS_TURN, 1);
        verifyStrategyReturnedTrue();
    }

    @Test
    public void UT_StrategyStain_When_the_piece_is_not_over_the_cell_it_fails() {
        Piece mainPiece = buildPiece(0, CharacterShape.CIRCLE, Color.BLUE, Color.RED, 0, 0);

        int targetCell = map.getCell(1, 1);
        setStrategyAndRun(targetCell, mainPiece, ObjectFactory.createObject(IStrategyStain.class));

        assertEquals("Should've stained", mainPiece.getBackgroundColor(), Color.RED);
        verifyStrategyReturnedFalse();
    }

    @Test
    public void UT_StrategyStain_When_the_cell_does_not_have_a_spot() {
        Piece mainPiece = buildPiece(0, CharacterShape.CIRCLE, Color.BLUE, Color.RED, 0, 0);

        int targetCell = map.getCell(0, 0);
        setStrategyAndRun(targetCell, mainPiece, ObjectFactory.createObject(IStrategyStain.class));

        assertEquals("Should've stained", mainPiece.getBackgroundColor(), Color.RED);
        verifyStrategyReturnedFalse();
    }

    @Test
    public void UT_StrategyStain_When_the_cell_is_around_and_has_a_spot_the_piece_fills_its_background_with_the_spot_color_succesfully() {
        Piece mainPiece = buildPiece(0, CharacterShape.CIRCLE, Color.BLUE, Color.RED, 0, 0);
        buildSpot(2, Color.BLUE, 0, 0);

        int targetCell = map.getCell(0, 0);
        setStrategyAndRun(targetCell, mainPiece, ObjectFactory.createObject(IStrategyStain.class));

        assertEquals("Should've stained", mainPiece.getBackgroundColor(), Color.BLUE);
        verifyStrategyReturnedTrue();
    }

    @Test
    public void UT_StrategyStain_When_called_succesfully_and_the_random_value_is_bellow_the_probability_the_spot_disappears() {
        abmConfigurationEntity = new AbmConfigurationEntity(new float[]{1, 0, 0, 0, 0.5f, 0, 0, 0, 0, 0, 0, 0, 0, 1});
        Piece mainPiece = buildPiece(0, CharacterShape.CIRCLE, Color.BLUE, Color.RED, 0, 0);
        buildSpot(2, Color.BLUE, 0, 0);

        int targetCell = map.getCell(0, 0);
        setStrategyAndRun(targetCell, mainPiece, ObjectFactory.createObject(IStrategyStain.class));

        IColorSpot spot = map.getColorSpot(targetCell);
        assertNull("Spot should've disappeared", spot);
        verifyStrategyReturnedTrue();
    }

    @Test
    public void UT_StrategyStain_When_called_succesfully_and_the_random_value_is_over_the_probability_the_spot_remains() {
        Piece mainPiece = buildPiece(0, CharacterShape.CIRCLE, Color.BLUE, Color.RED, 0, 0);
        buildSpot(2, Color.BLUE, 0, 0);

        int targetCell = map.getCell(0, 0);
        setStrategyAndRun(targetCell, mainPiece, ObjectFactory.createObject(IStrategyStain.class));

        IColorSpot spot = map.getColorSpot(targetCell);
        assertNotNull("Spot should've remained", spot);
        verifyStrategyReturnedTrue();
    }

    @Test
    public void UT_StrategyStain_When_stains_successfully_it_writes_to_the_log() {
        Piece mainPiece = buildPiece(0, CharacterShape.CIRCLE, Color.BLUE, Color.RED, 0, 0);
        buildSpot(2, Color.BLUE, 0, 0);

        int targetCell = map.getCell(0, 0);
        setStrategyAndRun(targetCell, mainPiece, ObjectFactory.createObject(IStrategyStain.class));

        verifyEventAddedToFakeEventWriter(EventFactory.STAINS, 1);
        verifyStrategyReturnedTrue();
    }

    @Test
    public void UT_StrategyStain_When_called_unsuccessfully_it_does_not_write_to_the_log() {
        Piece mainPiece = buildPiece(0, CharacterShape.CIRCLE, Color.BLUE, Color.RED, 0, 0);

        int targetCell = map.getCell(0, 0);
        setStrategyAndRun(targetCell, mainPiece, ObjectFactory.createObject(IStrategyStain.class));

        verifyEventAddedToFakeEventWriter(EventFactory.STAINS, 0);
        verifyStrategyReturnedFalse();
    }

    @Test
    public void UT_StrategyTransferColor_When_cell_is_not_around_it_fails() {
        Piece mainPiece = buildPiece(0, CharacterShape.CIRCLE, Color.BLUE, Color.RED, 0, 0);

        int targetCell = map.getCell(2, 2);
        setStrategyAndRun(targetCell, mainPiece, ObjectFactory.createObject(IStrategyTransferColor.class));

        verifyStrategyReturnedFalse();
    }

    @Test
    public void UT_StrategyTransferColor_When_cell_does_not_have_a_piece_it_fails() {
        Piece mainPiece = buildPiece(0, CharacterShape.CIRCLE, Color.BLUE, Color.RED, 0, 0);

        int targetCell = map.getCell(1, 1);
        setStrategyAndRun(targetCell, mainPiece, ObjectFactory.createObject(IStrategyTransferColor.class));

        verifyStrategyReturnedFalse();
    }

    @Test
    public void UT_StrategyTransferColor_When_piece_around_can_win_the_character_it_fails() {
        Piece mainPiece = buildPiece(0, CharacterShape.CIRCLE, Color.BLUE, Color.RED, 0, 0);
        Piece friend = buildPiece(1, CharacterShape.SQUARE, Color.GREEN, Color.YELLOW, 1, 1);

        int targetCell = map.getCell(1, 1);

        setStrategyAndRun(targetCell, mainPiece, ObjectFactory.createObject(IStrategyTransferColor.class));

        assertEquals("character shouln't have changed of color", mainPiece.getBackgroundColor(), Color.RED);
        assertEquals("friend shouln't have changed of color", friend.getBackgroundColor(), Color.YELLOW);

        verifyStrategyReturnedFalse();
    }

    @Test
    public void UT_StrategyTransferColor_When_piece_around_is_a_friend_it_transfers_bachground_colors_succesfully() {
        Piece mainPiece = buildPiece(0, CharacterShape.CIRCLE, Color.BLUE, Color.RED, 0, 0);
        Piece friend = buildPiece(1, CharacterShape.TRIANGLE, Color.GREEN, Color.YELLOW, 1, 1);

        int targetCell = map.getCell(1, 1);

        setStrategyAndRun(targetCell, mainPiece, ObjectFactory.createObject(IStrategyTransferColor.class));

        assertEquals("character shouln't have changed of color", mainPiece.getBackgroundColor(), Color.YELLOW);
        assertEquals("friend shouln't have changed of color", friend.getBackgroundColor(), Color.RED);

        verifyStrategyReturnedTrue();
    }

    @Test
    public void UT_StrategyTransferColor_When_transfer_color_succesfully_it_writes_to_the_log() {
        Piece mainPiece = buildPiece(0, CharacterShape.CIRCLE, Color.BLUE, Color.RED, 0, 0);
        Piece friend = buildPiece(1, CharacterShape.TRIANGLE, Color.GREEN, Color.YELLOW, 1, 1);

        int targetCell = map.getCell(1, 1);

        setStrategyAndRun(targetCell, mainPiece, ObjectFactory.createObject(IStrategyTransferColor.class));
        verifyStrategyReturnedTrue();
        verifyEventAddedToFakeEventWriter(EventFactory.TRANSFERS_COLOR, 1);
    }

    @Test
    public void UT_StrategyTransferColor_When_transfer_color_fails_it_does_not_write_to_the_log() {
        Piece mainPiece = buildPiece(0, CharacterShape.CIRCLE, Color.BLUE, Color.RED, 0, 0);
        int targetCell = map.getCell(1, 1);

        setStrategyAndRun(targetCell, mainPiece, ObjectFactory.createObject(IStrategyTransferColor.class));
        verifyStrategyReturnedFalse();
        verifyEventAddedToFakeEventWriter(EventFactory.TRANSFERS_COLOR, 0);
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

    private void verifyEventAddedToFakeEventWriter(String beginswith, int times) {
        verify(fakeEventsWriter, times(times)).add(argThat(new ArgumentMatcher<IEvent>() {
            @Override
            public boolean matches(Object item) {
                return ((IEvent) item).toLogicalPredicate().startsWith(beginswith);
            }
        }));
    }

    private void verifyStrategyReturnedTrue() {
        assertTrue("Should've returned true", conditionSatisfied);
    }

    private void verifyStrategyReturnedFalse() {
        assertFalse("Should've returned false", conditionSatisfied);
    }
}
