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

package com.velonuboso.made.core.common.implementation;

import com.velonuboso.made.core.common.api.IEventFactory;
import com.velonuboso.made.core.abm.api.ICharacter;
import com.velonuboso.made.core.common.api.IEvent;
import com.velonuboso.made.core.abm.api.IWorld;

/**
 *
 * @author Rubén Héctor García (raiben@gmail.com)
 */
public class EventFactory implements IEventFactory {

    public static final String WORLD_EXISTS = "WorldExist";
    public static final String INHABITANT_EXISTS = "InhabitantExists";
    
    @Override
    public IEvent worldExists(IWorld world) {
        return new Event(WORLD_EXISTS, world.getTimeUnit());
    }
    
    @Override
    public IEvent inhabitantExists(ICharacter inhabitant){
        return new Event(INHABITANT_EXISTS, inhabitant.getId(), inhabitant.getName());
    }

}
