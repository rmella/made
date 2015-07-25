package com.velonuboso.made.core.abm.unittest;

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

import com.velonuboso.made.core.abm.implementation.Piece;
import com.velonuboso.made.core.abm.implementation.World;
import com.velonuboso.made.core.abm.api.ICharacter;
import com.velonuboso.made.core.common.api.IEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

/**
 *
 * @author rhgarcia
 */
public class WorldTest {
    
    private World world;
    
    public WorldTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        world = new World();
    }
    
    @After
    public void tearDown() {
    }

    
    @Test
    public void UT_World_with_no_inhabitants_must_not_return_pupulated_list_of_inhabitants(){
        assertFalse("Empty world has inhabitants", world.getInhabitants().size()>0);
    }
    
    @Test
    public void UT_World_with_one_inhabitant_must_return_a_pupulated_list_of_inhabitants(){
        ICharacter character = mock(ICharacter.class);
        
        world.addInhabitant(character);
        assertTrue("Non empty world has no inhabitants", world.getInhabitants().size()>0);
    }
    
    @Test
    public void UT_World_must_set_id_to_inhabitants(){
        ICharacter character = mock(ICharacter.class);
        stub(character.getId()).toReturn(null);
        world.addInhabitant(character);
        
        verify(character, times(1)).setId(anyInt());
    }
    
    @Test
    public void UT_World_must_set_id_0_to_the_first_inhabitant(){
        ICharacter character = mock(ICharacter.class);
        stub(character.getId()).toReturn(null);
        world.addInhabitant(character);
        
        verify(character, times(1)).setId(anyInt());
    }
    
    
    @Test
    public void UT_World_must_set_correlative_ids_to_two_correlative_inhabitants(){
        ICharacter firstCharacter = mock(ICharacter.class);
        stub(firstCharacter.getId()).toReturn(null);
        
        ICharacter secondCharacter = mock(ICharacter.class);
        stub(secondCharacter.getId()).toReturn(null);
        
        world.addInhabitant(firstCharacter);
        world.addInhabitant(secondCharacter);
        
        verify(firstCharacter, times(1)).setId(0);
        verify(secondCharacter, times(1)).setId(1);
    }
    
    @Test
    public void UT_World_must_set_a_different_id_to_each_inhabitant(){
        int numberOfExpectedInhabitants = 10;
        final HashSet<Integer> identifiersUsed = new HashSet<>();

        for (int i=0; i<numberOfExpectedInhabitants; i++){
            ICharacter character = mock(ICharacter.class);
            stub(character.getId()).toReturn(null);
            doAnswer(new Answer() {
                @Override
                public Object answer(InvocationOnMock invocation) throws Throwable {
                    int identifier = (Integer)(invocation.getArguments()[0]);
                    identifiersUsed.add(identifier);
                    return null;
                }
            }).when(character).setId(anyInt());
            world.addInhabitant(character);
        }
        
        assertEquals(
                numberOfExpectedInhabitants+" added but not retrieved from the world", 
                numberOfExpectedInhabitants, world.getInhabitants().size());
        assertEquals("The number of different identifiers must be 10", 
                numberOfExpectedInhabitants, identifiersUsed.size());
    }    
    
    @Test
    public void UT_World_must_have_events(){
        assertNotNull("Events of a world must not be null", world.getEvents());
    }
    
    @Test
    public void UT_World_default_time_unit_must_be_days(){
        assertEquals("Default time unit must be days", TimeUnit.DAYS, world.getTimeUnit());
    }
    
    @Test
    public void UT_World_must_use_custom_time_unit_when_configured(){
        TimeUnit timeUnit = mock(TimeUnit.class);
        world.setTimeUnit(timeUnit);
        assertEquals("World's time unit should be "+timeUnit, timeUnit, world.getTimeUnit());
    }
    
    @Test
    public void UT_World_must_return_0_run_time_units_when_never_run(){
        assertEquals("World must have run 0 days", 0, world.getRunTime());
    }
    
    @Test
    public void UT_World_must_be_able_to_run_a_number_of_time_units(){
        world.run(10);
        assertEquals("World must run 10 days", 10, world.getRunTime());
    }
    
    @Test
    public void UT_World_must_have_empty_events_when_never_run(){
        assertTrue("Events of a world never run must be empty", world.getEvents().isEmpty());
    }
    
    @Test
    public void UT_World_must_have_non_empty_events_after_running(){
        world.run(10);
        assertFalse("Events of the world afetr running must not be empty", world.getEvents().isEmpty());
    }
    
    @Test
    public void UT_World_can_tell_a_story_about_itselft(){
        world.run(10);
        assertNotNull(world.getWorldStory());
    }
    
    @Test
    public void IT_AddInhabitant_must_insert_an_event_when_called(){
        List<IEvent> oldEvents = new ArrayList<>();
        Collections.copy(oldEvents, world.getEvents());
        
        Piece character = new Piece();
        world.addInhabitant(character);
        
        assertTrue("The number of events should be higher after a character addition",
                world.getEvents().size()>oldEvents.size());
    }
}
