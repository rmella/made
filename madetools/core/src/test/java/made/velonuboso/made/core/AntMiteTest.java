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

import com.velonuboso.made.core.AntMite;
import com.velonuboso.made.interfaces.IWorld;
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
public class AntMiteTest {
    
    private AntMite character;
    
    public AntMiteTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        character = new AntMite();
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void UT_GetName_must_return_the_default_name_when_not_set(){
        assertEquals("Default name for an antroach must be Kroo", "Kroo", character.getName());
    }
    
    @Test
    public void UT_GetName_must_return_the_name_of_the_agent_when_set(){
        String expectedName = "Aaaa";
        
        character.setName(expectedName);
        assertEquals("Name could not be set to "+expectedName, expectedName, character.getName());
    }
}
