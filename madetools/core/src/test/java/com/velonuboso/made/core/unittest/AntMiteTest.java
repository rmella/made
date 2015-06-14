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
package com.velonuboso.made.core.unittest;

import com.velonuboso.made.core.implementation.AntMite;
import com.velonuboso.made.core.api.IFiniteStateAutomaton;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Rubén Héctor García (raiben@gmail.com)
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
    
    @Test
    public void UT_GetFiniteStateAutomaton_must_return_a_valid_instance(){
        IFiniteStateAutomaton fsa = character.getFiniteStateAutomaton();
        assertNotNull("Finite state automaton must ve a valid instance", fsa);
    }
    
    
    // PRIORITY: 10
    // an antmite can move around the map, a certain number of cells
    // an antmite cannot move through obstacles
    // an antmite can feel other living creatures
    // an antmite that is hungry, looks for food
    // an antmite can eat food that is adjacent to it
    // an antmite can die of starvation
    // an antmite can feel other things as more or less appetizing
    // an antmite likes to be around other antmites
    // an antmite can eat other animals
    // an antmite can attack other animals to eat
    // an antmite can attack other animals to defend themselves
    // an antmite can help other antmites to attack other animals
    // an antmite can be scared of other animals
    // an antmite can flee of scaring animals
    
    // PRIORITY: 9
    // an antmite can be older or younger
    // an antmite can feed its litter
    
    // PRIORITY: 8
    // an antmite can eat animals and vegetables
    // an antmite can snoop/ nose around
    // an antmite can look for help
    
    // PRIORITY: 7
    // an antmite can ambush/spy
    // an antmite can feel agrevisity
    
    // PRIORITY: 6
    // an antmite can attack for fear
    // an antmite can stay near other animal withput attacking to it
    
    // PRIORITY 4
    // an antmite can have offspring
    // an antmite can pierce the land
    
    // PRIORITY: 3
    // an antmite can attack other antmite
    
    // PRIORITY: 2
    // an antmite can pair
    // an antmite can be friend of other antmite
    // an antmire can be family of other antmites
    
    
}
