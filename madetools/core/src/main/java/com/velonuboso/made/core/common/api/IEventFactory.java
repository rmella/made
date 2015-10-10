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
import com.velonuboso.made.core.abm.entity.CharacterShape;
import com.velonuboso.made.core.abm.implementation.piece.Piece;
import com.velonuboso.made.core.common.api.IEvent;
import com.velonuboso.made.core.common.implementation.EventFactory;
import com.velonuboso.made.core.common.util.ImplementedBy;
import javafx.scene.paint.Color;

/**
 *
 * @author Rubén Héctor García (raiben@gmail.com)
 */

@ImplementedBy(targetClass = EventFactory.class, targetMode = ImplementedBy.Mode.SINGLETON)
public interface IEventFactory {
    public void setDay(int day);
    IEvent inhabitantExists(ICharacter inhabitant);
    IEvent worldExists(IWorld world);
    IEvent newDay(); 
    IEvent hasFear(final ICharacter subject, final ICharacter enemy);
    IEvent hasAnticipation(final ICharacter subject, final IColorSpot spot);
    IEvent canImproveFriendSimilarity(ICharacter subject, ICharacter friend);
    IEvent canImproveSelfSimilarity(ICharacter subject, IColorSpot spot);
    IEvent canReduceEnemySimilarity(ICharacter subject, ICharacter enemy);
    IEvent isSad(ICharacter subject);
    IEvent isSurprised(ICharacter subject);
    IEvent error(ICharacter subject, String message);
    IEvent movesAway(ICharacter subject, int cellId);
    IEvent moves(ICharacter subject, int cellId);
    IEvent displaces(ICharacter subject, ICharacter targetCharacter, int cellId);
    IEvent skipsTurn(ICharacter character);
    IEvent stains(ICharacter subject, IColorSpot targetSpot);
    IEvent transfersColor(ICharacter subject, ICharacter targetCharacter);
    IEvent colorSpotAppears(IColorSpot spot, int cellId);
    IEvent colorSpotDisappears(IColorSpot spot, int cellId);
    IEvent exception(String message);
    IEvent joy(ICharacter aThis, float joy);
    IEvent isFriendOf(ICharacter subject, ICharacter ... friends);
    IEvent isEnemyOf(ICharacter subject, ICharacter ... enemies);
    IEvent naturalChange(Piece aThis, Color currentColor, Color newColor);
    IEvent characterAppears (ICharacter subject, int cellId);
    IEvent trusts(ICharacter subject, ICharacter friendWithMostAffinity);
    IEvent givesTurn(ICharacter subject, ICharacter target);

}
