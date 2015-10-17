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
import com.velonuboso.made.core.optimization.api.IOptimizer;

/**
 *
 * @author Rubén Héctor García (raiben@gmail.com)
 */
public class ExperimentEvostar2016 extends BaseExperiment {

    @Override
    public String getDescription() {
        return "Experiment for EvoGames 2016";
    }

    @Override
    public void run(String[] arguments) {
        installMockForExcelWriter();
        IGlobalConfigurationFactory globalConfigurationFactory
                = ObjectFactory.createObject(IGlobalConfigurationFactory.class);
     
        configureEcModule(globalConfigurationFactory);        
        configureAbmModule(globalConfigurationFactory);

        ObjectFactory.createObject(IGeneticAlgorithmListener.class).notifyNewExperimentExecuting(this);

        IOptimizer optimizer = ObjectFactory.createObject(IOptimizer.class);
        optimizer.run();
    }

    private void installMockForExcelWriter() {
        ExcelWriterGeneticAlgorithmListener listener = new ExcelWriterGeneticAlgorithmListener();
        ObjectFactory.installMock(IGeneticAlgorithmListener.class, listener);
    }
    
    private void configureEcModule(IGlobalConfigurationFactory globalConfigurationFactory) {
        CommonEcConfiguration ecConfig = globalConfigurationFactory.getCommonEcConfiguration();
        ecConfig.MAXIMUM_ITERATIONS = 1000;
        ecConfig.POPULATION_SIZE = 50;
        ecConfig.NUMBER_OF_TRIALS = 30;
        ecConfig.FITNESS_TROPE = null;
    }

    private void configureAbmModule(IGlobalConfigurationFactory globalConfigurationFactory) {
        CommonAbmConfiguration abmConfig = globalConfigurationFactory.getCommonAbmConfiguration();   
        abmConfig.MAX_NUMBER_OF_CIRCLES = 16;
        abmConfig.MAX_NUMBER_OF_TRIANGLES = 16;
        abmConfig.MAX_NUMBER_OF_SQUARES = 16;
        abmConfig.MIN_NUMBER_OF_DAYS = 2;
        abmConfig.MAX_NUMBER_OF_DAYS = 256;
        abmConfig.MAX_WORLD_SIZE = 16;
        abmConfig.MIN_WORLD_SIZE = 8;
    }
}
