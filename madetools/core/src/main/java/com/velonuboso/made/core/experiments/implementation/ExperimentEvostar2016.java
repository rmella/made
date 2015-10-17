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
public class ExperimentEvostar2016 extends BaseExperiment {

    public int MAX_NUMBER_OF_PIECES_BY_SHAPE = 10;
    public int MAX_NUMBER_OF_DAYS = 100;
    public int MAX_WORLD_SIZE = 15;
    public int MIN_WORLD_SIZE = 8;
    public int MAXIMUM_ITERATIONS = 50;
    public int POPULATION_SIZE = 6;
    public int NUMBER_OF_TRIALS = 1;

    @Override
    public String getDescription() {
        return "Experiment for EvoGames 2016";
    }

    @Override
    public void run(String[] arguments) {
        ExcelWriterGeneticAlgorithmListener listener = new ExcelWriterGeneticAlgorithmListener();
        ObjectFactory.installMock(IGeneticAlgorithmListener.class, listener);

        IGlobalConfigurationFactory globalConfigurationFactory
                = ObjectFactory.createObject(IGlobalConfigurationFactory.class);
        CommonEcConfiguration config = globalConfigurationFactory.getCommonEcConfiguration();
        CommonAbmConfiguration abmConfig = globalConfigurationFactory.getCommonAbmConfiguration();

        config.MAXIMUM_ITERATIONS = MAXIMUM_ITERATIONS;
        config.POPULATION_SIZE = POPULATION_SIZE;
        config.NUMBER_OF_TRIALS = NUMBER_OF_TRIALS;
        abmConfig.MAX_NUMBER_OF_CIRCLES = MAX_NUMBER_OF_PIECES_BY_SHAPE;
        abmConfig.MIN_NUMBER_OF_CIRCLES = 0;
        abmConfig.MAX_NUMBER_OF_TRIANGLES = MAX_NUMBER_OF_PIECES_BY_SHAPE;
        abmConfig.MIN_NUMBER_OF_TRIANGLES = 0;
        abmConfig.MAX_NUMBER_OF_SQUARES = MAX_NUMBER_OF_PIECES_BY_SHAPE;
        abmConfig.MIN_NUMBER_OF_SQUARES = 0;
        abmConfig.MIN_NUMBER_OF_DAYS = 1;
        abmConfig.MAX_NUMBER_OF_DAYS = MAX_NUMBER_OF_DAYS;
        abmConfig.MAX_WORLD_SIZE = MAX_WORLD_SIZE;
        abmConfig.MIN_WORLD_SIZE = MIN_WORLD_SIZE;

        IOptimizer optimizer = ObjectFactory.createObject(IOptimizer.class);

        ObjectFactory.createObject(IGeneticAlgorithmListener.class).notifyNewExperimentExecuting(this);

        optimizer.run();
    }
}
