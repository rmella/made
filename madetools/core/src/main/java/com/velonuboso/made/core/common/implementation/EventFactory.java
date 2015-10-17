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
import com.velonuboso.made.core.abm.entity.CharacterShape;
import com.velonuboso.made.core.abm.implementation.piece.Piece;
import com.velonuboso.made.core.abm.implementation.piece.PieceUtilities;
import java.util.Arrays;
import javafx.scene.paint.Color;

/**
 *
 * @author Rubén Héctor García (raiben@gmail.com)
 */
public class EventFactory implements IEventFactory {

    public static final String WORLD_EXISTS = "worldExist";
    public static final String INHABITANT_EXISTS = "inhabitantExists";
    public static final String HAS_FEAR = "hasFear";
    public static final String HAS_ANTICIPATION = "hasAnticipation";
    public static final String CAN_IMPROVE_FRIEND_SIMILARITY = "canImproveFriendSimilarity";
    public static final String CAN_IMPROVE_SELF_SIMILARITY = "canImproveSelfSimilarity";
    public static final String CAN_REDUCE_ENEMY_SIMILARITY = "canReduceEnemySimilarity";
    public static final String IS_SAD = "isSad";
    public static final String IS_SURPRISED = "isSurprised";
    public static final String ERROR = "error";
    public static final String MOVES_AWAY = "movesAway";
    public static final String MOVES = "moves";
    public static final String DISPLACES = "displaces";
    public static final String SKIPS_TURN = "skipsTurn";
    public static final String STAINS = "stains";
    public static final String TRANSFERS_COLOR = "transferColor";
    public static final String COLOR_SPOT_DISAPPEARS = "colorSpotDisappears";
    public static final String COLOR_SPOT_APPEARS = "colorSpotAppears";
    public static final String EXCEPTION = "exception";
    public static final String JOY = "joy";
    public static final String IS_FRIEND_OF = "isFriendOf";
    public static final String IS_ENEMY_OF = "isEnemyOf";
    public static final String NATURAL_CHANGE = "naturalChange";
    public static final String CHARACTER_APPEARS = "characterAppears";
    public static final String NEW_DAY = "newDay";
    public static final String TRUSTS = "trusts";
    public static final String GIVES_TURN = "giveTurn";
    public static final String ARE_NEAR = "areNear";
    
    private float currentDay;

    public EventFactory() {
        currentDay = 0;
    }

    @Override
    public void setDay(float day) {
        currentDay = day;
    }
    
    @Override
    public IEvent newDay() {
        return new Event(NEW_DAY, currentDay);
    }
    
    @Override
    public IEvent worldExists(final IWorld world) {
        return new Event(WORLD_EXISTS, currentDay, world.getTimeUnit());
    }

    @Override
    public IEvent inhabitantExists(final ICharacter inhabitant) {
        return new Event(INHABITANT_EXISTS, currentDay, inhabitant.getId());
    }

    @Override
    public IEvent hasFear(final ICharacter subject, final ICharacter enemy) {
        return new Event(HAS_FEAR, currentDay, subject.getId(), enemy.getId());
    }

    @Override
    public IEvent hasAnticipation(final ICharacter subject, final IColorSpot spot) {
        return new Event(HAS_ANTICIPATION, currentDay, subject.getId(), spot.getId());
    }

    @Override
    public IEvent canImproveFriendSimilarity(ICharacter subject, ICharacter friend) {
        return new Event(CAN_IMPROVE_FRIEND_SIMILARITY, currentDay, subject.getId(), friend.getId());
    }

    @Override
    public IEvent canImproveSelfSimilarity(ICharacter subject, IColorSpot spot) {
        return new Event(CAN_IMPROVE_SELF_SIMILARITY, currentDay, subject.getId(), spot.getId());
    }

    @Override
    public IEvent canReduceEnemySimilarity(ICharacter subject, ICharacter enemy) {
        return new Event(CAN_REDUCE_ENEMY_SIMILARITY, currentDay, subject.getId(), enemy.getId());
    }

    @Override
    public IEvent error(ICharacter subject, String message) {
        return new Event(ERROR, subject.getId(), message);
    }

    @Override
    public IEvent isSad(ICharacter subject) {
        return new Event(IS_SAD, currentDay, subject.getId());
    }

    @Override
    public IEvent isSurprised(ICharacter subject) {
        return new Event(IS_SURPRISED, currentDay, subject.getId());
    }

    @Override
    public IEvent movesAway(ICharacter subject, int cellId) {
        return new Event(MOVES_AWAY, currentDay, subject.getId(), cellId);
    }

    @Override
    public IEvent moves(ICharacter subject, int cellId) {
        return new Event(MOVES, currentDay, subject.getId(), cellId);
    }

    @Override
    public IEvent displaces(ICharacter subject, ICharacter targetCharacter, int cellId) {
        return new Event(DISPLACES, currentDay, subject.getId(), targetCharacter.getId(), cellId);
    }

    @Override
    public IEvent skipsTurn(ICharacter subject) {
        return new Event(SKIPS_TURN, currentDay, subject.getId());
    }

    @Override
    public IEvent stains(ICharacter subject, IColorSpot targetSpot) {
        return new Event(STAINS, currentDay, subject.getId(), targetSpot.getId());
    }

    @Override
    public IEvent transfersColor(ICharacter subject, ICharacter targetCharacter) {
        return new Event(TRANSFERS_COLOR, currentDay, subject.getId(), targetCharacter.getId());
    }

    @Override
    public IEvent colorSpotAppears(IColorSpot spot, int cellId) {
        return new Event(COLOR_SPOT_APPEARS, currentDay, spot.getId(), cellId);
    }

    @Override
    public IEvent colorSpotDisappears(IColorSpot spot, int cellId) {
        return new Event(COLOR_SPOT_DISAPPEARS, currentDay, spot.getId(), cellId);
    }

    @Override
    public IEvent exception(String message) {
        return new Event(EXCEPTION, currentDay, message);
    }

    @Override
    public IEvent joy(ICharacter subject, float joy) {
        return new Event(JOY, currentDay, subject.getId(), joy);
    }

    @Override
    public IEvent isFriendOf(ICharacter subject, ICharacter ... friends) {
        return new Event(
                IS_FRIEND_OF, currentDay, subject.getId(),
                Arrays.stream(friends).mapToInt(friend-> friend.getId()).toArray());
    }
    
    @Override
    public IEvent isEnemyOf(ICharacter subject, ICharacter ... enemies) {
        return new Event(
                IS_ENEMY_OF, currentDay, subject.getId(), 
                Arrays.stream(enemies).mapToInt(enemy-> enemy.getId()).toArray()
        );
    }

    @Override
    public IEvent naturalChange(Piece subject, Color currentColor, Color newColor) {
        float colorDifference = PieceUtilities.calculateColorDifference(currentColor, newColor);
        return new Event(NATURAL_CHANGE, currentDay, subject.getId(), colorToHexadecimal(currentColor), colorToHexadecimal(newColor), colorDifference);
    }
    
    @Override
    public IEvent characterAppears(ICharacter subject, int cellId) {
        Color foreground = subject.getForegroundColor()==null? Color.BLACK: subject.getForegroundColor();
        Color background = subject.getBackgroundColor()==null? Color.BLACK: subject.getBackgroundColor();
        CharacterShape shape = subject.getShape() == null? CharacterShape.CIRCLE: subject.getShape();
        
        return new Event(CHARACTER_APPEARS, currentDay, subject.getId(), colorToHexadecimal(background),
                colorToHexadecimal(background), shape.name());
    }
    
    @Override
    public IEvent trusts(ICharacter subject, ICharacter friendWithMostAffinity) {
        return new Event(TRUSTS, currentDay, subject.getId(), friendWithMostAffinity.getId());
    }

    @Override
    public IEvent givesTurn(ICharacter subject, ICharacter target) {
        return new Event(GIVES_TURN, currentDay, subject.getId(), target.getId());
    }
    
    @Override
    public IEvent areNear(ICharacter subject, ICharacter... charactersThatAreNear) {
    return new Event(
                ARE_NEAR, currentDay, subject.getId(), 
                Arrays.stream(charactersThatAreNear).mapToInt(enemy-> enemy.getId()).toArray()
        );
    }
    
    private String colorToHexadecimal(Color color){
        return String.format("#%02x%02x%02x", (int)(color.getRed()*255), (int)(color.getGreen()*255), (int)(color.getBlue()*255));
    }
}
