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

    private static final int DEFAULT_BUFFER_SIZE = 30;
    private static final int DEFAULT_MAXIMUM_ITERATIONS = 10000;
    
    private CircularFifoBuffer lastBestFitness;
    private int maximumIterations;
    private int bufferSize;
    
    public TerminationCondition() {
        maximumIterations = DEFAULT_MAXIMUM_ITERATIONS;
        bufferSize = DEFAULT_BUFFER_SIZE;
        lastBestFitness = null;
    }
    
    @Override
    public boolean mustFinish(int iteration, IIndividual bestIndividual) {
        
        if (lastBestFitness == null){
            lastBestFitness = new CircularFifoBuffer(bufferSize);
        }
        
        addFitnessToCircularBuffer(bestIndividual);
        
        boolean fitnessChanges = fitnessChangesInTheLastIterations(bestIndividual);
        boolean overMaximumIterations =  iteration>=maximumIterations;
        
        return !fitnessChanges || overMaximumIterations;
    }

    private void addFitnessToCircularBuffer(IIndividual bestIndividual) {
        lastBestFitness.add(bestIndividual.getCurrentFitness().getValue().getAverage());
    }

    private boolean fitnessChangesInTheLastIterations(IIndividual bestIndividual) {
        if (lastBestFitness.size()<bufferSize){
            return true;
        }
        return lastBestFitness.stream().distinct().toArray().length > 1;
    }

    @Override
    public void setMaximumIterations(int maximumIterations) {
        this.maximumIterations = maximumIterations;
    }
    
    @Override
    public void setSizeOfBufferToCheckChanges(int bufferSize) {
        this.bufferSize = bufferSize;
        
    }
}
