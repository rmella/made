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

import com.velonuboso.made.core.common.api.IProbabilityHelper;
import com.velonuboso.made.core.common.util.ObjectFactory;
import com.velonuboso.made.core.ec.api.IIndividual;
import com.velonuboso.made.core.ec.api.IPopulation;
import com.velonuboso.made.core.ec.api.ISelectionOperator;
import java.util.stream.IntStream;

/**
 *
 * @author Rubén Héctor García (raiben@gmail.com)
 */
public class SelectionOperator implements ISelectionOperator{

    @Override
    public IPopulation selectMatingPool(IPopulation sourcePopulation) {
        IPopulation matingPool = ObjectFactory.createObject(IPopulation.class);
        
        IntStream.range(0, sourcePopulation.getIndividuals().size())
                .forEach(index -> selectByBinaryTournament(sourcePopulation, matingPool));
        
        return matingPool;
    }

    private void selectByBinaryTournament(IPopulation sourcePopulation, IPopulation matingPool) {
        IIndividual firstIndividualIndex = selectRandomIndividual(sourcePopulation);
        IIndividual secondIIndividual = selectRandomIndividual(sourcePopulation);
        
        IIndividual selected =  firstIndividualIndex.getCurrentFitness().compareTo(secondIIndividual.getCurrentFitness())>0?
                firstIndividualIndex: secondIIndividual;
        
        matingPool.add(selected);
    }

    private IIndividual selectRandomIndividual(IPopulation sourcePopulation) {
        IProbabilityHelper helper = ObjectFactory.createObject(IProbabilityHelper.class);
        int firstIndividualIndex = helper.getNextInt(0, sourcePopulation.getIndividuals().size()-1);
        return sourcePopulation.getIndividuals().get(firstIndividualIndex);
    }
}
