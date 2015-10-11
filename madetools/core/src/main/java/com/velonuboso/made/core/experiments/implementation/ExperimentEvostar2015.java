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

import com.velonuboso.made.core.common.util.ObjectFactory;
import com.velonuboso.made.core.experiments.api.IExperiment;
import com.velonuboso.made.core.optimization.api.IOptimizer;

/**
 *
 * @author Rubén Héctor García (raiben@gmail.com)
 */
public class ExperimentEvostar2015 extends BaseExperiment{
    
    @Override
    public String getDescription() {
        return "Experiment for EvoGames 2016";
    }
    
    @Override
    public void run(String[] arguments) {
        IOptimizer optimizer = ObjectFactory.createObject(IOptimizer.class);
        optimizer.configure(10, 50, 0.5f, 20);
        optimizer.run();
    }

}
