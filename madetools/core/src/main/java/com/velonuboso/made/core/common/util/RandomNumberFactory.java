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

package com.velonuboso.made.core.common.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Rubén Héctor García (raiben@gmail.com)
 */
public class RandomNumberFactory {

    private static Random random;
    private static List<Float> nextValues = null;
    
    static{
        random = new Random();
    }
    
    public static float getNextProbability() {
        if (nextValues == null || nextValues.isEmpty()){
            return random.nextFloat();
        }else{
            return nextValues.remove(0);
        }
    }
    
    public static void setSeed(long seed) {
        nextValues = null;
        random = new Random(seed);
    }
    
    public static void setNextValues(Float ... values){
        nextValues = new ArrayList<>(Arrays.asList(values));
    }
}
