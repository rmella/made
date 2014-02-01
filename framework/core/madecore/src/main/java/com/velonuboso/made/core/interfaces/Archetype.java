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

import com.velonuboso.made.core.common.ArchetypeType;
import com.velonuboso.made.core.setup.GlobalSetup;
import java.util.ArrayList;
import org.apache.commons.math3.analysis.function.Gaussian;

/**
 *
 * @author raiben@gmail.com
 */
public abstract class Archetype {
    
    
    public abstract String getName();
    
    public abstract double evaluate(
            GlobalSetup setup, 
            ArrayList<MadeAgentInterface> agents, 
            Float from, 
            Float to);

    public static double getGaussian(float ratio, Float from, Float to) {
        Gaussian gaussian = new Gaussian();
        if (from == null && to == null){
            return 1;
        }else if (from==null){
            from = 1-to;
        }else if (to==null){
            to = 2-from;
        }
        float target = (from + to) /2f;
        float amplitude = to - from;
        return gaussian.value((ratio-target)/amplitude)/0.40;
    }
    
    public abstract  ArchetypeType getType();
    
}
