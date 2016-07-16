/*
 * Copyright (C) 2016 Rubén Héctor García (raiben@gmail.com)
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
package com.velonuboso.made.core.experiments.implementation;

import com.velonuboso.made.core.ec.api.IIndividual;
import java.util.HashMap;
import joptsimple.OptionSet;

/**
 *
 * @author Rubén Héctor García (raiben@gmail.com)
 */
public class RunIndividual extends BaseExperimentExtension {

    private static final String ARGUMENT_INDIVIDUAL = "trope";
    private static final String ARGUMENT_SEED = "tropeList";
    private IIndividual individual;
    private long seed;

    @Override
    public String getDescription() {
        return "Allows running an specific individual with an specific random "
                + "seed in order to get same results";
    }

    @Override
    public HashMap<String, String> getOptionsMapOfNamesAndDescriptions() {
        HashMap<String, String> map = new HashMap<>();
        map.put(ARGUMENT_INDIVIDUAL, "Individual to execute with format [0.1, 0.3]");
        map.put(ARGUMENT_SEED, "Random generator's seed (positive number)");
        return map;
    }

    @Override
    public void doRun(OptionSet options) {
        initializeSeed(options);

        initializeIndividual(options);
    }

    private void initializeSeed(OptionSet options) {
        if (options.has(ARGUMENT_SEED)) {
            seed = ExperimentUtilities.getSeed(
                    options.valueOf(ARGUMENT_SEED).toString());

        } else {
            seed = System.currentTimeMillis();
        }
    }

    private void initializeIndividual(OptionSet options) throws UnsupportedOperationException {
        if (options.has(ARGUMENT_INDIVIDUAL)) {
            individual = ExperimentUtilities.getIndividualFromString(
                    options.valueOf(ARGUMENT_INDIVIDUAL).toString());
        } else {
            // TODO add a sample individual
            throw new UnsupportedOperationException("Not supported yet.");
        }

        // TODO: Argument seed
        // TODO: Logic
    }
}
