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
package com.velonuboso.made.core;

import com.velonuboso.made.interfaces.ICell;
import com.velonuboso.made.interfaces.ICharacter;
import com.velonuboso.made.interfaces.IMap;
import com.velonuboso.made.interfaces.IPosition;
import com.velonuboso.made.interfaces.Terrain;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Rubén Héctor García (raiben@gmail.com)
 */
public class Map implements IMap {

    private int sizeX;
    private int sizeY;
    private ICell cells[][];
    private HashMap<ICharacter, Integer> cellByAgent = new HashMap<>();

    public Map() {
    }

    @Override
    public void initialize(int widthInCells, int heighInCells) {
        this.sizeX = widthInCells;
        this.sizeY = heighInCells;

        cells = new Cell[sizeX][sizeY];
        for (int iterX = 0; iterX < sizeX; iterX++) {
            for (int iterY = 0; iterY < sizeY; iterY++) {
                ICell cell = ObjectFactory.createObject(ICell.class);
                cell.setTerrain(Terrain.LAND);
                cells[iterX][iterY] = cell;
            }
        }
    }

    @Override
    public List<Integer> getCells() {
        List<Integer> allCells = new ArrayList<>();

        for (int iterX = 0; iterX < sizeX; iterX++) {
            for (int iterY = 0; iterY < sizeY; iterY++) {
                allCells.add(getCellId(iterX, iterY));
            }
        }
        return allCells;
    }

    @Override
    public void moveCharacter(int sourceCellId, int targetCellId) {

        ICell sourceCell = getCellById(sourceCellId);
        ICell targetCell = getCellById(targetCellId);

        if (sourceCell.getCharacter() == null) {
            throw new RuntimeException("source cell should have a character");
        }
        if (targetCell.getCharacter() != null) {
            throw new RuntimeException("target cell should have no character. Currently " + targetCell.getCharacter());
        }

        targetCell.setCharacter(sourceCell.getCharacter());
        sourceCell.setCharacter(null);
    }

    @Override
    public List<Integer> getPositionsInRatio(IPosition position, int cellsFromPosition) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getMapAsAscii() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getHeightInCells() {
        return sizeX;
    }

    @Override
    public int getWidthInCells() {
        return sizeY;
    }

    @Override
    public void putCharacter(ICharacter character, int cell) {
        if (getCellById(cell).getCharacter()!=null){
            throw new RuntimeException("Cell "+cell+" already has a character: "+getCellById(cell).getCharacter());
        }
        getCellById(cell).setCharacter(character);
        
    }

    @Override
    public ICharacter getCharacter(int cellId) {
        return getCellById(cellId).getCharacter();
    }

    @Override
    public Terrain getTerrain(int cellId) {
        return getCellById(cellId).getTerrain();
    }

    @Override
    public IPosition getPosition(int cellId) {
        IPosition position = ObjectFactory.createObject(IPosition.class);
        position.setCoords(getXFromId(cellId), getYFromId(cellId));
        return position;
    }

    @Override
    public Integer getCellByPosition(IPosition position) {
        return getCellId(position.getXCoord(), position.getYCoord());
    }

    private int getCellId(int x, int y) {
        return (((y + sizeY) % sizeY) * sizeY) + ((x + sizeX) % sizeX);
    }

    private int getXFromId(int id) {
        return id % sizeY;
    }

    private int getYFromId(int id) {
        return id / sizeY;
    }

    private ICell getCellById(int cellId) {

        int positionX = getXFromId(cellId);
        int positionY = getYFromId(cellId);

        return cells[positionX][positionY];
    }

    @Override
    public Integer getCellByCharacter(ICharacter position) {
        return 1;
    }

}
