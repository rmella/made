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

    public List<Integer> getCells();

    public void moveCharacter(int sourceCell, int targetCell);

    public List<Integer> getCellsToMove(Integer cellId, int maxMovement);

    public void putCharacter(ICharacter character, int cell);

    public void removeCharacter(int cell);

    public ICharacter getCharacter(int cell);

    public Terrain getTerrain(int cell);

    public Integer getPositionX(int cell);

    public Integer getPositionY(int cell);

    public IPosition getPosition(int cell);

    public Integer getCell(int coordX, int coordY);

    public Integer getCell(IPosition position);

    public Integer getCell(ICharacter character);

    public int getWidth();

    public int getHeight();

}
