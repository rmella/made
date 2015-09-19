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
package com.velonuboso.made.core.abm.implementation;

import com.velonuboso.made.core.common.util.ObjectFactory;
import com.velonuboso.made.core.abm.api.ICharacter;
import com.velonuboso.made.core.abm.api.IColorSpot;
import com.velonuboso.made.core.abm.api.IMap;
import com.velonuboso.made.core.abm.api.IPosition;
import com.velonuboso.made.core.abm.entity.TerrainType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.stream.IntStream;

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
    private HashMap<IColorSpot, Integer> cellByColorSpot = new HashMap<>();
    private HashMap<Integer, IColorSpot> ColorSpotByCell = new HashMap<>();

    private final static double SQRT_2 = Math.sqrt(2);
    
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

    @Override
    public List<Integer> getCellsAround(Integer currentCellId, int maxMovement) {

        int cellX = getPositionX(currentCellId);
        int cellY = getPositionY(currentCellId);

        int fromX = cellX - maxMovement;
        int toX = cellX + maxMovement + 1;
        int fromY = cellY - maxMovement;
        int toY = cellY + maxMovement + 1;

        ArrayList<Integer> cells = new ArrayList<>();

        IntStream.range(fromX, toX).forEach(
                x -> IntStream.range(fromY, toY).forEach(
                        y -> {
                            int cellAround = getCell(x, y);
                            if (cellAround != currentCellId) {
                                cells.add(getCell(x, y));
                            }
                        }
                )
        );

        return cells;
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
            throw new RuntimeException("Cell " + cell
                    + " already has a character: " + characterByCell.get(cell));
        }
        if (cellByCharacter.keySet().contains(character)) {
            throw new RuntimeException("Character " + character
                    + " is already in the map");
        }
        characterByCell.put(cell, character);
        cellByCharacter.put(character, cell);
        character.setMap(this);
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

    @Override
    public void putColorSpot(IColorSpot spot, int cell) {
        ColorSpotByCell.put(cell, spot);
        cellByColorSpot.put(spot, cell);
    }

    @Override
    public void removeSpot(int cell) {
        IColorSpot spot = getColorSpot(cell);
        if (spot != null) {
            ColorSpotByCell.remove(cell);
            cellByColorSpot.remove(spot);
        }
    }

    @Override
    public IColorSpot getColorSpot(int cell) {
        return ColorSpotByCell.get(cell);
    }

    @Override
    public Integer getCell(IColorSpot spot) {
        return cellByColorSpot.get(spot);
    }

    @Override
    public boolean isCharacterNearCell(ICharacter character, int cell) {
        return getCellsAround(getCell(character), 1).contains(cell);
    }

    @Override
    public Integer getCloserCell(ICharacter character, int targetCell) {
        final int PIECE_MOVEMENT = 1;

        int currentCell = getCell(character);
        List<Integer> cellsToMove = getCellsToMove(currentCell, PIECE_MOVEMENT);
        return cellsToMove
                .stream()
                .min((Integer firstCell, Integer secondCell) -> {
                    double distanceFirstCell = distanceBetweenCells(firstCell, targetCell);
                    double distanceSecondCell = distanceBetweenCells(secondCell, targetCell);
                    return Double.compare(distanceFirstCell, distanceSecondCell);
                })
                .orElse(null);
    }

    private void recursiveAddCellsToMove(Integer currentCell, HashSet<Integer> navigatedCells,
            int maxMovement, ICharacter author) {

        if (maxMovement < 0) {
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

                    recursiveAddCellsToMove(navigableCell, navigatedCells, maxMovement - 1, author);
                }
            }
        }
    }

    private int getXFromId(int id) {
        return id % sizeY;
    }

    private int getYFromId(int id) {
        return id / sizeY;
    }

    private boolean cellCanBeOccupiedByCharacter(int navigableCell, ICharacter author) {
        return getCharacter(navigableCell) == author 
                || getCharacter(navigableCell) == null
                || author.getShape().wins(getCharacter(navigableCell).getShape());
    }

    private double distanceBetweenCells(int sourceCell, int targetCell) {
        int currentCellX = getPositionX(sourceCell);
        int currentCellY = getPositionY(sourceCell);
        int targetCellX = getPositionX(targetCell);
        int targetCellY = getPositionY(targetCell);
        
        double minimum = Double.MAX_VALUE;

        for (int offsetX = currentCellX - getWidth(); offsetX <= currentCellX + getWidth(); offsetX += getWidth()) {
            for (int offsetY = currentCellY - getHeight(); offsetY <= currentCellY + getHeight(); offsetY += getHeight()) {
                minimum = Math.min(minimum, getDistance(offsetX, offsetY, targetCellX, targetCellY));
            }
        }
        return minimum;
    }

    private double getDistance(int x1, int y1, int x2, int y2) {
        int dx = Math.abs(x2 - x1);
        int dy = Math.abs(y2 - y1);

        int min = Math.min(dx, dy);
        int max = Math.max(dx, dy);

        int diagonalSteps = min;
        int straightSteps = max - min;

        return SQRT_2 * diagonalSteps + straightSteps;
    }
}
