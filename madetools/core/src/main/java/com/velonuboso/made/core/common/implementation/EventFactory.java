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
import com.velonuboso.made.core.abm.api.IColorSpot;
import com.velonuboso.made.core.common.api.IEvent;
import com.velonuboso.made.core.abm.api.IWorld;

/**
 *
 * @author Rubén Héctor García (raiben@gmail.com)
 */
public class EventFactory implements IEventFactory {

    public static final String WORLD_EXISTS = "WorldExist";
    public static final String INHABITANT_EXISTS = "InhabitantExists";
    public static final String HAS_FEAR = "HasFear";
    public static final String HAS_ANTICIPATION = "HasAnticipation";
    public static final String CAN_IMPROVE_FRIEND_SIMILARITY = "CanImproveFriendSimilarity";
    public static final String CAN_IMPROVE_SELF_SIMILARITY = "CanImproveSelfSimilarity";
    public static final String CAN_REDUCE_ENEMY_SIMILARITY = "CanReduceEnemySimilarity";
    public static final String IS_SAD = "IsSad";
    public static final String IS_SURPRISED = "IsSurprised";
    public static final String ERROR = "Error";
    
    @Override
    public IEvent worldExists(final IWorld world) {
        return new Event(WORLD_EXISTS, world.getTimeUnit());
    }
    
    @Override
    public IEvent inhabitantExists(final ICharacter inhabitant){
        return new Event(INHABITANT_EXISTS, inhabitant.getId());
    }

    @Override
    public IEvent hasFear(final ICharacter subject, final ICharacter enemy){
        return new Event(HAS_FEAR, subject.getId(), enemy.getId());
    }
    
    @Override
    public IEvent hasAnticipation(final ICharacter subject, final IColorSpot spot){
        return new Event(HAS_ANTICIPATION, subject.getId(), spot.getId());
    }

    @Override
    public IEvent canImproveFriendSimilarity(ICharacter subject, ICharacter friend) {
        return new Event(CAN_IMPROVE_FRIEND_SIMILARITY, subject.getId(), friend.getId());
    }

    @Override
    public IEvent canImproveSelfSimilarity(ICharacter subject, IColorSpot spot) {
        return new Event(CAN_IMPROVE_SELF_SIMILARITY, subject.getId(), spot.getId());
    }

    @Override
    public IEvent canReduceEnemySimilarity(ICharacter subject, ICharacter enemy) {
        return new Event(CAN_REDUCE_ENEMY_SIMILARITY, subject.getId(), enemy.getId());
    }
    
    @Override
    public IEvent error(ICharacter subject, String message) {
        return new Event(ERROR, subject.getId(), message);
    }

    @Override
    public IEvent isSad(ICharacter subject) {
        return new Event(IS_SAD, subject.getId());
    }

    @Override
    public IEvent isSurprised(ICharacter subject) {
        return new Event(IS_SURPRISED, subject.getId());
    }
}
