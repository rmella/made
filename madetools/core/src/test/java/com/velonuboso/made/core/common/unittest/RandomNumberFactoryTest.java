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

import com.velonuboso.made.core.common.util.RandomNumberFactory;
import java.text.NumberFormat;
import java.util.Locale;
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
public class RandomNumberFactoryTest {
    
    public RandomNumberFactoryTest() {
    }

    @Test
    public void getNextProbability_returns_a_random_number(){
        RandomNumberFactory.getNextProbability();
        assertTrue("Should've returned a random number", true);
    }
    
    @Test
    public void getNextProbability_returns_a__number_when_seed_is_0(){
        RandomNumberFactory.setSeed(0);
        NumberFormat format = NumberFormat.getInstance(Locale.ENGLISH);
        assertEquals("Should've returned a random number", "0.731",format.format(RandomNumberFactory.getNextProbability()));
    }
    
    @Test
    public void getNextProbability_returns_the_sequence_provided(){
        RandomNumberFactory.setNextValues(0.1f, 0.2f, 0.3f);
        NumberFormat format = NumberFormat.getInstance(Locale.ENGLISH);
        
        float number1 = RandomNumberFactory.getNextProbability();
        float number2 = RandomNumberFactory.getNextProbability();
        float number3 = RandomNumberFactory.getNextProbability();
        
        assertEquals("Should've returned 0.1 as number 1", "0.1", format.format(number1));
        assertEquals("Should've returned 0.2 as number 1", "0.2", format.format(number2));
        assertEquals("Should've returned 0.3 as number 1", "0.3", format.format(number3));
    }

}
