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

import com.velonuboso.made.core.api.IEvent;

/**
 *
 * @author Rubén Héctor García (raiben@gmail.com)
 */
public class Event implements IEvent{

    private String name;
    private Object[] arguments;
    
    public Event(String name, Object... arguments) {
        this.name = name;
        this.arguments = arguments;
    }

    @Override
    public String toLogicalPredicate() {
        StringBuilder logicalPredicate = new StringBuilder();
        logicalPredicate.append(name +" (");
        for (int i = 0; i < arguments.length; i++) {
            
            if (arguments[i] instanceof Integer){
                logicalPredicate.append(arguments[i]);
            }else{
                logicalPredicate.append("\""+arguments[i]+"\"");
            }
            
            if (i<arguments.length-1){
                logicalPredicate.append(", ");
            }
        }
        logicalPredicate.append(")");
        return logicalPredicate.toString();
    }
    
}
