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
package com.velonuboso.made.core.abm.unittest;

import com.velonuboso.made.core.abm.api.IBehaviourTree;
import com.velonuboso.made.core.abm.implementation.piece.Piece;
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
public class PieceTest {
    
    private Piece character;
    
    public PieceTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        character = new Piece();
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
    public void UT_GetBehaviourTree_must_return_a_valid_instance(){
        IBehaviourTree bt = character.getBehaviourTree();
        assertNotNull("BehaviourTree must have a valid instance", bt);
    }
}
