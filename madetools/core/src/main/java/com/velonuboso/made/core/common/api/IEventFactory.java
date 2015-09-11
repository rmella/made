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

package com.velonuboso.made.core.common.api;

import com.velonuboso.made.core.abm.api.ICharacter;
import com.velonuboso.made.core.abm.api.IColorSpot;
import com.velonuboso.made.core.abm.api.IWorld;
import com.velonuboso.made.core.common.api.IEvent;
import com.velonuboso.made.core.common.implementation.EventFactory;
import com.velonuboso.made.core.common.util.ImplementedBy;

/**
 *
 * @author Rubén Héctor García (raiben@gmail.com)
 */

@ImplementedBy(targetClass = EventFactory.class, targetMode = ImplementedBy.Mode.NORMAL)
public interface IEventFactory {
    IEvent inhabitantExists(ICharacter inhabitant);
    IEvent worldExists(IWorld world);
    IEvent hasFear(final ICharacter subject, final ICharacter enemy);
    IEvent hasAnticipation(final ICharacter subject, final IColorSpot spot);
    IEvent CanImproveFriendSimilarity(ICharacter character, ICharacter targetCharacter);
}
