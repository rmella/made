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

import com.velonuboso.made.core.abm.api.IEventsWriter;
import com.velonuboso.made.core.common.api.IEvent;
import com.velonuboso.made.core.common.entity.EventsLogEntity;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.IntStream;

/**
 *
 * @author Rubén Héctor García (raiben@gmail.com)
 */
public class EventsWriter implements IEventsWriter {

    private StringBuilder stringBuilder;

    public EventsWriter() {
        stringBuilder =new StringBuilder();
    }

    public EventsWriter(List<IEvent> events) {
        this();
        events.forEach(event -> appendPredicateToStringBuilder(event));
    }

    @Override
    public void add(IEvent event) {
        appendPredicateToStringBuilder(event);
    }

    @Override
    public EventsLogEntity getEventsLog() {
        EventsLogEntity entity = new EventsLogEntity();
        entity.setLog(stringBuilder.toString());
        return entity;
    }
    
    private StringBuilder appendPredicateToStringBuilder(IEvent event) {
        //System.out.println(event.toLogicalPredicate());
        return stringBuilder.append(event.toLogicalPredicate()+"\n");
    }
}
