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
package com.velonuboso.made.core.abm.implementation;

import com.velonuboso.made.core.common.util.ObjectFactory;
import com.velonuboso.made.core.abm.api.ICharacter;
import com.velonuboso.made.core.abm.api.IMap;
import com.velonuboso.made.core.abm.api.IPosition;
import com.velonuboso.made.core.abm.entity.TerrainType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 *
 * @author Rubén Héctor García (raiben@gmail.com)
 */
public class Map implements IMap {

    private int sizeX;
    private int sizeY;
    private HashMap<ICharacter, Integer> cellByCharacter = new HashMap<>();
    private HashMap<Integer, ICharacter> characterByCell = new HashMap<>();
    private HashMap<Integer, TerrainType> terrainByCell = new HashMap<>();

    public Map() {
    }

    @Override
    public void initialize(int widthInCells, int heighInCells) {
        this.sizeX = widthInCells;
        this.sizeY = heighInCells;

        for (int iterX = 0; iterX < sizeX; iterX++) {
            for (int iterY = 0; iterY < sizeY; iterY++) {
                terrainByCell.put(getCell(iterX, iterY), TerrainType.LAND);
            }
        }
    }

    @Override
    public List<Integer> getCells() {
        List<Integer> allCells = new ArrayList<>();

        for (int iterX = 0; iterX < sizeX; iterX++) {
            for (int iterY = 0; iterY < sizeY; iterY++) {
                allCells.add(getCell(iterX, iterY));
            }
        }
        return allCells;
    }

    @Override
    public void moveCharacter(int sourceCell, int targetCell) {

        if (characterByCell.get(sourceCell) == null) {
            throw new RuntimeException("source cell should have a character");
        }
        if (characterByCell.get(targetCell) != null) {
            throw new RuntimeException("target cell should have no character. Currently "
                    + characterByCell.get(targetCell));
        }

        characterByCell.put(targetCell, characterByCell.get(sourceCell));
        characterByCell.remove(sourceCell);
        cellByCharacter.put(characterByCell.get(targetCell), targetCell);
    }

    @Override
    public List<Integer> getCellsToMove(Integer cellId, int maxMovement) {
        HashSet<Integer> navigatedCells = new HashSet<>();
        ICharacter author = getCharacter(cellId);
        recursiveAddCellsToMove(cellId, navigatedCells, maxMovement, author);
        return new ArrayList<>(navigatedCells);
    }

    private void recursiveAddCellsToMove(Integer currentCell, HashSet<Integer> navigatedCells,
            int maxMovement, ICharacter author) {

        if (maxMovement<0){
            return;
        }
        
        int sourceX = getXFromId(currentCell);
        int sourceY = getYFromId(currentCell);

        navigatedCells.add(currentCell);

        for (int offsetX = -1; offsetX < 2; offsetX++) {
            for (int offsetY = -1; offsetY < 2; offsetY++) {
                
                int navigableCell = getCell(sourceX + offsetX, sourceY + offsetY);
                if (!navigatedCells.contains(navigableCell) 
                        && (cellCanBeOccupiedByCharacter(navigableCell, author))) {
                    
                    recursiveAddCellsToMove(navigableCell, navigatedCells, maxMovement-1, author);
                }
            }
        }
    }

    @Override
    public int getHeight() {
        return sizeX;
    }

    @Override
    public int getWidth() {
        return sizeY;
    }

    @Override
    public void putCharacter(ICharacter character, int cell) {
        if (characterByCell.get(cell) != null) {
            throw new RuntimeException("Cell " + cell + 
                    " already has a character: " + characterByCell.get(cell));
        }
        if (cellByCharacter.keySet().contains(character)){
            throw new RuntimeException("Character " + character + 
                    " is already in the map");
        }
        characterByCell.put(cell, character);
        cellByCharacter.put(character, cell);
    }

    @Override
    public ICharacter getCharacter(int cell) {
        return characterByCell.get(cell);
    }

    @Override
    public TerrainType getTerrain(int cell) {
        return terrainByCell.get(cell);
    }

    @Override
    public Integer getPositionX(int cell) {
        return getXFromId(cell);
    }

    @Override
    public Integer getPositionY(int cell) {
        return getYFromId(cell);
    }

    @Override
    public Integer getCell(int x, int y) {
        return (((y + sizeY) % sizeY) * sizeY) + ((x + sizeX) % sizeX);
    }

    private int getXFromId(int id) {
        return id % sizeY;
    }

    private int getYFromId(int id) {
        return id / sizeY;
    }

    @Override
    public Integer getCell(ICharacter character) {
        return cellByCharacter.get(character);
    }

    @Override
    public Integer getCell(IPosition position) {
        return getCell(position.getXCoord(), position.getYCoord());
    }

    @Override
    public IPosition getPosition(int cell) {
        IPosition position = ObjectFactory.createObject(IPosition.class);
        position.setCoords(getPositionX(cell), getPositionY(cell));
        return position;
    }

    @Override
    public void removeCharacter(int cell) {
        ICharacter character = getCharacter(cell);
        if (character != null) {
            characterByCell.remove(cell);
            cellByCharacter.remove(character);
        }
    }

    private boolean cellCanBeOccupiedByCharacter(int navigableCell, ICharacter author) {
        return getCharacter(navigableCell)==author || getCharacter(navigableCell)==null;
    }
}
