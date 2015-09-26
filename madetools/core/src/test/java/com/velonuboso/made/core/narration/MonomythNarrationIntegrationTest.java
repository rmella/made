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
package com.velonuboso.made.core.narration;

import com.velonuboso.made.core.common.entity.EventsLogEntity;
import com.velonuboso.made.core.common.util.InitializationException;
import com.velonuboso.made.core.common.util.ObjectFactory;
import com.velonuboso.made.core.customization.api.ICustomization;
import com.velonuboso.made.core.narration.api.INarrator;
import java.io.File;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

/**
 *
 * @author Rubén Héctor García (raiben@gmail.com)
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
        final String LOCAL_RESOURCE_MONOMYTH = "monomyth_narration_rules_en.json";
        
        String LocalResourceCustomizationMonomyth = ClassLoader.getSystemResource(LOCAL_RESOURCE_MONOMYTH).getFile();
        customization = ObjectFactory.createObject(ICustomization.class);
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
    
    @Test
    public void IT_Narrator_tells_no_story_when_predicate_signature_does_not_match(){
        final String expectedNarration = "";
        
        event.setPredicate("Conflict (0, 'Iñigo Montoya', 1, '\"six-fingered man\"', '4', 'the death of Iñigo's father')");
        narrator.setEventsLog(eventsLog);
        narrator.narrate();
        
        assertEquals("Should've retrieved no narration", expectedNarration, narrator.getNarration());
    }
    
    @Test
    public void IT_Narrator_tells_a_story_when_conflict_is_present(){
        final String expectedNarration = "The day 0, there was a conflict "
                + "between Iñigo Montoya and the "
                + "\"six-fingered man\" because of the death of "
                + "Iñigo's father.";
        
        event.setPredicate("Conflict (0, 0, 'Iñigo Montoya', 1, 'the \"six-fingered man\"', '4', 'the death of Iñigo's father')");
        narrator.setEventsLog(eventsLog);
        narrator.narrate();
        
        assertEquals("Should've retrieved no narration", expectedNarration, narrator.getNarration());
    }
    
    @Test
    public void IT_Narrator_tells_a_story_when_help_is_present(){
        final String expectedNarration = "The day 0, Iñigo Montoya helped "
                + "Westley with his plan to enter into the castle.";
        
        event.setPredicate("Helps (0, 0, 'Iñigo Montoya', 1, 'Westley', '4', 'his plan to enter into the castle.')");
        narrator.setEventsLog(eventsLog);
        narrator.narrate();
        
        assertEquals("Should've retrieved no narration", expectedNarration, narrator.getNarration());
    }
    
    @Test
    public void IT_Narrator_tells_a_story_when_conflict_and_help_is_present(){
        final String expectedNarration = "The day 0, there was a conflict "
                + "between Iñigo Montoya and the "
                + "\"six-fingered man\" because of the death of "
                + "Iñigo's father. The day 1, Iñigo Montoya helped Westley "
                + "with his plan to enter into the castle.";
        
        
        EventsLogEntity.EventEntity firstEvent = new EventsLogEntity.EventEntity(0, null, 
                "Conflict (0, 0, 'Iñigo Montoya', 1, 'the \"six-fingered man\"', '4', 'the death of Iñigo's father')"
        );
        EventsLogEntity.EventEntity secondEvent = new EventsLogEntity.EventEntity(0, null, 
                "Helps (1, 0, 'Iñigo Montoya', 2, 'Westley', '20', 'his plan to enter into the castle.')"
        );
        
        eventsLog = new EventsLogEntity(null, null, new EventsLogEntity.DayLog[]{
           new EventsLogEntity.DayLog(0, new EventsLogEntity.EventEntity[]{
               firstEvent, secondEvent
           })
        });
        
        narrator.setEventsLog(eventsLog);
        narrator.narrate();
        
        assertEquals("Should've retrieved expected narration", expectedNarration, narrator.getNarration());
    }
}
