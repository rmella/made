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
package com.velonuboso.made.core.abm.implementation;

import com.velonuboso.made.core.abm.api.IEventsWriter;
import com.velonuboso.made.core.common.api.IEvent;
import com.velonuboso.made.core.common.entity.EventMood;
import com.velonuboso.made.core.common.entity.EventType;
import com.velonuboso.made.core.common.implementation.Event;
import com.velonuboso.made.core.common.implementation.EventFactory;
import com.velonuboso.made.core.common.util.ObjectFactory;
import com.velonuboso.made.core.narration.api.IEventsNarrator;
import simplenlg.framework.CoordinatedPhraseElement;
import simplenlg.framework.NLGFactory;
import simplenlg.lexicon.Lexicon;
import simplenlg.realiser.english.Realiser;

import java.util.ArrayList;

/**
 * @author Rubén Héctor García (raiben@gmail.com)
 */
public class NaturalLanguageEventsWriter extends EventsWriter implements IEventsWriter {
    ArrayList<IEvent> events = new ArrayList<>();

    @Override
    public void add(IEvent event) {
        super.add(event);
        events.add(event);
    }

    @Override
    public ArrayList<IEvent> getEvents() {
        return events;
    }

    @Override
    public String getNarration() {
        IEventsNarrator narrator = ObjectFactory.createObject(IEventsNarrator.class);
        narrator.setEvents(events);
        return narrator.narrate();
    }
}
