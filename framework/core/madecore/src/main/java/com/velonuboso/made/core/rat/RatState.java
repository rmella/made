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

package com.velonuboso.made.core.rat;

/**
 *
 * @author raiben@gmail.com
 */
public enum RatState {
    BORN,
    HUNGRY,
    EAT,
    NUDGE_OK,
    KILL,
    KILLED,
    NUDGED,
    NUDGE_FAILED,
    DEFENDED,
    KINDLY_DISPLACED,
    MOVE,
    MOVE_TO_EAT,
    LOOK_FOR_PARTNER,
    PARTNER_FOUND,
    PREGNANT,
    PARENT,
    DIE,
    FREE_TIME, 
    KINDLY_REQUESTED_FOOD, 
    DESCENDANT
}
