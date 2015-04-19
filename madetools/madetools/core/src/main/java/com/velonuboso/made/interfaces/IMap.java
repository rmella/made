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
package com.velonuboso.made.interfaces;

import java.util.List;

/**
 *
 * @author Rubén Héctor García (raiben@gmail.com)
 */
public interface IMap {

    public void initialize(int widthInCells, int heighInCells);
    
    public int getWidthInCells();

    public int getHeightInCells();
    
    public List<Integer> getCells();

    public Integer getCellByPosition(IPosition position);
    
    public Integer getCellByCharacter(ICharacter position);

    public void moveCharacter(int sourceCell, int targetCell); 

    public List<Integer> getPositionsInRatio(IPosition position, int cellsFromPosition);

    public String getMapAsAscii();

    public void putCharacter (ICharacter character, int cell);
    
    public ICharacter getCharacter (int cellId);
    
    public Terrain getTerrain (int cellId);
    
    public IPosition getPosition (int cellId);
    
}
