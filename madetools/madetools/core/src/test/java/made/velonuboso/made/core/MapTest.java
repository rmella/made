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

import com.velonuboso.made.core.Map;
import com.velonuboso.made.core.ObjectFactory;
import com.velonuboso.made.core.Position;
import com.velonuboso.made.interfaces.ICell;
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
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

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
        boolean allCellsAreEmpty = map.getCells().stream().allMatch(cell -> cell.getCharacter() == null);
        assertTrue("Should've set all the cells as empty", allCellsAreEmpty);
    }

    @Test
    public void newMap_allCells_mustBeLand() {
        boolean allCellsAreLands = map.getCells().stream().allMatch(cell -> cell.getTerrain() == Terrain.LAND);
        assertTrue("Should've set all the cells as land", allCellsAreLands);
    }

    @Test
    public void newMap_allCells_mustHaveDifferentPosition() {
        HashSet<IPosition> differentPositions = new HashSet<>();
        map.getCells().stream().forEach(cell -> differentPositions.add(cell.getPosition()));

        assertEquals("all the cells should have different positions", 
                differentPositions.size(), map.getCells().size());
    }

    @Test
    public void getCell_allCells_mustHaveThePositionThanTheOneUsedToGetThem() {
        for (int iterX = 0; iterX < map.getWidthInCells(); iterX++) {
            for (int iterY = 0; iterY < map.getHeightInCells(); iterY++) {
                IPosition position = ObjectFactory.createObject(IPosition.class);
                position.setCoords(iterX, iterY);
                ICell retrievedCell = map.getCell(position);
                assertEquals("The cell in position " + position
                        + "should have position " + position
                        + ", not " + retrievedCell.getPosition(),
                        position, retrievedCell.getPosition());
            }
        }
    }
    
    @Test
    public void getCell_NewCell_MustBeSaved(){
        IPosition position = ObjectFactory.createObject(IPosition.class);
        position.setCoords(1,2);
        ICharacter character = mock(ICharacter.class);
        
        ICell cell = map.getCell(position);
        cell.setTerrain(Terrain.WATER);
        cell.setCharacter(character);
        
        assertEquals("Should've stored the terrain (water)", map.getCell(position).getCharacter(), character);
        assertEquals("Should've stored the agent (stubbed)", map.getCell(position).getTerrain(), Terrain.WATER);
    }
    
    @Test
    public void getCell_PositionOfOutBounds_MustBeModuled(){
        IPosition position = ObjectFactory.createObject(IPosition.class);
        position.setCoords(1,2);
        
        ICell cell = map.getCell(position);
        position.setCoords(21, 22);
        assertEquals("Should've retrieved the same cell using "+position+" and "+cell.getPosition(),
                cell, map.getCell(position));
        
        position.setCoords(-19, -18);
        assertEquals("Should've retrieved the same cell using "+position+" and "+cell.getPosition(),
                cell, map.getCell(position));
    }
    
    @Test
    public void moveCharacter_SourceWithAgentAndTargetEmpty_mustMoveCharacterToTarget(){
        ICharacter character = mock(ICharacter.class);
        
        IPosition positionSource = ObjectFactory.createObject(IPosition.class);
        positionSource.setCoords(1,2);
        
        IPosition positionTarget = ObjectFactory.createObject(IPosition.class);
        positionTarget.setCoords(1,5);
        
        ICell cellSource = map.getCell(positionSource);
        cellSource.setCharacter(character);
        
        ICell cellTarget = map.getCell(positionTarget);
        map.moveCharacter(cellSource, cellTarget);
        
        assertNull("Character should not be in source cell", cellSource.getCharacter());
        assertNotNull("Character should be in target cell", cellTarget.getCharacter());   
    }
    
    @Test
    public void moveCharacter_MovingToTheSameLocation_mustThrowException(){
        ICharacter character = mock(ICharacter.class);
        
        try{
            IPosition positionSource = ObjectFactory.createObject(IPosition.class);
            positionSource.setCoords(1,2);

            IPosition positionTarget = ObjectFactory.createObject(IPosition.class);
            positionTarget.setCoords(1,2);

            ICell cellSource = map.getCell(positionSource);
            cellSource.setCharacter(character);
            ICell cellTarget = map.getCell(positionTarget);
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

            ICell cellSource = map.getCell(positionSource);
            ICell cellTarget = map.getCell(positionTarget);
            
            cellSource.setCharacter(characterInSource);
            cellTarget.setCharacter(characterInTarget);
            
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

            ICell cellSource = map.getCell(positionSource);
            ICell cellTarget = map.getCell(positionTarget);
            
            map.moveCharacter(cellSource, cellTarget);
            assertFalse("Should've thrown exception when trying to move the character from a non-occupied cell", false);
        }catch(Exception ex){
            
        }
    }
    
}
