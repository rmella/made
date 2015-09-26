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

import com.velonuboso.made.core.common.util.ObjectFactory;
import com.velonuboso.made.core.ec.api.IIndividual;
import com.velonuboso.made.core.ec.api.IPopulation;
import java.util.ArrayList;
import java.util.Comparator;

/**
 *
 * @author Rubén Héctor García (raiben@gmail.com)
 */
public class Population implements IPopulation {

    ArrayList<IIndividual> individuals;

    public Population() {
        individuals = new ArrayList<>();
    }

    @Override
    public IIndividual getBestIndividual() {
        individuals.stream().forEach(individual -> individual.reEvaluate());
        return individuals.stream()
                .max((IIndividual firstIndividual, IIndividual secondIndividual) -> 
                        Float.compare(firstIndividual.getCurrentFitness(), secondIndividual.getCurrentFitness()))
                .orElse(null);
    }

    @Override
    public float getAverageFitness() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void add(IIndividual individual) {
        individuals.add(individual);
    }

    @Override
    public IPopulation selectMatingPool() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public IPopulation createOffspring(int populationSize, float blxAlpha) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
