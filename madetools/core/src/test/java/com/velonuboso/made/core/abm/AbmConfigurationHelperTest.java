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

import com.velonuboso.made.core.abm.implementation.piece.AbmConfigurationHelper;
import com.velonuboso.made.core.abm.implementation.piece.AbmConfigurationHelperWorld;
import com.velonuboso.made.core.common.api.IGlobalConfigurationFactory;
import com.velonuboso.made.core.common.entity.AbmConfigurationEntity;
import com.velonuboso.made.core.common.entity.CommonAbmConfiguration;
import com.velonuboso.made.core.common.util.ObjectFactory;
import java.util.Arrays;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Rubén Héctor García (raiben@gmail.com)
 */
public class AbmConfigurationHelperTest {

    private CommonAbmConfiguration config;
    
    public AbmConfigurationHelperTest() {
    }

    @Before
    public void setUp() {
        IGlobalConfigurationFactory globalConfigurationFactory
                = ObjectFactory.createObject(IGlobalConfigurationFactory.class);
        config = globalConfigurationFactory.getCommonAbmConfiguration();
    }

    @Test
    public void Prepare_when_chromosome_is_shorter_it_throws_exception() throws Exception {
        int size = 45;

        float[] chromosome = new float[size];
        Arrays.fill(chromosome, 0, size, 0.5f);
        boolean success = prepareAbmConfigurationHelper(chromosome);
        assertFalse("Should've thrown exception since the chromosome is shorter than required", success);
    }

    @Test
    public void Prepare_when_chromosome_is_longer_it_throws_exception() throws Exception {
        int size = 47;
        float[] chromosome = new float[size];
        Arrays.fill(chromosome, 0, size, 0.5f);
        boolean success = prepareAbmConfigurationHelper(chromosome);
        assertFalse("Should've thrown exception since the chromosome is longer than required", success);
    }

    @Test
    public void Prepare_when_chromosome_values_for_the_world_are_below_the_range_it_throws_exception() throws Exception {
        int size = 46;
        float[] chromosome = new float[size];
        chromosome[0] = config.MIN_WORLD_SIZE;
        chromosome[1] = config.MIN_NUMBER_OF_CIRCLES;
        chromosome[2] = config.MIN_NUMBER_OF_TRIANGLES;
        chromosome[3] = config.MIN_NUMBER_OF_SQUARES;
        chromosome[4] = config.MIN_NUMBER_OF_DAYS - 1;
        Arrays.fill(chromosome, 5, size, 0.5f);

        boolean success = prepareAbmConfigurationHelper(chromosome);
        assertFalse("Should've thrown exception since the fifth chromosome value is below the range", success);
    }

    @Test
    public void Prepare_when_chromosome_values_for_the_world_are_over_the_range_it_throws_exception() throws Exception {
        int size = 46;
        float[] chromosome = new float[size];
        chromosome[0] = config.MIN_WORLD_SIZE;
        chromosome[1] = config.MIN_NUMBER_OF_CIRCLES;
        chromosome[2] = config.MIN_NUMBER_OF_TRIANGLES;
        chromosome[3] = config.MIN_NUMBER_OF_SQUARES;
        chromosome[4] = config.MAX_NUMBER_OF_DAYS + 1;
        Arrays.fill(chromosome, 5, size, 0.5f);

        boolean success = prepareAbmConfigurationHelper(chromosome);
        assertFalse("Should've thrown exception since the fifth chromosome value is over the range", success);
    }

    @Test
    public void Prepare_when_chromosome_int_values_for_the_world_have_decimals_it_throws_exception() throws Exception {
        int size = 46;
        float[] chromosome = new float[size];
        chromosome[0] = config.MIN_WORLD_SIZE;
        chromosome[1] = config.MIN_NUMBER_OF_CIRCLES;
        chromosome[2] = config.MIN_NUMBER_OF_TRIANGLES;
        chromosome[3] = config.MIN_NUMBER_OF_SQUARES;
        chromosome[4] = config.MIN_NUMBER_OF_DAYS + 0.5f;
        Arrays.fill(chromosome, 5, size, 0.5f);

        boolean success = prepareAbmConfigurationHelper(chromosome);
        assertFalse("Should've thrown exception since the fifth chromosome value is over the range", success);
    }

    @Test
    public void Prepare_when_chromosome_float_values_for_the_piece_are_not_in_the_range_throws_exception() throws Exception {
        int size = 46;
        float[] chromosome = new float[size];
        chromosome[0] = config.MIN_WORLD_SIZE;
        chromosome[1] = config.MIN_NUMBER_OF_CIRCLES;
        chromosome[2] = config.MIN_NUMBER_OF_TRIANGLES;
        chromosome[3] = config.MIN_NUMBER_OF_SQUARES;
        chromosome[4] = config.MIN_NUMBER_OF_DAYS;
        Arrays.fill(chromosome, 5, size, 2f);

        boolean success = prepareAbmConfigurationHelper(chromosome);
        assertFalse("Should've thrown exception since the fifth chromosome value is over the range", success);
    }
    
    
    @Test
    public void Prepare_when_chromosome_is_valid_it_does_not_throw_exception() throws Exception {
        int size = 52;
        float[] chromosome = new float[size];
        chromosome[0] = config.MIN_WORLD_SIZE;
        chromosome[1] = config.MIN_NUMBER_OF_CIRCLES;
        chromosome[2] = config.MIN_NUMBER_OF_TRIANGLES;
        chromosome[3] = config.MIN_NUMBER_OF_SQUARES;
        chromosome[4] = config.MIN_NUMBER_OF_DAYS;
        Arrays.fill(chromosome, 5, size, 0.5f);

        boolean success = prepareAbmConfigurationHelper(chromosome);
        assertTrue("Shouldn't have thrown exception since the values have the correct type and are in the range", success);
    }
    
    private boolean prepareAbmConfigurationHelper(float[] chromosome) {
        AbmConfigurationEntity entity = new AbmConfigurationEntity(chromosome);
        AbmConfigurationHelper configurationHelper = new AbmConfigurationHelper(entity);
        boolean success = true;

        try {
            configurationHelper.prepare();
        } catch (Exception exception) {
            System.err.println(exception.getMessage());
            success = false;
        }

        return success;
    }

}
