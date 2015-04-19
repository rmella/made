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
package made.velonuboso.made.core;

import com.velonuboso.made.core.Fact;
import com.velonuboso.made.core.FactFactory;
import com.velonuboso.made.interfaces.IFact;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author rhgarcia
 */
public class FactTest {
    
    @Test
    public void Fact_returns_correct_Predicate_when_no_arguments_provided(){
        final String expectedPredicate = "Name ()";
        
        IFact fact = new Fact("Name");
        assertEquals("Should've return "+expectedPredicate, expectedPredicate, fact.toLogicalPredicate());
    }
    
    @Test
    public void Fact_returns_correct_Predicate_when_one_argument_provided(){
        final String expectedPredicate = "Name (\"firstargument\")";
        
        IFact fact = new Fact("Name", "firstargument");
        assertEquals("Should've return "+expectedPredicate, expectedPredicate, fact.toLogicalPredicate());
    }
    
    @Test
    public void Fact_returns_correct_Predicate_when_two_arguments_provided(){
        final String expectedPredicate = "Name (\"firstargument\", \"secondargument\")";
        
        IFact fact = new Fact("Name", "firstargument", "secondargument");
        assertEquals("Should've return "+expectedPredicate, expectedPredicate, fact.toLogicalPredicate());
    }
    
    @Test
    public void Fact_returns_correct_Predicate_with_numbers_and_strings(){
        final String expectedPredicate = "Name (0, \"secondargument\")";
        
        IFact fact = new Fact("Name", 0, "secondargument");
        assertEquals("Should've return "+expectedPredicate, expectedPredicate, fact.toLogicalPredicate());
    }
    
}
