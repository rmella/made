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
package com.velonuboso.made.core.optimization;

import com.velonuboso.made.core.common.api.IGlobalConfigurationFactory;
import com.velonuboso.made.core.common.entity.CommonAbmConfiguration;
import com.velonuboso.made.core.common.entity.CommonEcConfiguration;
import com.velonuboso.made.core.common.util.ObjectFactory;
import com.velonuboso.made.core.optimization.api.IOptimizer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.Ignore;

/**
 *
 * @author Rubén Héctor García (raiben@gmail.com)
 */
public class OptimizerIT {
    
    private IOptimizer optimizer;
    
    public OptimizerIT() {
    }
    
    @Before
    public void setUp() {
        optimizer = ObjectFactory.createObject(IOptimizer.class);
    }
    
    @After
    public void tearDown() {
    }

    @Ignore
    @Test
    public void optimizer_can_run_50_executions() {
        
        IGlobalConfigurationFactory globalConfigurationFactory = 
            ObjectFactory.createObject(IGlobalConfigurationFactory.class);
        CommonEcConfiguration config = globalConfigurationFactory.getCommonEcConfiguration();
        
        config.MAXIMUM_ITERATIONS = 50;
        optimizer.run();
    }
    
    @Test
    public void tuprolog_does_not_thrown_exception_when_using_45_agents_500_days() {
        IGlobalConfigurationFactory globalConfigurationFactory = 
            ObjectFactory.createObject(IGlobalConfigurationFactory.class);
        CommonEcConfiguration config = globalConfigurationFactory.getCommonEcConfiguration();

        config.MAXIMUM_ITERATIONS = 0;
        config.POPULATION_SIZE = 2;
        config.NUMBER_OF_TRIALS = 1;
        
        CommonAbmConfiguration abmConfig = globalConfigurationFactory.getCommonAbmConfiguration();
        abmConfig.MAX_NUMBER_OF_CIRCLES = 15;
        abmConfig.MIN_NUMBER_OF_CIRCLES = 15;
        abmConfig.MAX_NUMBER_OF_TRIANGLES = 15;
        abmConfig.MIN_NUMBER_OF_TRIANGLES = 15;
        abmConfig.MAX_NUMBER_OF_SQUARES = 15;
        abmConfig.MIN_NUMBER_OF_SQUARES = 15;
        abmConfig.MIN_NUMBER_OF_DAYS = 500;
        abmConfig.MAX_NUMBER_OF_DAYS = 500;
        abmConfig.MAX_WORLD_SIZE = 100;
        abmConfig.MIN_WORLD_SIZE = 100;

        
        optimizer.run();
    }
}
