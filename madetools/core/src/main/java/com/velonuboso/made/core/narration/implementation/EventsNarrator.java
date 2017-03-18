/*
 * Copyright (C) 2016 Rubén Héctor García (raiben@gmail.com)
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
package com.velonuboso.made.core.narration.implementation;

import com.velonuboso.made.core.common.api.IEvent;
import com.velonuboso.made.core.common.entity.EventMood;
import com.velonuboso.made.core.common.entity.EventType;
import com.velonuboso.made.core.common.implementation.Event;
import com.velonuboso.made.core.common.implementation.EventFactory;
import com.velonuboso.made.core.narration.api.IEventsNarrator;
import simplenlg.framework.CoordinatedPhraseElement;
import simplenlg.framework.NLGFactory;
import simplenlg.lexicon.Lexicon;
import simplenlg.realiser.english.Realiser;

import java.util.ArrayList;

/**
 * @author Rubén Héctor García (raiben@gmail.com)
 */
public class EventsNarrator implements IEventsNarrator {

    int INITIAL_DAY = -1;
    EventMood INITIAL_MOOD = EventMood.NEUTRAL;
    EventType INITIAL_TYPE = EventType.ACTION;

    private Lexicon lexicon;
    private NLGFactory factory;
    private Realiser realiser;
    private ArrayList<IEvent> events;

    private float currentDay;
    private EventMood currentMood;
    private EventType currentType;

    private float newDay;
    private EventMood newMood;
    private EventType newType;

    private StringBuilder stringBuilder;

    public EventsNarrator() {
        lexicon = Lexicon.getDefaultLexicon();
        factory = new NLGFactory(lexicon);
        realiser = new Realiser(lexicon);
        reset_initial_states();
    }

    public void reset_initial_states() {
        currentDay = INITIAL_DAY;
        currentMood = INITIAL_MOOD;
        currentType = INITIAL_TYPE;
        newDay = INITIAL_DAY;
        newMood = INITIAL_MOOD;
        newType = INITIAL_TYPE;

        stringBuilder = new StringBuilder();
    }

    public void setEvents(ArrayList<IEvent> events) {
        this.events = events;
    }

    @Override
    public String narrate() {
        reset_initial_states();
        StringBuilder builder = buildPhraseFromEvents();
        return removeBlanks(builder.toString());
    }

    private StringBuilder buildPhraseFromEvents() {
        CoordinatedPhraseElement complexPhrase = factory.createCoordinatedPhrase();

        for (IEvent iEvent : events) {
            if (iEvent.toPhrase() != EventFactory.NULL_PHRASE) {
                Event event = (Event) iEvent;

                newDay = event.getDay();
                newMood = event.getMood();
                newType = event.getType();

                if (isNewParagraph()) {
                    addCurrentPhraseAsFullSentence(complexPhrase, newDay);
                    complexPhrase = factory.createCoordinatedPhrase();
                }

                update_current_states_from_new();
                complexPhrase.addCoordinate(event.toPhrase());
            }
        }
        String text = realiser.realiseSentence(complexPhrase);
        stringBuilder.append(text);
        return stringBuilder;
    }

    private void addCurrentPhraseAsFullSentence(CoordinatedPhraseElement complexPhrase, float newDay) {
        String text = realiser.realiseSentence(complexPhrase);
        stringBuilder.append(text);

        if (new_line_should_be_in_new_paragraph(newDay)) {
            continueInNewParagraph();
        } else {
            continueInCurrentParagraph();
        }
    }

    private boolean isNewParagraph() {
        return (new_line_should_be_in_new_paragraph(newDay)) ||
                (newMood == EventMood.BAD && currentMood == EventMood.GOOD) ||
                (newMood == EventMood.GOOD && currentMood == EventMood.BAD) ||
                (newType != currentType);
    }

    private void update_current_states_from_new() {
        currentDay = newDay;
        currentMood = newMood;
        currentType = newType;
    }

    private boolean new_line_should_be_in_new_paragraph(float newDay) {
        return newDay != currentDay;
    }

    private String removeBlanks(String document) {
        String improved_document = document.replaceAll("  *", " ");
        return improved_document.replaceAll("^ *", "");
    }

    private void continueInNewParagraph() {
        stringBuilder.append("\n\n");
    }

    private void continueInCurrentParagraph() {
        stringBuilder.append(" ");
    }
}
