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
package com.velonuboso.made.core.abm.api;

import com.velonuboso.made.core.abm.entity.TerrainType;
import com.velonuboso.made.core.abm.implementation.Map;
import com.velonuboso.made.core.common.util.ImplementedBy;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Rubén Héctor García (raiben@gmail.com)
 */
@ImplementedBy(targetClass = Map.class, targetMode = ImplementedBy.Mode.NORMAL)
public interface IMap {

    public void initialize(int widthInCells, int heighInCells);

    public List<Integer> getCells();

    public void moveCharacter(int sourceCell, int targetCell);

    public List<Integer> getCellsToMove(Integer cellId, int maxMovement,HashMap<ICharacter, Float> affinityMatrix);

    public List<Integer> getCellsAround(Integer currentCellId, int maxMovement);
    
    public void putCharacter(ICharacter character, int cell);

    public void putColorSpot(IColorSpot spot, int cell);
    
    public void removeCharacter(int cell);
    
    public void removeSpot(int cell);

    public ICharacter getCharacter(int cell);
    
    public IColorSpot getColorSpot(int cell);
    
    public TerrainType getTerrain(int cell);

    public Integer getPositionX(int cell);

    public Integer getPositionY(int cell);

    public IPosition getPosition(int cell);

    public Integer getCell(int coordX, int coordY);

    public Integer getCell(IPosition position);

    public Integer getCell(ICharacter character);

    public Integer getCell(IColorSpot character);
    
    public int getWidth();

    public int getHeight();

    public boolean isCharacterNearCell(ICharacter character, int ccell);
    
    public Integer getCloserCell(ICharacter character, int cell, HashMap<ICharacter, Float> affinityMatrix);

    public void setEventsWriter(IEventsWriter eventsWriter);

}
