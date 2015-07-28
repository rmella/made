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

import com.velonuboso.made.core.abm.api.IBlackBoard;
import java.util.HashMap;

/**
 *
 * @author Rubén Héctor García (raiben@gmail.com)
 */
public class BlackBoard implements IBlackBoard{

    private HashMap<String, Object> mapKeyValues;
    public static final int DEFAULT_INT_VALUE = 0;
    public static final float DEFAULT_FLOAT_VALUE = 0f;
    public static final String DEFAULT_STRING_VALUE = "";

    public BlackBoard() {
        mapKeyValues = new HashMap<>();
    }
    
    @Override
    public int getInt(String key) {
        if (!containedAndValidType(key, Integer.class)){
            return DEFAULT_INT_VALUE;
        }
        return (int) mapKeyValues.get(key);
    }

    @Override
    public void setInt(String key, int value) {
        mapKeyValues.put(key, value);
    }

    @Override
    public float getFloat(String key) {
        if (!containedAndValidType(key, Float.class)){
            return DEFAULT_FLOAT_VALUE;
        }
        return (float) mapKeyValues.get(key);
    }

    @Override
    public void setFloat(String key, float value) {
        mapKeyValues.put(key, value);
    }

    @Override
    public String getString(String key) {
        if (!containedAndValidType(key, String.class)){
            return DEFAULT_STRING_VALUE;
        }
        return (String) mapKeyValues.get(key);
    }

    @Override
    public void setString(String key, String value) {
        mapKeyValues.put(key, value);
    }
    
    private boolean containedAndValidType(String key, Class targetClass) {
        return mapKeyValues.containsKey(key) && (mapKeyValues.get(key)).getClass()==targetClass;
    }
}
