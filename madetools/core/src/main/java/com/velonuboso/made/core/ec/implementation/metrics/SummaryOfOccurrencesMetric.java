/*
 * Copyright (C) 2015 Ruben
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
package com.velonuboso.made.core.ec.implementation.metrics;

import com.velonuboso.made.core.ec.api.IFitnessMetric;
import com.velonuboso.made.core.ec.entity.TrialInformation;
import com.velonuboso.made.core.inference.entity.Trope;
import com.velonuboso.made.core.inference.entity.WorldDeductions;
import java.util.ArrayList;

/**
 *
 * @author Ruben
 */
public class SummaryOfOccurrencesMetric implements IFitnessMetric {

    @Override
    public TrialInformation getTrialInformation(ArrayList<WorldDeductions> deductionsForAllTrials) {
        ArrayList<Float> fitnessPerTrial = new ArrayList<>();
        deductionsForAllTrials.stream().map(
                (deductions) -> deductions.values().stream()
                .map((tropes) -> tropes.length)
                .reduce(0, Integer::sum))
                .forEach((numberOfTropes) -> {
                    fitnessPerTrial.add((float) numberOfTropes);
                });
        return getTrialInformationFromSummary(fitnessPerTrial, deductionsForAllTrials);
    }

    @Override
    public TrialInformation getTrialInformationForSpecificTrope(ArrayList<WorldDeductions> deductionsForAllTrials, Trope tropeToLookAt) {
        ArrayList<Float> fitnessPerTrial = new ArrayList<>();
        deductionsForAllTrials.stream().map(
                (deductions) -> deductions.get(tropeToLookAt) != null
                        ? deductions.get(tropeToLookAt).length
                        : 0)
                .forEach((numberOfTropes) -> {
                    fitnessPerTrial.add((float) numberOfTropes);
                });
        return getTrialInformationFromSummary(fitnessPerTrial, deductionsForAllTrials);
    }

    private TrialInformation getTrialInformationFromSummary(ArrayList<Float> fitnessPerTrial,
            ArrayList<WorldDeductions> deductionsForAllTrials) {
        float average = (float) fitnessPerTrial.stream().mapToDouble(val -> (double) val).average().orElse(0f);
        float variance = fitnessPerTrial.size()==1
                ? 0 
                : (float) fitnessPerTrial.stream().mapToDouble(val -> Math.pow(average - val, 2)).sum() / (fitnessPerTrial.size() - 1f);
        float standardDeviation = (float) Math.sqrt(variance);
        int numberOfTriaals = deductionsForAllTrials.size();
        return new TrialInformation(numberOfTriaals, average, standardDeviation);
    }
}
