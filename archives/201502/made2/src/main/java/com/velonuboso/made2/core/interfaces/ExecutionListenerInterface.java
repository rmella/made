/*
 * Copyright 2014 Rubén Héctor García <raiben@gmail.com>.
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


package com.velonuboso.made2.core.interfaces;

import com.velonuboso.made2.core.rat.RatAgent;
import java.util.ArrayList;

/**
 *
 * @author raiben@gmail.com
 */
public interface ExecutionListenerInterface {
    
    public void start();
    
    public void end();
    
    public void progress (float value);
    
    public void generation (int id, float fitnessMax, float fitnessAVG, ArrayList individual);
    
    public void failure (Exception e);

    public void log (String text);

    public float getProgress();
    
    public void environmentExecuted (ArrayList<RatAgent> agents);
    
}
