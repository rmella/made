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

import com.velonuboso.made.core.ec.api.IGeneticAlgorithmListener;
import com.velonuboso.made.core.ec.api.IIndividual;
import com.velonuboso.made.core.ec.entity.Fitness;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 *
 * @author Rubén Héctor García (raiben@gmail.com)
 */
public class GeneticAlgorithmListener implements IGeneticAlgorithmListener{

    @Override
    public void notifyIterationSummary(int iteration, IIndividual bestIndividual, Fitness bestFitnessValue, float averagePopulationFitness) {
        NumberFormat format = NumberFormat.getInstance(Locale.ENGLISH);
        
        ArrayList<String> elements = new ArrayList<>();
        elements.add(format.format(iteration));
        elements.add(format.format(bestFitnessValue.getValue().getAverage()));
        elements.add(format.format(bestFitnessValue.getValue().getStandardDeviation()));
        elements.add(format.format(bestFitnessValue.getValue().getNumberOfTrials()));
        String csvLine = String.join(";", elements);
        System.out.println(csvLine);
    }
    
}
