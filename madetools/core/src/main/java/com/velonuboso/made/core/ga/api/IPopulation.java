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
package com.velonuboso.made.core.ga.api;

import com.velonuboso.made.core.common.util.ImplementedBy;
import com.velonuboso.made.core.ga.implementation.Population;

/**
 *
 * @author Rubén Héctor García (raiben@gmail.com)
 */
@ImplementedBy(targetClass = Population.class, targetMode = ImplementedBy.Mode.NORMAL)
public interface IPopulation {
    public IIndividual getBestIndividual();
    public float getAverageFitness();
    public void add(IIndividual individual);
    public IPopulation selectMatingPool();
    public IPopulation createOffspring(int populationSize, float blxAlpha);
}
