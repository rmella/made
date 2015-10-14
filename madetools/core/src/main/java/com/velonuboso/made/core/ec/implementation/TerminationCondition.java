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
package com.velonuboso.made.core.ec.implementation;

import com.velonuboso.made.core.common.api.IGlobalConfigurationFactory;
import com.velonuboso.made.core.common.entity.CommonEcConfiguration;
import com.velonuboso.made.core.common.util.ObjectFactory;
import com.velonuboso.made.core.ec.api.IIndividual;
import com.velonuboso.made.core.ec.api.ITerminationCondition;
import java.util.AbstractQueue;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Stack;
import org.apache.commons.collections.buffer.CircularFifoBuffer;

/**
 *
 * @author Rubén Héctor García (raiben@gmail.com)
 */
public class TerminationCondition implements ITerminationCondition{
    
    private CircularFifoBuffer lastBestFitness;
    
    public TerminationCondition() {
        lastBestFitness = null;
    }
    
    @Override
    public boolean mustFinish(int iteration, IIndividual bestIndividual) {
        IGlobalConfigurationFactory globalConfigurationFactory = 
            ObjectFactory.createObject(IGlobalConfigurationFactory.class);
        CommonEcConfiguration config = globalConfigurationFactory.getCommonEcConfiguration();
        
        if (lastBestFitness == null){
            lastBestFitness = new CircularFifoBuffer(config.TERMINATE_IF_NOT_IMPROVES_IN_ITERATIONS);
        }
        
        addFitnessToCircularBuffer(bestIndividual);
        
        boolean fitnessChanges = fitnessChangesInTheLastIterations(bestIndividual);
        boolean overMaximumIterations =  iteration>=config.MAXIMUM_ITERATIONS;
        
        return !fitnessChanges || overMaximumIterations;
    }

    private void addFitnessToCircularBuffer(IIndividual bestIndividual) {
        lastBestFitness.add(bestIndividual.getCurrentFitness().getValue().getAverage());
    }

    private boolean fitnessChangesInTheLastIterations(IIndividual bestIndividual) {
        IGlobalConfigurationFactory globalConfigurationFactory = 
            ObjectFactory.createObject(IGlobalConfigurationFactory.class);
        CommonEcConfiguration config = globalConfigurationFactory.getCommonEcConfiguration();
        
        if (lastBestFitness.size()<config.TERMINATE_IF_NOT_IMPROVES_IN_ITERATIONS){
            return true;
        }
        return lastBestFitness.stream().distinct().toArray().length > 1;
    }
}
