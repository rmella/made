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
package com.velonuboso.made.core.ec.api;

import com.velonuboso.made.core.common.util.ImplementedBy;
import com.velonuboso.made.core.ec.entity.Fitness;
import com.velonuboso.made.core.ec.implementation.listeners.ConsoleWriterGeneticAlgorithmListener;
import com.velonuboso.made.core.experiments.api.IExperiment;
import com.velonuboso.made.core.inference.entity.WorldDeductions;
import java.io.File;

/**
 *
 * @author Rubén Héctor García (raiben@gmail.com)
 */
@ImplementedBy(targetClass = ConsoleWriterGeneticAlgorithmListener.class, targetMode = ImplementedBy.Mode.SINGLETON)
public interface IGeneticAlgorithmListener {
    void notifyNewExperimentExecuting(IExperiment experiment);
    void notifyTrialExecuted(WorldDeductions deductions);
    void notifyIndividualEvaluation(Fitness fitness);
    void notifyIterationSummary(int iteration, long timeInMs, IIndividual bestIndividualEver, float populationAverage, float populationStandardDeviation);
}
