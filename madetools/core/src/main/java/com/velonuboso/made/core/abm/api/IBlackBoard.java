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
package com.velonuboso.made.core.abm.api;

import com.velonuboso.made.core.abm.implementation.BlackBoard;
import com.velonuboso.made.core.common.util.ImplementedBy;

/**
 *
 * @author Rubén Héctor García (raiben@gmail.com)
 */
@ImplementedBy(targetClass = BlackBoard.class, targetMode = ImplementedBy.Mode.NORMAL)
public interface IBlackBoard {
    
    int getInt(String key);
    void setInt(String key, int value);
    
    float getFloat(String key);
    void setFloat (String key, float value);
    
    String getString (String key);
    void setString (String key, String value);
    
    Object getObject (String key);
    void setObject (String key, Object value);
}
