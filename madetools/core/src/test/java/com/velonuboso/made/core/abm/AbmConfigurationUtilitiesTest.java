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

import com.velonuboso.made.core.abm.implementation.piece.AbmConfigurationHelperPiece;
import com.velonuboso.made.core.abm.implementation.piece.AbmConfigurationHelperWorld;
import com.velonuboso.made.core.abm.implementation.piece.AbmConfigurationUtilities;
import com.velonuboso.made.core.common.entity.AbmConfigurationEntity;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

/**
 *
 * @author Rubén Héctor García (raiben@gmail.com)
 */
public class AbmConfigurationUtilitiesTest {

    public AbmConfigurationUtilitiesTest() {
    }

    @Before
    public void setUp() {
    }

    @Test
    public void ValueIsProbability__when_value_is_0_it_returns_true() {
        boolean result = AbmConfigurationUtilities.valueIsProbability(0);
        assertTrue("Should've returned true since 0 is a probability", result);
    }

    @Test
    public void ValueIsProbability__when_value_is_1_it_returns_true() {
        boolean result = AbmConfigurationUtilities.valueIsProbability(1);
        assertTrue("Should've returned true since 1 is a probability", result);
    }

    @Test
    public void ValueIsProbability__when_value_is_1_1_it_returns_false() {
        boolean result = AbmConfigurationUtilities.valueIsProbability(1.1f);
        assertFalse("Should've returned true since 1.1 is not a probability", result);
    }

    @Test
    public void ValueIsProbability__when_value_is_minus_0_1_it_returns_false() {
        boolean result = AbmConfigurationUtilities.valueIsProbability(-0.1f);
        assertFalse("Should've returned true since -0.1 is not a probability", result);
    }

    @Test
    public void CheckValueInteger_when_from_5_to_20_and_value_is_5_it_throws_exception() {
        boolean result = tryCheckValueIsBetween5And20(5);
        assertTrue("Shouldn't have thrown exception since 5 is in the range", result);
    }

    @Test
    public void CheckValueInteger_when_from_5_to_20_and_value_is_20_it_throws_exception() {
        boolean result = tryCheckValueIsBetween5And20(20);
        assertTrue("Shouldn't have thrown exception since 20 is in the range", result);
    }

    @Test
    public void CheckValueInteger_when_from_5_to_20_and_value_is_4_it_throws_exception() {
        boolean result = tryCheckValueIsBetween5And20(4);
        assertFalse("Should've thrown exception since 4 is not in the range", result);
    }

    @Test
    public void CheckValueInteger_when_from_5_to_20_and_value_is_21_it_throws_exception() {
        boolean result = tryCheckValueIsBetween5And20(21);
        assertFalse("Should've thrown exception since 21 is not in the range", result);
    }

    @Test
    public void CheckValueInteger_when_from_5_to_20_and_value_is_5_001_it_throws_exception() {
        boolean result = tryCheckValueIsBetween5And20(5.001f);
        assertFalse("Should've thrown exception since 5.001 is not an integer", result);
    }

    @Test
    public void CheckValueProbability_value_is_0_5_throws_exception() throws Exception {
        boolean result = tryCheckValueIsProbability(0.5f);
        assertTrue("Shouldn't have thrown exception since 0.5 is not a probability", result);
    }
    
    @Test
    public void CheckValueProbability_value_is_2_throws_exception() throws Exception {
        boolean result = tryCheckValueIsProbability(2);
        assertFalse("Should've thrown exception since 2 is not a probability", result);
    }

    @Test
    public void GetGene_when_th_second_gene_is_retrieved_it_resturns_the_second_element_of_the_chromosome() {
        AbmConfigurationEntity entity = new AbmConfigurationEntity(new float[]{0f,1f,2f, 3f, 4f, 5f});
        
        float value = AbmConfigurationUtilities.getGene(AbmConfigurationHelperWorld.Gene.NUMBER_OF_CIRCLES, entity);
        assertEquals("Should've returned 1 since the position of the gene NUMBER_OF_CIRCLES is the second", 
                1, value, 0);
        
        value = AbmConfigurationUtilities.getGene(AbmConfigurationHelperPiece.Gene.FOREGROUND_COLOR_SIMILARITY_WEIGHT, entity);
        assertEquals("Should've returned 1 since the position of the gene FOREGROUND_COLOR_SIMILARITY_WEIGHT is the second", 
                1, value, 0);
        
    }

    private boolean tryCheckValueIsBetween5And20(float value) {
        boolean result;
        try {
            AbmConfigurationUtilities.checkValueInteger(value, 0, 5, 20);
            result = true;
        } catch (Exception exception) {
            result = false;
        }
        return result;
    }

    private boolean tryCheckValueIsProbability(float value) {
        boolean result;
        try {
            AbmConfigurationUtilities.checkValueProbability(value, 0);
            result = true;
        } catch (Exception exception) {
            result = false;
        }
        return result;
    }
}
