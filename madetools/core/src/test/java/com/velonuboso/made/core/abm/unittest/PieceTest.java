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
import com.velonuboso.made.core.abm.implementation.piece.Piece;
import javafx.scene.paint.Color;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Rubén Héctor García (raiben@gmail.com)
 */
public class PieceTest {

    private Piece character;

    public PieceTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        character = new Piece();
    }

    @After
    public void tearDown() {
    }

    @Test
    public void UT_GetBehaviourTree_must_return_a_valid_instance() {
        IBehaviourTreeNode bt = character.getBehaviourTree();
        assertNotNull("BehaviourTree must have a valid instance", bt);
    }

    @Test
    public void UT_GetColorDifference_must_return_0_when_background_is_equal_to_foreground() {
        final float EXPECTED_DIFFERENCE = 0f;

        character.setBackgroundColor(Color.AQUAMARINE);
        character.setForegroundColor(Color.AQUAMARINE);

        assertEquals("Difference should be " + EXPECTED_DIFFERENCE + " when background and foreground colors are equals",
                EXPECTED_DIFFERENCE, character.getColorDifference(), 0f);
    }

    @Test
    public void UT_GetColorDifference_must_return_1_when_background_is_white_and_foreground_is_black() {
        final float EXPECTED_DIFFERENCE = 1f;

        character.setBackgroundColor(Color.WHITE);
        character.setForegroundColor(Color.BLACK);

        assertEquals("Difference should be " + EXPECTED_DIFFERENCE + " when background and foreground colors are the opposite",
                EXPECTED_DIFFERENCE, character.getColorDifference(), 0f);
    }

    @Test
    public void UT_GetColorDifference_must_return_0_comma_6_when_background_is_red_and_foreground_is_blue() {
        final float EXPECTED_DIFFERENCE = 2f / 3f;

        character.setBackgroundColor(Color.RED);
        character.setForegroundColor(Color.BLUE);

        assertEquals("Difference should be " + EXPECTED_DIFFERENCE + " when background is red and foreground is blue",
                EXPECTED_DIFFERENCE, character.getColorDifference(), 0f);
    }
}
