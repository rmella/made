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
package com.velonuboso.made.core.ec.implementation.listeners;

import com.velonuboso.made.core.ec.api.IGeneticAlgorithmListener;
import com.velonuboso.made.core.ec.api.IIndividual;
import com.velonuboso.made.core.ec.entity.Fitness;
import com.velonuboso.made.core.ec.entity.TrialInformation;
import com.velonuboso.made.core.experiments.api.IExperiment;
import com.velonuboso.made.core.inference.entity.WorldDeductions;
import java.io.File;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 *
 * @author Rubén Héctor García (raiben@gmail.com)
 */
public class ConsoleWriterGeneticAlgorithmListener implements IGeneticAlgorithmListener {

    boolean headerprinted = false;
    boolean inEvaluation = false;

    public ConsoleWriterGeneticAlgorithmListener() {
    }

    @Override
    public void notifyIterationSummary(int iteration, IIndividual bestIndividualEver, float populationAverage, float populationStandardDeviation) {
        if (!headerprinted) {
            headerprinted = true;
            printHeader(bestIndividualEver);
        }
        printLine(iteration, populationAverage, populationStandardDeviation, bestIndividualEver);
        inEvaluation = false;
    }

    private void printLine(int iteration, float populationAverage, float populationStandardDeviation, IIndividual bestIndividualEver) {
        NumberFormat format = NumberFormat.getInstance(Locale.ENGLISH);
        
        ArrayList<String> elements = new ArrayList<>();
        elements.add(format.format(iteration));

        elements.add(format.format(populationAverage));
        elements.add(format.format(populationStandardDeviation));

        elements.add(format.format(bestIndividualEver.getCurrentFitness().getValue().getAverage()));
        elements.add(format.format(bestIndividualEver.getCurrentFitness().getValue().getStandardDeviation()));
        elements.add(format.format(bestIndividualEver.getCurrentFitness().getValue().getNumberOfTrials()));

        HashMap<String, TrialInformation> extraMeasures = bestIndividualEver.getCurrentFitness().getExtraMeasures();
        List<String> tags = extraMeasures.keySet().stream().sorted().collect(Collectors.toList());
        tags.stream().forEach(tag -> {
            elements.add(format.format(extraMeasures.get(tag).getAverage()));
            elements.add(format.format(extraMeasures.get(tag).getStandardDeviation()));
        });

        String csvLine = String.join(";", elements);
        System.out.print(csvLine + "\n");
    }

    private void printHeader(IIndividual bestIndividualEver) {
        ArrayList<String> elements = new ArrayList<>();
        elements.add("Iteration");
        
        elements.add("Population average");
        elements.add("Population std dev.");
        
        elements.add("Best fitness");
        elements.add("Best Fitness Std. dev. (trials)");
        elements.add("Number of trials");
        
        HashMap<String, TrialInformation> extraMeasures = bestIndividualEver.getCurrentFitness().getExtraMeasures();
        List<String> tags = extraMeasures.keySet().stream().sorted().collect(Collectors.toList());
        tags.stream().forEach(tag -> {
            elements.add(tag);
            elements.add(tag + " Std. dev.");
        });
        
        String csvLine = String.join(";", elements);
        System.out.print(csvLine + "\n");
    }

    @Override
    public void notifyTrialExecuted(WorldDeductions deductions) {
        if (!inEvaluation) {
            inEvaluation = true;
            System.out.print("#");
        }
        System.out.print("*");
    }

    @Override
    public void notifyIndividualEvaluation(Fitness fitness) {
        System.out.print(" " + fitness.getValue().getAverage() + "\n");
        inEvaluation = false;
    }

    @Override
    public void notifyNewExperimentExecuting(IExperiment experiment) {
    }
}
