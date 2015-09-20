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
import com.velonuboso.made.core.abm.entity.CharacterShape;
import com.velonuboso.made.core.abm.implementation.BehaviourTreeNode;
import com.velonuboso.made.core.abm.implementation.ColorSpot;
import com.velonuboso.made.core.abm.implementation.piece.Piece;
import com.velonuboso.made.core.common.api.IEvent;
import com.velonuboso.made.core.common.entity.AbmConfigurationEntity;
import com.velonuboso.made.core.common.implementation.EventFactory;
import com.velonuboso.made.core.common.util.ObjectFactory;
import javafx.scene.paint.Color;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;
import org.mockito.ArgumentMatcher;
import static org.mockito.Mockito.*;

/**
 *
 * @author Rubén Héctor García (raiben@gmail.com)
 */
public class StrategiesTest {
    public void UT_StrategyMoveAway_When_there_are_empty_cells_it_moves_successfully_to_a_empty_cell(){}
    public void UT_StrategyMoveAway_When_there_is_only_one_empty_cell_it_moves_successfully_to_the_cell(){}
    public void UT_StrategyMoveAway_When_there_is_no_empty_cell_it_fails(){}
    
    public void UT_StrategyMoveAway_When_called_successfully_it_writes_to_the_log(){}
    public void UT_StrategyMoveAway_When_called_unsuccessfully_it_does_not_write_to_the_log(){}
    
    public void UT_StrategyMoveOrDisplace_When_the_piece_is_already_in_the_cell_it_fails(){}
    public void UT_StrategyMoveOrDisplace_When_the_cell_is_around_and_free_it_moves_sucesfully_to_the_cell(){}
    public void UT_StrategyMoveOrDisplace_When_the_cell_is_around_and_occupied_by_a_piece_that_can_be_won_it_displaces_the_piece_and_moves_to_the_cell(){}
    public void UT_StrategyMoveOrDisplace_When_the_cell_is_around_and_occupied_by_a_piece_that_can_be_won_but_is_not__enemy_it_fails(){}
    public void UT_StrategyMoveOrDisplace_When_the_cell_is_around_and_occupied_by_a_piece_that_can_be_won_but_does_not_have_free_cells_around_it_fails(){}    
    public void UT_StrategyMoveOrDisplace_When_the_cell_is_around_and_occupied_by_a_piece_that_cannot_be_won_it_fails(){}
    public void UT_StrategyMoveOrDisplace_When_the_cell_is_not_around_and_the_closer_cell_is_free_it_moves_successfully_to_the_piece(){}
    public void UT_StrategyMoveOrDisplace_When_the_cell_is_not_around_and_the_closer_cell_is_occupied_by_a_piece_that_can_be_won_it_displaces_the_piece_and_moves_to_the_cell(){}
    public void UT_StrategyMoveOrDisplace_When_the_cell_is_not_around_and_the_closer_cell_is_occupied_by_a_piece_that_can_be_won_but_is_not_enemy_it_fails(){}
    public void UT_StrategyMoveOrDisplace_When_the_cell_is_not_around_and_the_closer_cell_is_occupied_by_a_piece_that_can_be_won_but_does_not_have_free_cells_around_it_fails(){}
    public void UT_StrategyMoveOrDisplace_When_the_cell_is_not_around_and_the_closer_cell_is_occupied_by_a_piece_that_cannot_be_won_it_fails(){}

    public void UT_StrategyMoveOrDisplace_When_moves_successfully_it_writes_to_the_log(){}
    public void UT_StrategyMoveOrDisplace_When_moves_and_displaces_successfully_it_writes_to_the_log(){}
    public void UT_StrategyMoveOrDisplace_When_called_unsuccessfully_it_does_not_write_to_the_log(){}
    
    public void UT_StrategySkipTurn_the_piece_does_not_move_or_stain(){}
    
    public void UT_StrategySkipTurn_When_called_successfully_it_writes_to_the_log(){}
    
    public void UT_StrategyStain_When_the_cell_is_not_around_it_fails(){}
    public void UT_StrategyStain_When_the_cell_does_not_have_a_spot(){}
    public void UT_StrategyStain_When_the_cell_is_around_and_has_a_spot_the_piece_fills_its_background_with_the_spot_color_succesfully(){}
    public void UT_StrategyStain_When_called_succesfully_and_the_random_value_is_bellow_the_probability_the_spot_dissapears(){}
    public void UT_StrategyStain_When_called_succesfully_and_the_random_value_is_over_the_probability_the_spot_remains(){}
    
    public void UT_StrategyStain_When_stains_successfully_it_writes_to_the_log(){}
    public void UT_StrategyStain_When_the_spot_dissapears_it_writes_to_the_log(){}
    public void UT_StrategyStain_When_called_unsuccessfully_it_does_not_write_to_the_log(){}
    
    public void UT_StrategyTransferColor_When_cell_is_not_around_it_fails(){}
    public void UT_StrategyTransferColor_When_cell_does_not_have_a_piece_it_fails(){}
    public void UT_StrategyTransferColor_When_piece_around_is_an_enemy_it_fails(){}
    public void UT_StrategyTransferColor_When_piece_around_is_a_friend_but_cannot_be_won_it_fails(){}
    public void UT_StrategyTransferColor_When_piece_around_is_a_friend_it_transfers_bachground_colors_succesfully(){}
    
    public void UT_StrategyTransferColor_When_transfer_color_succesfully_it_writes_to_the_log(){}
    public void UT_StrategyTransferColor_When_transfer_color_succesfully_it_does_not_write_to_the_log(){}
}
