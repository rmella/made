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

package com.velonuboso.made.core.narration.api;

import com.velonuboso.made.core.common.api.IEvent;
import com.velonuboso.made.core.common.entity.EventsLogEntity;
import com.velonuboso.made.core.common.util.ImplementedBy;
import com.velonuboso.made.core.customization.api.ICustomization;
import com.velonuboso.made.core.narration.implementation.EventsNarrator;
import com.velonuboso.made.core.narration.implementation.Narrator;

import java.util.ArrayList;

/**
 * @author Rubén Héctor García (raiben@gmail.com)
 */
@ImplementedBy(targetClass = EventsNarrator.class, targetMode = ImplementedBy.Mode.NORMAL)
public interface IEventsNarrator {
    void setEvents(ArrayList<IEvent> events);
    String narrate();
}
