/*
 * Copyright (C) 2015 Ruben
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
package com.velonuboso.made.core.common.entity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 *
 * @author Ruben
 */
public class EventsLogConverter {
    
    public static String toJson (EventsLog log){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try {
            return gson.toJson(log);
        } catch (Exception e) {
            return null;
        }
    } 
    
    public static EventsLog fromJson (String log){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try {
            return gson.fromJson(log, EventsLog.class);
        } catch (Exception e) {
            return null;
        }
    }
}
