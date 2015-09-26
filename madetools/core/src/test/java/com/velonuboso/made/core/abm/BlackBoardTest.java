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
package com.velonuboso.made.core.abm;

import com.velonuboso.made.core.abm.api.IBlackBoard;
import com.velonuboso.made.core.abm.implementation.BlackBoard;
import com.velonuboso.made.core.common.util.ObjectFactory;
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
public class BlackBoardTest {
    
    private static final String SAMPLE_KEY = "SAMPLE_KEY"; 
    private static final int SAMPLE_INT = 42; 
    private static final float SAMPLE_FLOAT = 42.42f; 
    private static final String SAMPLE_STRING = "SAMPLE_STRING"; 
    
    private BlackBoard blackBoard;
    
    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
       blackBoard = new BlackBoard();
    }
    
    @After
    public void tearDown() {
    }


    @Test
    public void UT_BlackBoard_must_be_created_correctly_with_ObjectFactory() {
        Class EXPECTED_CLASS = BlackBoard.class;

        IBlackBoard sampleBlackBoard = ObjectFactory.createObject(IBlackBoard.class);
        assertEquals("ObjectFactory should've created a " + EXPECTED_CLASS.getSimpleName()
                + " as default implementation", EXPECTED_CLASS, sampleBlackBoard.getClass());
    }
    
    @Test
    public void UT_BlackBoard_must_get_the_default_int_when_not_previously_set() {
        int retrievedValue = blackBoard.getInt(SAMPLE_KEY);
        assertEquals("Inserted value should be equal to default", 
                retrievedValue, BlackBoard.DEFAULT_INT_VALUE);
    }
    
    @Test
    public void UT_BlackBoard_must_get_the_valid_int_when_previously_set() {
        blackBoard.setInt(SAMPLE_KEY, SAMPLE_INT);
        int retrievedValue = blackBoard.getInt(SAMPLE_KEY);
        assertEquals("Inserted value should be equal to retrieved value", 
                retrievedValue, SAMPLE_INT);
    }
    
    @Test
    public void UT_BlackBoard_must_get_the_default_float_when_not_previously_set() {
        float retrievedValue = blackBoard.getFloat(SAMPLE_KEY);
        assertEquals("Inserted value should be equal to default", 
                retrievedValue, BlackBoard.DEFAULT_FLOAT_VALUE,0 );
    }
    
    @Test
    public void UT_BlackBoard_must_get_the_valid_float_when_previously_set() {
        blackBoard.setFloat(SAMPLE_KEY, SAMPLE_FLOAT);
        float retrievedValue = blackBoard.getFloat(SAMPLE_KEY);
        assertEquals("Inserted value should be equal to retrieved value", 
                retrievedValue, SAMPLE_FLOAT, 0);
    }
    
    @Test
    public void UT_BlackBoard_must_get_the_default_string_when_not_previously_set() {
        String retrievedValue = blackBoard.getString(SAMPLE_KEY);
        assertEquals("Inserted value should be equal to default", 
                retrievedValue, BlackBoard.DEFAULT_STRING_VALUE);
    }
    
    @Test
    public void UT_BlackBoard_must_get_the_valid_string_when_previously_set() {
        blackBoard.setString(SAMPLE_KEY, SAMPLE_STRING);
        String retrievedValue = blackBoard.getString(SAMPLE_KEY);
        assertEquals("Inserted value should be equal to retrieved value", 
                retrievedValue, SAMPLE_STRING);
    }
    
    
    @Test
    public void UT_BlackBoard_must_get_the_default_object_when_not_previously_set() {
        Object retrievedValue = blackBoard.getObject(SAMPLE_KEY);
        assertEquals("Inserted value should be equal to default", 
                retrievedValue, null);
    }
    
    @Test
    public void UT_BlackBoard_must_get_the_valid_object_when_previously_set() {
        blackBoard.setObject(SAMPLE_KEY, SAMPLE_STRING);
        Object retrievedValue = blackBoard.getObject(SAMPLE_KEY);
        assertEquals("Inserted value should be equal to retrieved value", 
                retrievedValue, SAMPLE_STRING);
    }
}
