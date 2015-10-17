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

package com.velonuboso.made.core.experiments.implementation;

import com.velonuboso.made.core.common.api.IGlobalConfigurationFactory;
import com.velonuboso.made.core.common.entity.CommonAbmConfiguration;
import com.velonuboso.made.core.common.entity.CommonEcConfiguration;
import com.velonuboso.made.core.common.util.ObjectFactory;
import com.velonuboso.made.core.ec.api.IGeneticAlgorithmListener;
import com.velonuboso.made.core.ec.implementation.listeners.ExcelWriterGeneticAlgorithmListener;
import com.velonuboso.made.core.experiments.api.IExperiment;
import com.velonuboso.made.core.optimization.api.IOptimizer;

/**
 *
 * @author Rubén Héctor García (raiben@gmail.com)
 */
public class ExperimentEvostar2016 extends BaseExperiment{
    
    @Override
    public String getDescription() {
        return "Experiment for EvoGames 2016";
    }
    
    @Override
    public void run(String[] arguments) {
        ExcelWriterGeneticAlgorithmListener listener = new ExcelWriterGeneticAlgorithmListener();
        ObjectFactory.installMock(IGeneticAlgorithmListener.class, listener);
        
        IGlobalConfigurationFactory globalConfigurationFactory = 
            ObjectFactory.createObject(IGlobalConfigurationFactory.class);
        CommonEcConfiguration config = globalConfigurationFactory.getCommonEcConfiguration();
        config.MAXIMUM_ITERATIONS = 10000;
        config.POPULATION_SIZE = 50;
        config.NUMBER_OF_TRIALS = 30;
        
        CommonAbmConfiguration abmConfig = globalConfigurationFactory.getCommonAbmConfiguration();
        abmConfig.MAX_NUMBER_OF_CIRCLES = 15;
        abmConfig.MIN_NUMBER_OF_CIRCLES = 0;
        abmConfig.MAX_NUMBER_OF_TRIANGLES = 15;
        abmConfig.MIN_NUMBER_OF_TRIANGLES = 0;
        abmConfig.MAX_NUMBER_OF_SQUARES = 15;
        abmConfig.MIN_NUMBER_OF_SQUARES = 0;
        abmConfig.MIN_NUMBER_OF_DAYS = 1;
        abmConfig.MAX_NUMBER_OF_DAYS = 100;
        abmConfig.MAX_WORLD_SIZE = 15;
        abmConfig.MIN_WORLD_SIZE = 8;
        
        IOptimizer optimizer = ObjectFactory.createObject(IOptimizer.class);
        
        ObjectFactory.createObject(IGeneticAlgorithmListener.class).notifyNewExperimentExecuting(this);
        
        optimizer.run();
    }
}
