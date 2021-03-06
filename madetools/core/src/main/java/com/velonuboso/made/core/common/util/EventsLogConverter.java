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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.velonuboso.made.core.common.entity.EventsLogEntity;

/**
 *
 * @author Rubén Héctor García (raiben@gmail.com)
 */
public class EventsLogConverter {
    
    public static String toJson (EventsLogEntity log){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(log);
    } 
    
    public static EventsLogEntity fromJson (String log){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try {
            return gson.fromJson(log, EventsLogEntity.class);
        } catch (Exception e) {
            return null;
        }
    }
}
