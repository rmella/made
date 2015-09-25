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
package com.velonuboso.made.core.ga.implementation;

import com.sun.javafx.scene.control.skin.VirtualFlow;
import com.velonuboso.made.core.ga.api.*;
import com.velonuboso.made.core.ga.entity.GenType;
import com.velonuboso.made.core.ga.entity.GeneDefinition;
import com.velonuboso.made.core.ga.entity.IndividualDefinition;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Rubén Héctor García (raiben@gmail.com)
 */
public class GeneticAlgorithm implements IGeneticAlgorithm{

    private List<IGeneticAlgorithmListener> listeners;
    private IndividualDefinition definition;
    private int populationSize;
    private int maximumIterations;
    private float blxAlpha;
    
    private static final IndividualDefinition EmptyDefinition = new IndividualDefinition(new GeneDefinition[0]);
    
    public GeneticAlgorithm() {
        listeners = new ArrayList<>();
        definition = EmptyDefinition;
        populationSize = 0;
        maximumIterations = 0;
        blxAlpha = 0;
    }

    @Override
    public void addListener(IGeneticAlgorithmListener listener) {
        listeners.add(listener);
    }

    @Override
    public void configure(IndividualDefinition definition, int populationSize, int maximumIterations, float blxAlpha) {
        this.definition = definition;
        this.populationSize = populationSize;
        this.maximumIterations = maximumIterations;
        this.blxAlpha = blxAlpha;
    }

    @Override
    public void run() {
        
    }
}
