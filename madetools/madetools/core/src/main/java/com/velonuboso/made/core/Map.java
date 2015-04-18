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
import java.util.List;

/**
 *
 * @author Rubén Héctor García (raiben@gmail.com)
 */
public class Map implements IMap{

    private int sizeX;
    private int sizeY;
    private ICell cells[][];
    
    public Map() {
    }

    @Override
    public void initialize(int widthInCells, int heighInCells) {
        this.sizeX = widthInCells;
        this.sizeY = heighInCells;
        
        cells = new Cell[sizeX][sizeY];
        for (int iterX=0; iterX<sizeX; iterX++){
            for (int iterY=0; iterY<sizeY; iterY++){
                ICell cell = ObjectFactory.createObject(ICell.class);
                
                cell.getPosition().setCoords(iterX, iterY);
                
                cell.setTerrain(Terrain.LAND);
                
                cells[iterX][iterY] = cell;
            }   
        }
    }
    
    private int getCellId (int x, int y){
        return (y*sizeY)+x;
    }
    
    private int getXFromId(int id){
        return id%sizeY;
    }
    
    private int getFromId(int id){
        return id%sizeY;
    }
    
    @Override
    public List<ICell> getCells() {
        List<ICell> allCells = new ArrayList<>();
        
        for (int iterX=0; iterX<sizeX; iterX++){
            for (int iterY=0; iterY<sizeY; iterY++){
                allCells.add(cells[iterX][iterY]);
            }   
        }
        return allCells;
    }

    @Override
    public ICell getCell(IPosition position) {
        return cells[getXCoordModule(position)][getYCoordModule(position)];
    }

    private int getXCoordModule(IPosition position) {
        return (position.getXCoord()+sizeX)%sizeX;
    }
    
    private int getYCoordModule(IPosition position) {
        return (position.getYCoord()+sizeY)%sizeY;
    }

    @Override
    public void moveCharacter(ICell source, ICell target) {
        if (target.getCharacter()!=null){
            throw new RuntimeException("target cell should have no character. Currently "+target.getCharacter());
        }
        if (source.getCharacter()==null){
            throw new RuntimeException("source cell should have a character");
        }
        target.setCharacter(source.getCharacter());
        source.setCharacter(null);
    }
    
    @Override
    public List<ICell> getPositionsInRatio(IPosition position, int cellsFromPosition) {
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
    public void putCharacter(ICharacter character, ICell cell) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
}
