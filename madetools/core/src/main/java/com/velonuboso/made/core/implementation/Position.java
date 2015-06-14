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

package com.velonuboso.made.core.implementation;

import com.velonuboso.made.core.api.IPosition;

/**
 *
 * @author Rubén Héctor García (raiben@gmail.com)
 */
public class Position implements IPosition{

    int x;
    int y;

    @Override
    public void setCoords(int xCoord, int yCoord) {
        x = xCoord;
        y = yCoord;
    }
    
    @Override
    public int getXCoord() {
        return x;
    }

    @Override
    public int getYCoord() {
        return y;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof IPosition)){
            return false;
        }
        
        IPosition position = (IPosition) obj;
        return this.getXCoord()==position.getXCoord() && this.getYCoord() == position.getYCoord();
    }

    @Override
    public int hashCode() {
        return (y*1000)+x;
    }

    @Override
    public String toString() {
        return "("+this.x+","+this.y+")";
    }

    
}
