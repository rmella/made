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

import com.velonuboso.made.core.common.entity.EventMood;
import com.velonuboso.made.core.common.entity.EventType;
import javafx.scene.paint.Color;
import org.apache.commons.lang.StringUtils;
import simplenlg.features.Feature;
import simplenlg.features.Tense;
import simplenlg.framework.CoordinatedPhraseElement;
import simplenlg.framework.NLGElement;
import simplenlg.framework.NLGFactory;
import simplenlg.lexicon.Lexicon;
import simplenlg.phrasespec.SPhraseSpec;

/**
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

    private static Lexicon lexicon;
    public static NLGFactory phraseFactory;
    public static SPhraseSpec NULL_PHRASE;

    static {
        lexicon = Lexicon.getDefaultLexicon();
        phraseFactory = new NLGFactory(lexicon);
        NULL_PHRASE = phraseFactory.createClause();
    }

    private float currentDay;
    private int lastSubjectId;
    private int lastIndirectObjectId;

    public EventFactory() {
        currentDay = 0;
        lastIndirectObjectId = -1;
        lastSubjectId = -1;
    }

    @Override
    public void setDay(float day) {
        currentDay = day;
    }

    @Override
    public IEvent newDay() {
        SPhraseSpec phrase = phraseFactory.createClause("the day " + Float.toString(currentDay), "come");
        return new Event(currentDay, EventMood.NEUTRAL, EventType.DESCRIPTION, phrase, NEW_DAY, currentDay);
    }

    @Override
    public IEvent worldExists(final IWorld world) {
        return new Event(currentDay, EventMood.NEUTRAL, EventType.DESCRIPTION, NULL_PHRASE, WORLD_EXISTS, currentDay,
                world.getTimeUnit());
    }

    @Override
    public IEvent inhabitantExists(final ICharacter inhabitant) {
        CoordinatedPhraseElement phrase = phraseFactory.createCoordinatedPhrase();
        phrase.addCoordinate(phraseFactory.createClause(inhabitant.getId(), "be born", "in " + currentDay));
        phrase.addCoordinate(phraseFactory.createClause(inhabitant.getId(), "have", inhabitant.getBackgroundColor()));
        phrase.addCoordinate(phraseFactory.createClause(inhabitant.getId(), "is", inhabitant.getShape()));

        return new Event(currentDay, EventMood.NEUTRAL, EventType.ACTION, phrase, INHABITANT_EXISTS, currentDay,
                inhabitant.getId
                        ());
    }

    @Override
    public IEvent hasFear(final ICharacter subject, final ICharacter enemy) {
        SPhraseSpec phrase = phraseFactory.createClause(getCharacterNameAsSubject(subject), "have", "fear");
        NLGElement phraseCause = phraseFactory.createClause(getCharacterNameAsObject(subject), "could", "damage it");
        phrase.setFeature(Feature.COMPLEMENTISER, "because");
        phrase.setFeature(Feature.TENSE, Tense.PAST);
        phrase.addComplement(phraseCause);

        return new Event(currentDay, EventMood.BAD, EventType.DESCRIPTION, phrase, HAS_FEAR, currentDay,
                subject.getId(), enemy.getId());
    }

    @Override
    public IEvent hasAnticipation(final ICharacter subject, final IColorSpot spot) {
        SPhraseSpec phrase = phraseFactory.createClause(getCharacterNameAsSubject(subject), "have", "anticipation");
        SPhraseSpec phraseCause = phraseFactory.createClause(getCharacterNameAsObject(subject),
                "want to get to", "spot " + Integer.toString(spot.getId()));
        phrase.setFeature(Feature.COMPLEMENTISER, "because");
        phrase.setFeature(Feature.TENSE, Tense.PAST);
        phrase.addComplement(phraseCause);
        return new Event(currentDay, EventMood.GOOD, EventType.DESCRIPTION, phrase, HAS_ANTICIPATION, currentDay,
                subject.getId(), spot.getId());
    }

    @Override
    public IEvent canImproveFriendSimilarity(ICharacter subject, ICharacter friend) {
        SPhraseSpec phrase = phraseFactory
                .createClause(getCharacterNameAsSubject(subject), "want to help", Integer.toString(friend.getId()));
        return new Event(currentDay, EventMood.GOOD, EventType.DESCRIPTION, phrase, CAN_IMPROVE_FRIEND_SIMILARITY,
                currentDay, subject.getId(), friend.getId());
    }

    @Override
    public IEvent canImproveSelfSimilarity(ICharacter subject, IColorSpot spot) {
        SPhraseSpec phrase =
                phraseFactory.createClause(getCharacterNameAsSubject(subject), "can improve", "self-similarity");
        return new Event(currentDay, EventMood.GOOD, EventType.DESCRIPTION, phrase, CAN_IMPROVE_SELF_SIMILARITY,
                currentDay, subject.getId(),
                spot.getId());
    }

    @Override
    public IEvent canReduceEnemySimilarity(ICharacter subject, ICharacter enemy) {
        SPhraseSpec phrase =
                phraseFactory.createClause(getCharacterNameAsSubject(subject), "can reduce", "self-similarity");
        return new Event(currentDay, EventMood.GOOD, EventType.DESCRIPTION, phrase, CAN_REDUCE_ENEMY_SIMILARITY,
                currentDay, subject.getId(),
                enemy.getId());
    }

    @Override
    public IEvent error(ICharacter subject, String message) {
        return new Event(currentDay, EventMood.NEUTRAL, EventType.DESCRIPTION, NULL_PHRASE, ERROR, subject.getId(),
                message);
    }

    @Override
    public IEvent isSad(ICharacter subject) {

        SPhraseSpec phrase = phraseFactory.createClause(getCharacterNameAsSubject(subject), "be", "sad");
        return new Event(currentDay, EventMood.BAD, EventType.DESCRIPTION, phrase, IS_SAD, currentDay, subject.getId());
    }

    @Override
    public IEvent isSurprised(ICharacter subject) {
        SPhraseSpec phrase = phraseFactory.createClause(getCharacterNameAsSubject(subject), "be", "surprised");
        return new Event(currentDay, EventMood.BAD, EventType.DESCRIPTION, phrase, IS_SURPRISED, currentDay,
                subject.getId());
    }

    @Override
    public IEvent movesAway(ICharacter subject, int cellId) {
        SPhraseSpec phrase = phraseFactory.createClause(getCharacterNameAsSubject(subject), "moves ");
        phrase.addComplement("away");
        return new Event(currentDay, EventMood.BAD, EventType.ACTION, phrase, MOVES_AWAY, currentDay, subject.getId(),
                cellId);
    }

    @Override
    public IEvent moves(ICharacter subject, int cellId) {
        SPhraseSpec phrase = phraseFactory.createClause(getCharacterNameAsSubject(subject), "move");
        phrase.addComplement("to " + Integer.toString(cellId));

        return new Event(currentDay, EventMood.NEUTRAL, EventType.ACTION, phrase, MOVES, currentDay, subject.getId(),
                cellId);
    }

    @Override
    public IEvent displaces(ICharacter subject, ICharacter targetCharacter, int cellId) {
        SPhraseSpec phrase = phraseFactory.createClause(getCharacterNameAsSubject(subject),
                "displace", Integer.toString(targetCharacter.getId()));
        return new Event(currentDay, EventMood.BAD, EventType.ACTION, phrase, DISPLACES, currentDay, subject.getId(),
                targetCharacter.getId(), cellId);
    }

    @Override
    public IEvent skipsTurn(ICharacter subject) {
        SPhraseSpec phrase = phraseFactory.createClause(getCharacterNameAsSubject(subject), "skip", "turn");
        return new Event(currentDay, EventMood.NEUTRAL, EventType.ACTION, phrase, SKIPS_TURN, currentDay,
                subject.getId());
    }

    @Override
    public IEvent stains(ICharacter subject, IColorSpot targetSpot) {
        SPhraseSpec phrase = phraseFactory.createClause(getCharacterNameAsSubject(subject), "get color");
        phrase.addComplement("from the spot " + Integer.toString(targetSpot.getId()));
        return new Event(currentDay, EventMood.GOOD, EventType.ACTION, phrase, STAINS, currentDay, subject.getId(),
                targetSpot.getId());
    }

    @Override
    public IEvent transfersColor(ICharacter subject, ICharacter targetCharacter) {
        SPhraseSpec phrase = phraseFactory.createClause(getCharacterNameAsSubject(subject), "transfer color");
        phrase.addComplement("to " + Integer.toString(targetCharacter.getId()));
        return new Event(currentDay, EventMood.GOOD, EventType.ACTION, phrase, TRANSFERS_COLOR, currentDay,
                subject.getId(),
                targetCharacter.getId());
    }

    @Override
    public IEvent colorSpotAppears(IColorSpot spot, int cellId) {
        SPhraseSpec phrase = phraseFactory.createClause("spot " + Integer.toString(spot.getId()), "appear");
        phrase.addComplement("in cell " + Integer.toString(cellId));
        return new Event(currentDay, EventMood.NEUTRAL, EventType.ACTION, phrase, COLOR_SPOT_APPEARS, currentDay,
                spot.getId(), cellId);
    }

    @Override
    public IEvent colorSpotDisappears(IColorSpot spot, int cellId) {
        SPhraseSpec phrase = phraseFactory.createClause("spot " + Integer.toString(spot.getId()), "dissapear");
        phrase.addComplement("from cell " + Integer.toString(cellId));
        return new Event(currentDay, EventMood.BAD, EventType.ACTION, phrase, COLOR_SPOT_DISAPPEARS, currentDay,
                spot.getId(), cellId);
    }

    @Override
    public IEvent exception(String message) {
        return new Event(currentDay, EventMood.NEUTRAL, EventType.DESCRIPTION, NULL_PHRASE, EXCEPTION, currentDay,
                message);
    }

    @Override
    public IEvent joy(ICharacter subject, float joy) {
        SPhraseSpec phrase = phraseFactory.createClause(getCharacterNameAsSubject(subject), "is");
        EventMood mood;
        if (joy > 0.5) {
            mood = EventMood.GOOD;
            phrase.addComplement("happy");
        } else {
            mood = EventMood.BAD;
            phrase.addComplement("sad");
        }

        return new Event(currentDay, mood, EventType.DESCRIPTION, phrase, JOY, currentDay, subject.getId(), joy);
    }

    @Override
    public IEvent isFriendOf(ICharacter subject, ICharacter... friends) {

        SPhraseSpec phrase;
        if (friends.length > 0) {
            phrase = phraseFactory.createClause(getCharacterNameAsSubject(subject), "be", "friend of");
            CoordinatedPhraseElement phraseFriends = phraseFactory.createCoordinatedPhrase();
            for (ICharacter friend : friends) {
                phraseFriends.addCoordinate(phraseFactory.createNounPhrase(getCharacterNameAsObject(friend)));
            }
            phrase.addComplement(phraseFriends);
        } else {
            phrase = phraseFactory.createClause(getCharacterNameAsSubject(subject), "have", "no friends");
        }

        return new Event(currentDay, EventMood.GOOD, EventType.DESCRIPTION, phrase,
                IS_FRIEND_OF, currentDay, subject.getId(),
                Arrays.stream(friends).mapToInt(friend -> friend.getId()).toArray());
    }

    @Override
    public IEvent isEnemyOf(ICharacter subject, ICharacter... enemies) {
        SPhraseSpec phrase;
        if (enemies.length > 0) {
            phrase = phraseFactory.createClause(getCharacterNameAsSubject(subject), "be", "enemy of");
            CoordinatedPhraseElement phraseEnemies = phraseFactory.createCoordinatedPhrase();
            for (ICharacter enemy : enemies) {
                phraseEnemies.addCoordinate(phraseFactory.createNounPhrase(getCharacterNameAsObject(enemy)));
            }
            phrase.addComplement(phraseEnemies);
        } else {
            phrase = phraseFactory.createClause(getCharacterNameAsSubject(subject), "have", "no enemies");
        }
        return new Event(currentDay, EventMood.BAD, EventType.DESCRIPTION, phrase,
                IS_ENEMY_OF, currentDay, subject.getId(),
                Arrays.stream(enemies).mapToInt(enemy -> enemy.getId()).toArray()
        );
    }

    @Override
    public IEvent naturalChange(Piece subject, Color currentColor, Color newColor) {
        SPhraseSpec phrase = phraseFactory.createClause(getCharacterNameAsSubject(subject), "change");
        float colorDifference = PieceUtilities.calculateColorDifference(currentColor, newColor);
        return new Event(currentDay, EventMood.NEUTRAL, EventType.ACTION, phrase, NATURAL_CHANGE, currentDay,
                subject.getId(),
                colorToHexadecimal(currentColor), colorToHexadecimal(newColor), colorDifference);
    }

    @Override
    public IEvent characterAppears(ICharacter subject, int cellId) {
        SPhraseSpec phrase = phraseFactory.createClause(getCharacterNameAsSubject(subject), "appear");
        phrase.addComplement("in cell " + Integer.toString(cellId));

        Color foreground = subject.getForegroundColor() == null ? Color.BLACK : subject.getForegroundColor();
        Color background = subject.getBackgroundColor() == null ? Color.BLACK : subject.getBackgroundColor();
        CharacterShape shape = subject.getShape() == null ? CharacterShape.CIRCLE : subject.getShape();

        return new Event(currentDay, EventMood.GOOD, EventType.ACTION, phrase, CHARACTER_APPEARS, currentDay,
                subject.getId(),
                colorToHexadecimal(foreground), colorToHexadecimal(background), shape.name());
    }

    @Override
    public IEvent trusts(ICharacter subject, ICharacter friendWithMostAffinity) {
        SPhraseSpec phrase = phraseFactory.createClause(getCharacterNameAsSubject(subject), "trust");
        phrase.addComplement("in " + Integer.toString(friendWithMostAffinity.getId()));
        return new Event(currentDay, EventMood.GOOD, EventType.ACTION, phrase, TRUSTS, currentDay, subject.getId(),
                friendWithMostAffinity.getId());
    }

    @Override
    public IEvent givesTurn(ICharacter subject, ICharacter target) {
        SPhraseSpec phrase = phraseFactory.createClause(getCharacterNameAsSubject(subject), "give", "turn");
        phrase.addComplement("to " + Integer.toString(target.getId()));
        return new Event(currentDay, EventMood.GOOD, EventType.ACTION, phrase, GIVES_TURN, currentDay, subject.getId(),
                target.getId());
    }

    @Override
    public IEvent areNear(ICharacter subject, ICharacter... charactersThatAreNear) {

        String phraseSubject = getCharacterNameAsSubject(subject);
        CoordinatedPhraseElement phraseNeighbours = phraseFactory.createCoordinatedPhrase();
        for (ICharacter neighbor : charactersThatAreNear) {
            phraseNeighbours.addCoordinate(phraseFactory.createNounPhrase(getCharacterNameAsObject(neighbor)));
        }
        SPhraseSpec phrase = phraseFactory.createClause(phraseSubject, "be near", phraseNeighbours);

        return new Event(currentDay, EventMood.NEUTRAL, EventType.DESCRIPTION, phrase,
                ARE_NEAR, currentDay, subject.getId(),
                Arrays.stream(charactersThatAreNear).mapToInt(enemy -> enemy.getId()).toArray()
        );
    }

    private String colorToHexadecimal(Color color) {
        return String.format("#%02x%02x%02x", (int) (color.getRed() * 255), (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }

    private String getCharacterNameAsSubject(ICharacter character) {
        if (lastSubjectId == character.getId()) {
            return "it";
        } else {
            lastSubjectId = character.getId();
            return StringUtils.capitalize(character.getShape() + Integer.toString(character.getId()));
        }
    }

    private String getCharacterNameAsObject(ICharacter character) {
        return StringUtils.capitalize(character.getShape() + Integer.toString(character.getId()));
    }
}
