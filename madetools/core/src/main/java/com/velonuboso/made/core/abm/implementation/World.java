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
package com.velonuboso.made.core.abm.implementation;

import com.velonuboso.made.core.common.implementation.EventFactory;
import com.velonuboso.made.core.abm.api.IWorld;
import com.velonuboso.made.core.abm.api.ICharacter;
import com.velonuboso.made.core.common.api.IEvent;
import com.velonuboso.made.core.common.api.IEventFactory;
import com.velonuboso.made.core.common.util.ObjectFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * world class
 *
 * @author Rubén Héctor García (raiben@gmail.com)
 */
public class World implements IWorld {

    private final List<ICharacter> inhabitants;
    private int identifiersSequence;
    private final List<IEvent> events;
    private TimeUnit timeUnit;
    private int runTime;
    private IEventFactory eventFactory;

    public World() {
        inhabitants = new ArrayList<>();
        identifiersSequence = 0;
        events = new ArrayList<>();
        timeUnit = TimeUnit.DAYS;
        runTime = 0;
        eventFactory = ObjectFactory.createObject(IEventFactory.class);
    }

    @Override
    public List<ICharacter> getInhabitants() {
        return inhabitants;
    }

    @Override
    public void addInhabitant(ICharacter character) {
        if (character.getId() == null) {
            character.setId(getNextIdentifier());
        }
        character.setEventsWriter(new EventsWriter(events));
        inhabitants.add(character);
        events.add(eventFactory.inhabitantExists(character));
    }

    private synchronized int getNextIdentifier() {
        return identifiersSequence++;
    }

    @Override
    public List<IEvent> getEvents() {
        return events;
    }

    @Override
    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    @Override
    public void setTimeUnit(TimeUnit timeUnit) {
        this.timeUnit = timeUnit;
    }

    @Override
    public void run(int expectedExecutionDays) {
        events.add(getConfigurationEvent());

        for (runTime = 0; runTime < expectedExecutionDays; runTime++) {

        }
        //TODO What should happen if run is called twice?
    }

    @Override
    public int getRunTime() {
        return runTime;
    }

    private IEvent getConfigurationEvent() {
        return eventFactory.worldExists(this);
    }

    @Override
    public String getEventsAsString() {
        StringBuilder story = new StringBuilder();
        for (int eventIterator = 0; eventIterator < events.size(); eventIterator++) {
            IEvent event = events.get(eventIterator);
            story.append(event.toLogicalPredicate());
            if (eventIterator<events.size()-1){
                story.append("\n");
            }
        }
        return story.toString();
    }

    public Object getWorldStory() {
        return new Object();
    }
}
