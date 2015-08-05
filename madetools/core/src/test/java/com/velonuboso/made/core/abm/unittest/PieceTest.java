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
import com.velonuboso.made.core.abm.entity.CharacterShape;
import com.velonuboso.made.core.abm.api.IEventsWriter;
import com.velonuboso.made.core.abm.api.IMap;
import com.velonuboso.made.core.abm.implementation.Position;
import com.velonuboso.made.core.abm.implementation.piece.Piece;
import com.velonuboso.made.core.common.entity.AbmConfigurationEntity;
import com.velonuboso.made.core.common.util.ObjectFactory;
import java.util.HashMap;
import javafx.scene.AmbientLight;
import javafx.scene.paint.Color;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.ArgumentMatcher;
import static org.mockito.Mockito.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

/**
 *
 * @author Rubén Héctor García (raiben@gmail.com)
 */
public class PieceTest {

    private Piece character;
    private ICharacter fakeSquareNeighbor;
    private ICharacter fakeCircleNeighbor;

    IBlackBoard fakeBlackBoard;
    IMap fakeMap;
    IEventsWriter fakeEventsWriter;

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
        initializeFakeSquareNeighbor();
        initializeFakeCircleNeighbor();
        buildFakeBlackboard();
        buildFakeEventsWriter();
        initializeCharacter();
        buildFakeMap();
        character.setMap(fakeMap);
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

    @Test
    public void UT_execute_in_a_map_with_3_characters_must_write_an_affinity_matrix_with_two_elements_in_blacboard() {
        final int EXPECTED_AGENTS_IN_AFFINITY_MATRIX = 2;
        character.run();
        verify(fakeBlackBoard).setObject(eq(Piece.BLACKBOARD_AFFINITY_MATRIX),
                argThat(new BaseMatcher<Object>() {
                    @Override
                    public boolean matches(Object argument) {
                        HashMap<ICharacter, Float> affinityMatrix = (HashMap<ICharacter, Float>) argument;
                        return affinityMatrix.size() == EXPECTED_AGENTS_IN_AFFINITY_MATRIX;
                    }

                    @Override
                    public void describeTo(Description description) {
                        description.appendText("Hasmap with " + EXPECTED_AGENTS_IN_AFFINITY_MATRIX + " elements");
                    }
                }));
    }

    @Test
    public void UT_execute_must_define_affinity_matrix_with_proper_values() {
        final float EXPECTED_AFFINITY_WITH_FAKE_CIRCLE = -0.5363f;
        final float EXPECTED_AFFINITY_WITH_FAKE_SQUARE = -0.3333f;
        final HashMap<ICharacter, Float> affinityMatrix = new HashMap<>();

        doAnswer((Answer) (InvocationOnMock invocation) -> {
            HashMap<ICharacter, Float> inserttedAffinityMatrix = (HashMap<ICharacter, Float>) invocation.getArguments()[1];
            affinityMatrix.putAll(inserttedAffinityMatrix);
            return null;
        }).when(fakeBlackBoard).setObject(eq(Piece.BLACKBOARD_AFFINITY_MATRIX), anyObject());

        character.run();

        assertEquals("Should've had an affinity of " + EXPECTED_AFFINITY_WITH_FAKE_CIRCLE  + " with the circle",
                EXPECTED_AFFINITY_WITH_FAKE_CIRCLE, affinityMatrix.get(fakeCircleNeighbor), 0.0001f);
        assertEquals("Should've had an affinity of " + EXPECTED_AFFINITY_WITH_FAKE_SQUARE  + " with the square",
                EXPECTED_AFFINITY_WITH_FAKE_SQUARE, affinityMatrix.get(fakeSquareNeighbor), 0.0001f);
    }

    @Test
    public void UT_execute_affinity_must_be_minus1_when_enemy_shape_is_different_and_colors_opposite() {
        final float EXPECTED_AFFINITY = -1;
        
        float affinity = getAffinityForCharacter(new float[]{1f,1f,1f}, Color.RED.invert(), 
                Color.BLUE.invert(), CharacterShape.SQUARE);
        
        assertEquals("Should've had an affinity of " + EXPECTED_AFFINITY  + " with the enemy since"
                + " the colours are inverted and the shape differs",
                EXPECTED_AFFINITY, affinity, 0.0001f);
    }
    
    @Test
    public void UT_execute_affinity_must_be_1_when_enemy_shape__and_colors_are_the_same() {
        final float EXPECTED_AFFINITY = 1;
        
        float affinity = getAffinityForCharacter(new float[]{1f,1f,1f}, Color.RED, 
                Color.BLUE, CharacterShape.TRIANGLE);
        
        assertEquals("Should've had an affinity of " + EXPECTED_AFFINITY  + " with the friend since"
                + " the shape and colors are the same",
                EXPECTED_AFFINITY, affinity, 0.0001f);
    }
    
    @Test
    public void UT_execute_affinity_must_be_0_when_configuration_is_all_0() {
        final float EXPECTED_AFFINITY = 0;
        
        float affinity = getAffinityForCharacter(new float[]{0f,0f,0f}, Color.RED, 
                Color.BLUE, CharacterShape.TRIANGLE);
        
        assertEquals("Should've had an affinity of " + EXPECTED_AFFINITY  + " with the friend since"
                + " the shape and colors are the same but the configuration is 0 for all the genes",
                EXPECTED_AFFINITY, affinity, 0.0001f);
    }
    
    @Test
    public void UT_execute_affinity_must_be_minus_1_divided_by_3_when_only_foreground_color_is_the_same() {
        final float EXPECTED_AFFINITY = -1f/3f;
        
        float affinity = getAffinityForCharacter(new float[]{1f,1f,1f}, Color.RED.invert(), 
                Color.BLUE, CharacterShape.CIRCLE);
        
        assertEquals("Should've had an affinity of " + EXPECTED_AFFINITY  + " with the character since"
                + " only foreground is the same",
                EXPECTED_AFFINITY, affinity, 0.0001f);
    }
    
    private float getAffinityForCharacter (float []configurationValues, 
            Color background, Color foreground, CharacterShape shape){
        
        final int FAKE_CHARACTER_ID = 2;
        final int FAKE_CHARACTER_CELL = 22;
        final AbmConfigurationEntity configuration = new AbmConfigurationEntity(configurationValues);
        final HashMap<ICharacter, Float> affinityMatrix = new HashMap<>();

        character.setAbmConfiguration(configuration);
        
        ICharacter fakeCharacter = mock(ICharacter.class);
        stub(fakeCharacter.getBackgroundColor()).toReturn(background);
        stub(fakeCharacter.getForegroundColor()).toReturn(foreground);
        stub(fakeCharacter.getShape()).toReturn(shape);
        stub(fakeCharacter.getId()).toReturn(FAKE_CHARACTER_ID);
        
        stub(fakeMap.getCharacter(eq(FAKE_CHARACTER_CELL))).toReturn(fakeCharacter);
        stub(fakeMap.getCharacter(eq(0))).toReturn(null);
        stub(fakeMap.getCharacter(eq(1))).toReturn(character);
        stub(fakeMap.getCharacter(eq(99))).toReturn(null);

        doAnswer((Answer) (InvocationOnMock invocation) -> {
            HashMap<ICharacter, Float> inserttedAffinityMatrix = (HashMap<ICharacter, Float>) invocation.getArguments()[1];
            affinityMatrix.putAll(inserttedAffinityMatrix);
            return null;
        }).when(fakeBlackBoard).setObject(eq(Piece.BLACKBOARD_AFFINITY_MATRIX), anyObject());

        character.run();
        return affinityMatrix.get(fakeCharacter);
    }
    
    
    private void initializeCharacter() {
        ObjectFactory.installMock(IBlackBoard.class, fakeBlackBoard);
        character = new Piece();

        character.setEventsWriter(fakeEventsWriter);
        character.setId(0);
        character.setBackgroundColor(Color.RED);
        character.setForegroundColor(Color.BLUE);
        character.setShape(CharacterShape.TRIANGLE);
        
        AbmConfigurationEntity abmConfiguration = new AbmConfigurationEntity(
                new float[]{0.5f, 0.5f, 0.5f}
        );
        character.setAbmConfiguration(abmConfiguration);

        ObjectFactory.removeMock(IBlackBoard.class);
    }

    private void buildFakeEventsWriter() {
        fakeEventsWriter = mock(IEventsWriter.class);
    }

    private void buildFakeMap() {
        fakeMap = mock(IMap.class);
        stub(fakeMap.getWidth()).toReturn(10);
        stub(fakeMap.getHeight()).toReturn(10);
        stub(fakeMap.getCharacter(eq(0))).toReturn(fakeSquareNeighbor);
        stub(fakeMap.getCharacter(eq(1))).toReturn(character);
        stub(fakeMap.getCharacter(eq(99))).toReturn(fakeCircleNeighbor);
    }

    private void buildFakeBlackboard() {
        fakeBlackBoard = mock(IBlackBoard.class);
    }

    private void initializeFakeSquareNeighbor() {
        fakeSquareNeighbor = mock(ICharacter.class);
        stub(fakeSquareNeighbor.getBackgroundColor()).toReturn(Color.BLUE);
        stub(fakeSquareNeighbor.getForegroundColor()).toReturn(Color.BLACK);
        stub(fakeSquareNeighbor.getShape()).toReturn(CharacterShape.SQUARE);
        stub(fakeSquareNeighbor.getId()).toReturn(1);
    }

    private void initializeFakeCircleNeighbor() {
        fakeCircleNeighbor = mock(ICharacter.class);
        stub(fakeCircleNeighbor.getBackgroundColor()).toReturn(Color.BLUE);
        stub(fakeCircleNeighbor.getForegroundColor()).toReturn(Color.ALICEBLUE);
        stub(fakeCircleNeighbor.getShape()).toReturn(CharacterShape.CIRCLE);
        stub(fakeCircleNeighbor.getId()).toReturn(2);
    }
}
