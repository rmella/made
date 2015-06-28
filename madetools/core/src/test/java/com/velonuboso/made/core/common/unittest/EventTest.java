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
package com.velonuboso.made.core.common.unittest;

import com.velonuboso.made.core.common.implementation.Event;
import com.velonuboso.made.core.common.api.IEvent;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Rubén Héctor García (raiben@gmail.com)
 */
public class EventTest {
    
    @Test
    public void Event_returns_correct_Predicate_when_no_arguments_provided(){
        final String expectedPredicate = "Name ()";
        
        IEvent event = new Event("Name");
        assertEquals("Should've return "+expectedPredicate, expectedPredicate, event.toLogicalPredicate());
    }
    
    @Test
    public void Event_returns_correct_Predicate_when_one_argument_provided(){
        final String expectedPredicate = "Name (\"firstargument\")";
        
        IEvent event = new Event("Name", "firstargument");
        assertEquals("Should've return "+expectedPredicate, expectedPredicate, event.toLogicalPredicate());
    }
    
    @Test
    public void Event_returns_correct_Predicate_when_two_arguments_provided(){
        final String expectedPredicate = "Name (\"firstargument\", \"secondargument\")";
        
        IEvent event = new Event("Name", "firstargument", "secondargument");
        assertEquals("Should've return "+expectedPredicate, expectedPredicate, event.toLogicalPredicate());
    }
    
    @Test
    public void Event_returns_correct_Predicate_with_numbers_and_strings(){
        final String expectedPredicate = "Name (0, \"secondargument\")";
        
        IEvent event = new Event("Name", 0, "secondargument");
        assertEquals("Should've return "+expectedPredicate, expectedPredicate, event.toLogicalPredicate());
    }
    
}
