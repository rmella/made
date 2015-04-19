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
package made.velonuboso.made.core;

import com.velonuboso.made.core.ObjectFactory;
import com.velonuboso.made.interfaces.ICharacter;
import com.velonuboso.made.interfaces.IMap;
import com.velonuboso.made.interfaces.IPosition;
import com.velonuboso.made.interfaces.Terrain;
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
    public void newMap20x20_mustContain400Cells() {
        assertEquals("Should've created 400 cells", 400, map.getCells().size());
    }

    @Test
    public void newMap_allCells_mustBeEmpty() {
        boolean allCellsAreEmpty = map.getCells().stream().allMatch(cellId -> map.getCharacter(cellId) == null);
        assertTrue("Should've set all the cells as empty", allCellsAreEmpty);
    }

    @Test
    public void newMap_allCells_mustBeLand() {
        boolean allCellsAreLands = map.getCells().stream().allMatch(cellId -> map.getTerrain(cellId) == Terrain.LAND);
        assertTrue("Should've set all the cells as land", allCellsAreLands);
    }

    @Test
    public void newMap_allCells_mustHaveDifferentPosition() {
        HashSet<IPosition> differentPositions = new HashSet<>();
        map.getCells().stream().forEach(cellId -> differentPositions.add(map.getPosition(cellId)));

        assertEquals("all the cells should have different positions", 
                differentPositions.size(), map.getCells().size());
    }

    @Test
    public void getCell_allCells_mustHaveTheSamePositionThanTheOneUsedToGetThem() {
        for (int iterX = 0; iterX < map.getWidthInCells(); iterX++) {
            for (int iterY = 0; iterY < map.getHeightInCells(); iterY++) {
                IPosition position = ObjectFactory.createObject(IPosition.class);
                position.setCoords(iterX, iterY);
                Integer retrievedCell = map.getCellByPosition(position);
                assertEquals("The cell in position " + position
                        + "should have position " + position
                        + ", not " + map.getPosition(retrievedCell),
                        position, map.getPosition(retrievedCell));
            }
        }
    }
    
    @Test
    public void getCell_PositionOfOutBounds_MustBeModuled(){
        IPosition position = ObjectFactory.createObject(IPosition.class);
        position.setCoords(1,2);
        
        Integer cellId = map.getCellByPosition(position);
        position.setCoords(21, 22);
        assertEquals("Should've retrieved the same cell using "+position+" and "+map.getPosition(cellId),
                cellId, map.getCellByPosition(position));
        
        position.setCoords(-19, -18);
        assertEquals("Should've retrieved the same cell using "+position+" and "+map.getPosition(cellId),
                cellId, map.getCellByPosition(position));
    }
    
    @Test
    public void putCharacter_inEmptyCell_mustPutTheCharacter(){
        ICharacter character = mock(ICharacter.class);
        
        IPosition positionSource = ObjectFactory.createObject(IPosition.class);
        positionSource.setCoords(1,2);
        Integer cellSource = map.getCellByPosition(positionSource);
        map.putCharacter(character, cellSource);
        
        assertNotNull("Character should've been placed in cell", map.getCharacter(cellSource)); 
    }
    
    @Test
    public void putCharacter_inNonEmptyCell_mustThrowException(){
        ICharacter character = mock(ICharacter.class);
        
        IPosition positionSource = ObjectFactory.createObject(IPosition.class);
        positionSource.setCoords(1,2);
        Integer cellSource = map.getCellByPosition(positionSource);
        map.putCharacter(character, cellSource);
        
        try{
            map.putCharacter(character, cellSource);
            assertTrue("Character shouldn't have been placed in cell", false); 
        }catch(Exception e){
            
        }
    }
    
    @Test
    public void moveCharacter_SourceWithCharacterAndTargetEmpty_mustMoveCharacterToTarget(){
        ICharacter character = mock(ICharacter.class);
        
        IPosition positionSource = ObjectFactory.createObject(IPosition.class);
        positionSource.setCoords(1,2);
        
        IPosition positionTarget = ObjectFactory.createObject(IPosition.class);
        positionTarget.setCoords(1,5);
        
        Integer cellSource = map.getCellByPosition(positionSource);
        map.putCharacter(character, cellSource);
        
        Integer cellTarget = map.getCellByPosition(positionTarget);
        map.moveCharacter(cellSource, cellTarget);
        
        assertNull("Character should not be in source cell", map.getCharacter(cellSource));
        assertNotNull("Character should be in target cell", map.getCharacter(cellTarget));   
    }
    
    @Test
    public void getCellByCharacter_moveCharacterToTarget_returnedCellMustBeTarget(){
        ICharacter character = mock(ICharacter.class);
        
        IPosition positionSource = ObjectFactory.createObject(IPosition.class);
        positionSource.setCoords(1,2);
        
        IPosition positionTarget = ObjectFactory.createObject(IPosition.class);
        positionTarget.setCoords(1,5);
        
        Integer cellSource = map.getCellByPosition(positionSource);
        map.putCharacter(character, cellSource);
        
        Integer cellTarget = map.getCellByPosition(positionTarget);
        map.moveCharacter(cellSource, cellTarget);
        
        assertEquals("Character's cell should be the new one ("+cellTarget+")",
                map.getCellByCharacter(character));
    }
    
    @Test
    public void moveCharacter_MovingToTheSameLocation_mustThrowException(){
        ICharacter character = mock(ICharacter.class);
        
        try{
            IPosition positionSource = ObjectFactory.createObject(IPosition.class);
            positionSource.setCoords(1,2);

            IPosition positionTarget = ObjectFactory.createObject(IPosition.class);
            positionTarget.setCoords(1,2);

            Integer cellSource = map.getCellByPosition(positionSource);
            map.putCharacter(character, cellSource);
            Integer cellTarget = map.getCellByPosition(positionTarget);
            map.moveCharacter(cellSource, cellTarget);
            assertFalse("Should've thrown exception when trying to move the character to the same position", false);
        }catch(Exception ex){
            
        }
    }
    
    @Test
    public void moveCharacter_movingToANonEmptyLocation_mustThrowException(){
        ICharacter characterInSource = mock(ICharacter.class);
        ICharacter characterInTarget = mock(ICharacter.class);
        
        try{
            IPosition positionSource = ObjectFactory.createObject(IPosition.class);
            positionSource.setCoords(1,2);

            IPosition positionTarget = ObjectFactory.createObject(IPosition.class);
            positionTarget.setCoords(1,5);

            Integer cellSource = map.getCellByPosition(positionSource);
            Integer cellTarget = map.getCellByPosition(positionTarget);
            
            map.putCharacter(characterInSource, cellSource);
            map.putCharacter(characterInTarget, cellTarget);
            
            map.moveCharacter(cellSource, cellTarget);
            assertFalse("Should've thrown exception when trying to move the character to an occupied position", false);
        }catch(Exception ex){
            
        }
    }
    
    @Test
    public void moveCharacter_movingFromAnEmptyLocation_mustThrowException(){
        
        try{
            IPosition positionSource = ObjectFactory.createObject(IPosition.class);
            positionSource.setCoords(1,2);

            IPosition positionTarget = ObjectFactory.createObject(IPosition.class);
            positionTarget.setCoords(1,5);

            Integer cellSource = map.getCellByPosition(positionSource);
            Integer cellTarget = map.getCellByPosition(positionTarget);
            
            map.moveCharacter(cellSource, cellTarget);
            assertFalse("Should've thrown exception when trying to move the character from a non-occupied cell", false);
        }catch(Exception ex){
            
        }
    }
    
    @Test
    public void getCellByCharacter_CharacterInMap_mustReturnACell(){
        ICharacter newCharacter = mock(ICharacter.class);
        IPosition emptyPosition = ObjectFactory.createObject(IPosition.class);
        emptyPosition.setCoords(1,2);
        Integer emptyCell = map.getCellByPosition(emptyPosition);
        map.putCharacter(newCharacter, emptyCell);
            
        assertEquals("Should've retrieved the cell where the character was put ("+emptyCell+")",
                emptyCell, map.getCellByCharacter(newCharacter));
    }
    
    
}
