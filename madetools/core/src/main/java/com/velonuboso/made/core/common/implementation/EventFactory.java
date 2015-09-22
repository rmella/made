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
    public static final String MOVES_AWAY = "MovesAway";
    public static final String MOVES = "Moves";
    public static final String DISPLACES = "Displaces";
    public static final String SKIPS_TURN = "SkipsTurn";
    public static final String STAINS = "Stains";
    public static final String TRANSFERS_COLOR = "TransferColor";
    public static final String COLOR_SPOT_DISAPPEARS = "ColorSpotDisappears";
    public static final String COLOR_SPOT_APPEARS = "ColorSpotAppears";
    public static final String EXCEPTION = "Exception";

    @Override
    public IEvent worldExists(final IWorld world) {
        return new Event(WORLD_EXISTS, world.getTimeUnit());
    }

    @Override
    public IEvent inhabitantExists(final ICharacter inhabitant) {
        return new Event(INHABITANT_EXISTS, inhabitant.getId());
    }

    @Override
    public IEvent hasFear(final ICharacter subject, final ICharacter enemy) {
        return new Event(HAS_FEAR, subject.getId(), enemy.getId());
    }

    @Override
    public IEvent hasAnticipation(final ICharacter subject, final IColorSpot spot) {
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

    @Override
    public IEvent movesAway(ICharacter subject) {
        return new Event(MOVES_AWAY, subject.getId());
    }

    @Override
    public IEvent moves(ICharacter subject) {
        return new Event(MOVES, subject.getId());
    }

    @Override
    public IEvent displaces(ICharacter subject) {
        return new Event(DISPLACES, subject.getId());
    }

    @Override
    public IEvent skipsTurn(ICharacter subject) {
        return new Event(SKIPS_TURN, subject.getId());
    }

    @Override
    public IEvent stains(ICharacter subject) {
        return new Event(STAINS, subject.getId());
    }

    @Override
    public IEvent transfersColor(ICharacter subject) {
        return new Event(TRANSFERS_COLOR, subject.getId());
    }

    @Override
    public IEvent colorSpotAppears(IColorSpot subject) {
        return new Event(COLOR_SPOT_APPEARS, subject.getId());
    }

    @Override
    public IEvent colorSpotDisappears(IColorSpot subject) {
        return new Event(COLOR_SPOT_DISAPPEARS, subject.getId());
    }

    @Override
    public IEvent exception(String message) {
        return new Event(EXCEPTION, message);
    }
}
