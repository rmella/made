/*
 * Copyright (C) 2015 rhgarcia
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
package com.velonuboso.made.core.narration.integrationtest;

import com.velonuboso.made.core.common.entity.EventsLogEntity;
import com.velonuboso.made.core.common.util.InitializationException;
import com.velonuboso.made.core.common.util.ObjectFactory;
import com.velonuboso.made.core.customization.api.ICustomization;
import com.velonuboso.made.core.customization.implementation.Customization;
import com.velonuboso.made.core.narration.api.INarrator;
import java.io.File;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

/**
 *
 * @author rhgarcia
 */
public class MonomythNarrationIntegrationTest {
    
    private INarrator narrator;
    private ICustomization customization;
    private EventsLogEntity eventsLog;
    private EventsLogEntity.EventEntity event;
    
    public MonomythNarrationIntegrationTest() {
    }
    
    @Before
    public void setUp() throws InitializationException {
        final String LOCAL_RESOURCE_MONOMYTH = "monomyth_narration_rules_en";
        
        String LocalResourceCustomizationMonomyth = ClassLoader.getSystemResource(LOCAL_RESOURCE_MONOMYTH).getFile();
        customization = new Customization();
        customization.loadFromFile(new File(LocalResourceCustomizationMonomyth));
        
        narrator = ObjectFactory.createObject(INarrator.class);
        narrator.setCustomization(customization);
        
        event = new EventsLogEntity.EventEntity(0, null, null);
        
        eventsLog = new EventsLogEntity(null, null, new EventsLogEntity.DayLog[]{
           new EventsLogEntity.DayLog(0, new EventsLogEntity.EventEntity[]{
               event
           })
        });
    }
    
    @Ignore
    @Test
    public void IT_Narrator_tells_a_story_when_conflict_is_present(){
        event.setPredicate("Conflict (0, 'Actor1', 1, 'Actor2', '4', 'thinghy thing')");
        narrator.setEventsLog(eventsLog);
        narrator.narrate();
        System.out.println(narrator.getNarration());
    }
    
}
