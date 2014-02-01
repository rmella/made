/*
 * Copyright 2013 Rubén Héctor García <raiben@gmail.com>.
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


package com.velonuboso.made.core.interfaces;

import com.velonuboso.made.core.common.Position;
import java.util.HashSet;

/**
 *
 * @author raiben@gmail.com
 */
public interface MadeAgentInterface {

    public void addline(int days, String action);

    public int getDays();

    public HashSet<String> getLabels();

    public Position getPosition();

    public int getProfile();

    // -------------------------------------------------------------------------
    public String getSheet();

    boolean isAlive();

    public void justLive();

    public String getStringLog();

    public void addLabel(String label);

    public int getId();
    
    public String getFullName();
    
    public String getLabelsAsString();

}
