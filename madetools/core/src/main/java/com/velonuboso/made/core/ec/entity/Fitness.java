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
package com.velonuboso.made.core.ec.entity;

import java.util.HashMap;

/**
 *
 * @author Rubén Héctor García (raiben@gmail.com)
 */
public class Fitness implements Comparable<Fitness> {

    public TrialInformation EMPTY_TRIAL_INFORMATION = new TrialInformation(0, Float.MIN_VALUE, 0);

    private TrialInformation value;
    private HashMap<String, TrialInformation> extraMeasures;

    public Fitness() {
        value = EMPTY_TRIAL_INFORMATION;
        this.extraMeasures = new HashMap<>();
    }

    public Fitness(TrialInformation value) {
        super();
        this.value = value;
    }

    public HashMap<String, TrialInformation> getExtraMeasures() {
        return extraMeasures;
    }

    public TrialInformation getValue() {
        return value;
    }

    public void setValue(TrialInformation value) {
        this.value = value;
    }

    public void setValue(int numberOfTrials, float average, float standardDeviation) {
        this.value = new TrialInformation(numberOfTrials, average, standardDeviation);
    }

    @Override
    public int compareTo(Fitness o) {
        return Float.compare(this.getValue().average, o.getValue().getAverage());
    }
}
