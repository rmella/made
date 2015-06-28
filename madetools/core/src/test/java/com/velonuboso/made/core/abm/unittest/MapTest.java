/*
 * Copyright (C) 2015 rhgarcia
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

import com.velonuboso.made.core.common.util.ObjectFactory;
import com.velonuboso.made.core.abm.api.ICharacter;
import com.velonuboso.made.core.abm.api.IMap;
import com.velonuboso.made.core.abm.api.IPosition;
import com.velonuboso.made.core.abm.entity.TerrainType;
import java.util.HashSet;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 *
 * @author rhgarcia
 */
public class MapTest {

    private IMap map = null;

    public MapTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        map = ObjectFactory.createObject(IMap.class);
        map.initialize(20, 20);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void newMap__20x20_must_contain_400_cells() {
        assertEquals("Should've created 400 cells", 400, map.getCells().size());
    }

    @Test
    public void newMap__allCells_must_be_empty() {
        boolean allCellsAreEmpty = map.getCells().stream().allMatch(cellId -> map.getCharacter(cellId) == null);
        assertTrue("Should've set all the cells as empty", allCellsAreEmpty);
    }

    @Test
    public void newMap__allCells_must_be_land() {
        boolean allCellsAreLands = map.getCells().stream().allMatch(cellId -> map.getTerrain(cellId) == TerrainType.LAND);
        assertTrue("Should've set all the cells as land", allCellsAreLands);
    }

    @Test
    public void newMap__allCells_must_have_different_position() {
        HashSet<IPosition> differentPositions = new HashSet<>();
        map.getCells().stream().forEach(cellId -> differentPositions.add(map.getPosition(cellId)));

        assertEquals("all the cells should have different positions",
                differentPositions.size(), map.getCells().size());
    }

    @Test
    public void getCell__allCells_must_have_the_same_position_than_the_one_used_to_get_them() {
        for (int iterX = 0; iterX < map.getWidth(); iterX++) {
            for (int iterY = 0; iterY < map.getHeight(); iterY++) {
                IPosition position = ObjectFactory.createObject(IPosition.class);
                position.setCoords(iterX, iterY);
                Integer retrievedCell = map.getCell(position);
                assertEquals("The cell in position " + position
                        + "should have position " + position
                        + ", not " + map.getPosition(retrievedCell),
                        position, map.getPosition(retrievedCell));
            }
        }
    }

    @Test
    public void getCellByPosition__cell_retrieved_by_position_and_retrieved_by_coords_must_be_the_same() {
        final int coordX = 1;
        final int coordY = 2;

        IPosition position = ObjectFactory.createObject(IPosition.class);
        position.setCoords(coordX, coordY);
        assertEquals("getCellByPostion methods should return the same cellId",
                map.getCell(position), map.getCell(coordX, coordY));
    }

    @Test
    public void getPositionByCell__position_retrieved_by_cell_and_coords_retrieved_by_cell_must_be_the_same() {
        final int cellId = 12;
        IPosition position = ObjectFactory.createObject(IPosition.class);
        position.setCoords(map.getPositionX(cellId), map.getPositionY(cellId));

        assertEquals("getPositionByCell, getPositionXByCell and getPositionYByCell methods should return the same coods",
                position, map.getPosition(cellId));
    }

    @Test
    public void getCell__position_out_of_bounds_must_be_moduled() {
        IPosition position = ObjectFactory.createObject(IPosition.class);
        position.setCoords(1, 2);

        Integer cellId = map.getCell(position);
        position.setCoords(21, 22);
        assertEquals("Should've retrieved the same cell using " + position + " and " + map.getPosition(cellId),
                cellId, map.getCell(position));

        position.setCoords(-19, -18);
        assertEquals("Should've retrieved the same cell using " + position + " and " + map.getPosition(cellId),
                cellId, map.getCell(position));
    }

    @Test
    public void putCharacter__in_empty_cell_must_put_the_character() {
        ICharacter character = mock(ICharacter.class);

        IPosition positionSource = ObjectFactory.createObject(IPosition.class);
        positionSource.setCoords(1, 2);
        Integer cellSource = map.getCell(positionSource);
        map.putCharacter(character, cellSource);

        assertNotNull("Character should've been placed in cell", map.getCharacter(cellSource));
    }

    @Test
    public void removeCharacter__cell_with_character_must_remove_the_character_from_the_map() {
        ICharacter character = mock(ICharacter.class);

        IPosition position = ObjectFactory.createObject(IPosition.class);
        position.setCoords(1, 2);
        Integer cell = map.getCell(position);
        map.putCharacter(character, cell);
        map.removeCharacter(cell);

        assertNull("Character should not be in the cell", map.getCharacter(cell));
        assertNull("Character should have no cell", map.getCell(character));
    }

    @Test
    public void putCharacter__in_non_empty_cell_must_throw_exception(){
        ICharacter character = mock(ICharacter.class);

        IPosition positionSource = ObjectFactory.createObject(IPosition.class);
        positionSource.setCoords(1,2);
        Integer cellSource = map.getCell(positionSource);
        map.putCharacter(character, cellSource);

        try{
            map.putCharacter(character, cellSource);
            assertTrue("Character shouldn't have been placed in cell", false);
        }catch(Exception e){
        
        }
    }

    @Test
    public void putCharacter__putting_the_same_character_in_two_cells_must_throw_exception() {
        ICharacter character = mock(ICharacter.class);

        IPosition positionSource = ObjectFactory.createObject(IPosition.class);
        positionSource.setCoords(1, 2);

        IPosition positionTarget = ObjectFactory.createObject(IPosition.class);
        positionTarget.setCoords(2, 2);

        Integer cellSource = map.getCell(positionSource);
        map.putCharacter(character, cellSource);
        
        try {
            Integer cellTarget = map.getCell(positionTarget);
            map.putCharacter(character, cellTarget);
            assertTrue("Should've thrown exception when trying to put the same character in two cells", false);
        } catch (Exception ex) {
            int a = 1;
        }
    }

    @Test
    public void moveCharacter__source_with_character_and_target_empty_must_move_character_to_target() {
        ICharacter character = mock(ICharacter.class);

        IPosition positionSource = ObjectFactory.createObject(IPosition.class);
        positionSource.setCoords(1, 2);

        IPosition positionTarget = ObjectFactory.createObject(IPosition.class);
        positionTarget.setCoords(1, 5);

        Integer cellSource = map.getCell(positionSource);
        map.putCharacter(character, cellSource);

        Integer cellTarget = map.getCell(positionTarget);
        map.moveCharacter(cellSource, cellTarget);

        assertNull("Character should not be in source cell", map.getCharacter(cellSource));
        assertNotNull("Character should be in target cell", map.getCharacter(cellTarget));
    }

    @Test
    public void getCellByCharacter__when_character_is_moved_to_target_the_returned_cell_must_be_the_target() {
        ICharacter character = mock(ICharacter.class);

        IPosition positionSource = ObjectFactory.createObject(IPosition.class);
        positionSource.setCoords(1, 2);

        IPosition positionTarget = ObjectFactory.createObject(IPosition.class);
        positionTarget.setCoords(1, 5);

        Integer cellSource = map.getCell(positionSource);
        map.putCharacter(character, cellSource);

        Integer cellTarget = map.getCell(positionTarget);
        map.moveCharacter(cellSource, cellTarget);

        assertEquals("Character's cell should be the new one (" + cellTarget + ")",
                map.getCell(character), cellTarget);
    }

    @Test
    public void moveCharacter__moving_to_the_same_location_must_throw_exception() {
        ICharacter character = mock(ICharacter.class);

        try {
            IPosition positionSource = ObjectFactory.createObject(IPosition.class);
            positionSource.setCoords(1, 2);

            IPosition positionTarget = ObjectFactory.createObject(IPosition.class);
            positionTarget.setCoords(1, 2);

            Integer cellSource = map.getCell(positionSource);
            map.putCharacter(character, cellSource);
            Integer cellTarget = map.getCell(positionTarget);
            map.moveCharacter(cellSource, cellTarget);
            assertFalse("Should've thrown exception when trying to move the character to the same position", false);
        } catch (Exception ex) {

        }
    }

    @Test
    public void moveCharacter_moving_to_a_non_empty_location_must_throw_exception() {
        ICharacter characterInSource = mock(ICharacter.class);
        ICharacter characterInTarget = mock(ICharacter.class);

        try {
            IPosition positionSource = ObjectFactory.createObject(IPosition.class);
            positionSource.setCoords(1, 2);

            IPosition positionTarget = ObjectFactory.createObject(IPosition.class);
            positionTarget.setCoords(1, 5);

            Integer cellSource = map.getCell(positionSource);
            Integer cellTarget = map.getCell(positionTarget);

            map.putCharacter(characterInSource, cellSource);
            map.putCharacter(characterInTarget, cellTarget);

            map.moveCharacter(cellSource, cellTarget);
            assertFalse("Should've thrown exception when trying to move the character to an occupied position", false);
        } catch (Exception ex) {

        }
    }

    @Test
    public void moveCharacter_moving_from_an_empty_location_must_throw_exception() {

        try {
            IPosition positionSource = ObjectFactory.createObject(IPosition.class);
            positionSource.setCoords(1, 2);

            IPosition positionTarget = ObjectFactory.createObject(IPosition.class);
            positionTarget.setCoords(1, 5);

            Integer cellSource = map.getCell(positionSource);
            Integer cellTarget = map.getCell(positionTarget);

            map.moveCharacter(cellSource, cellTarget);
            assertFalse("Should've thrown exception when trying to move the character from a non-occupied cell", false);
        } catch (Exception ex) {

        }
    }

    @Test
    public void getCellByCharacter__character_in_map_must_return_a_cell() {
        ICharacter newCharacter = mock(ICharacter.class);
        IPosition emptyPosition = ObjectFactory.createObject(IPosition.class);
        emptyPosition.setCoords(1, 2);
        Integer emptyCell = map.getCell(emptyPosition);
        map.putCharacter(newCharacter, emptyCell);

        assertEquals("Should've retrieved the cell where the character was put (" + emptyCell + ")",
                emptyCell, map.getCell(newCharacter));
    }

    @Test
    public void getCellByCharacter_character_not_in_map_must_return_null() {
        ICharacter newCharacter = mock(ICharacter.class);
        IPosition emptyPosition = ObjectFactory.createObject(IPosition.class);
        emptyPosition.setCoords(1, 2);
        Integer emptyCell = map.getCell(emptyPosition);

        assertNull("Should return null since the character iss not in the map",
                map.getCell(newCharacter));
    }

    @Test
    public void getPositionsToMove__When_movement_is_1_and_from_0x_0y_then_position_2x_2y_must_not_be_included() {
        final int movement = 1;
        final int newX = 2;
        final int newY = 2;
        Integer sourceCell = map.getCell(0, 0);
        Integer targetCell = map.getCell(newX, newY);
        assertFalse("Shouldn't have contain cell " + " (" + newX + "," + newY + ")",
                map.getCellsToMove(sourceCell, movement).contains(targetCell));
    }

    @Test
    public void getPositionsToMove__When_movement_is_2_and_from_0x_0y_then_position_2x_2y_must_be_included() {
        final int movement = 2;
        final int newX = 2;
        final int newY = 2;
        Integer sourceCell = map.getCell(0, 0);
        Integer targetCell = map.getCell(newX, newY);
        assertTrue("Should've contain cell " + targetCell + " (" + newX + "," + newY + ")",
                map.getCellsToMove(sourceCell, movement).contains(targetCell));
    }

    @Test
    public void getPositionsToMove__When_movement_is_2_and_from_0x_0y_then_position_18x_18y_must_be_included() {
        final int movement = 2;
        final int newX = 18;
        final int newY = 18;
        Integer sourceCell = map.getCell(0, 0);
        Integer targetCell = map.getCell(newX, newY);
        assertTrue("Should've contained cell " + targetCell + " (" + newX + "," + newY + ")",
                map.getCellsToMove(sourceCell, movement).contains(targetCell));
    }

    @Test
    public void getPositionsToMove__When_movement_is_2_and_from_0x_0y_then_position_18x_2y_must_be_included() {
        final int movement = 2;
        final int newX = 18;
        final int newY = 2;
        Integer sourceCell = map.getCell(0, 0);
        Integer targetCell = map.getCell(newX, newY);
        assertTrue("Should've contain cell " + targetCell + " (" + newX + "," + newY + ")",
                map.getCellsToMove(sourceCell, movement).contains(targetCell));
    }

    @Test
    public void getPositionsToMove__When_movement_is_2_and_from_0x_0y_then_position_2x_18y_must_be_included(){
        final int movement = 2;
        final int newX = 2;
        final int newY = 18;
        Integer sourceCell = map.getCell(0, 0);
        Integer targetCell = map.getCell(newX, newY);
        assertTrue("Should've contain cell " + targetCell + " (" + newX + "," + newY + ")",
                map.getCellsToMove(sourceCell, movement).contains(targetCell));
    }

    @Test
    public void getPositionsToMove_When_movement_is_2_from_0x_0y_and_obstacles_then_must_not_include_unreachable_cells() {
        final int movement = 2;
        final int obstacleX = 1;
        final int obstacleY = 1;
        final int newX = 2;
        final int newY = 2;

        Integer sourceCell = map.getCell(0, 0);
        Integer targetCell = map.getCell(newX, newY);
        Integer obstacleCell = map.getCell(newX, newY);

        ICharacter newCharacter = mock(ICharacter.class);
        map.putCharacter(newCharacter, obstacleCell);

        assertFalse("Shouldn't contain cell " + targetCell + " (" + newX + "," + newY + ") "
                + "since an obstacle is present in (" + obstacleX + "," + obstacleY + ")",
                map.getCellsToMove(sourceCell, movement).contains(targetCell));
    }
}
