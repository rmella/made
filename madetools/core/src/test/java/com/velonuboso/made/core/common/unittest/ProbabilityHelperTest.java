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

import com.velonuboso.made.core.abm.api.IBehaviourTreeNode;
import com.velonuboso.made.core.abm.api.ICharacter;
import com.velonuboso.made.core.abm.api.IMap;
import com.velonuboso.made.core.abm.implementation.BehaviourTreeNode;
import com.velonuboso.made.core.common.api.IProbabilityHelper;
import com.velonuboso.made.core.common.implementation.ProbabilityHelper;
import com.velonuboso.made.core.common.util.ObjectFactory;
import java.util.HashSet;
import java.util.function.Consumer;
import java.util.stream.IntStream;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
/**
 *
 * @author Rubén Héctor García (raiben@gmail.com)
 */
public class ProbabilityHelperTest {
    
    private ProbabilityHelper probabilityHelper;
    
    public ProbabilityHelperTest() {
    }

    @Before
    public void setUp() {
        probabilityHelper = new ProbabilityHelper();
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void testSomeMethod() {
    }
    
    @Test
    public void UT_GetNextProbability_returns_numbers_between_0_amd_1(){
        final int NUMBER_OF_RETRIEVES = 100;
        boolean allBetween0and1 = IntStream.range(0, NUMBER_OF_RETRIEVES).allMatch(index -> 
                between(probabilityHelper.getNextProbability(this.getClass()), 0, 1f));
        assertTrue("All the number retrieved should be between 0 and 1", allBetween0and1);
    }
    
    @Test
    public void UT_GetNextProbability_returns_different_numbers(){
        final int NUMBER_OF_RETRIEVES = 100;
        
        HashSet<Float> elements = new HashSet<>();
        
        IntStream.range(0, NUMBER_OF_RETRIEVES).forEach(index -> 
                elements.add(probabilityHelper.getNextProbability(this.getClass())));
        assertEquals("All the number retrieved should be different", NUMBER_OF_RETRIEVES, elements.size());
    }
    
    @Test
    public void UT_getNexProbability_after_SetSeed_0_returns_a_known_number(){
        final long seed = System.currentTimeMillis();
        
        probabilityHelper.setSeed(seed);
        float firstValue = probabilityHelper.getNextProbability(this.getClass());
        
        probabilityHelper.setSeed(seed);
        float secondValue = probabilityHelper.getNextProbability(this.getClass());
        
        assertEquals("Two retrieved values after setting the same seed should be the same",
                firstValue, secondValue, 0f);
    }
    
    private boolean between (float number, float minimumInclusive, float maximumInclusive){
        return number >= minimumInclusive && number <= maximumInclusive;
    }
}
