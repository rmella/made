/*
 * Copyright 2013 Rubén Héctor García <raiben@gmail.com>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.velonuboso.made.prototype;

import com.velonuboso.made.core.MadeFitnessFunctionInterface;
import org.jgap.FitnessFunction;
import org.jgap.IChromosome;

/**
 *
 * @author Ruben
 */
public class RatFitnessFunctionGrqph extends FitnessFunction implements MadeFitnessFunctionInterface{


    static int getGeneNumber() {
        RatEvaluator e = RatEvaluator.getInstance();
        return Parameters.getInstance().getNumberOfProfiles()*RatAgent.NUMBER_OF_FEATURES;
    }
    
    @Override
    public double evaluate(IChromosome ic) {
        RatEvaluator e = RatEvaluator.getInstance();
        double ret = 0;
        for (int i=0; i<Parameters.getInstance().getNumberOfExecutions(); i++){
            RatEnvironment env = new RatEnvironment(ic);
            ret += env.runEnvironment(false, true);
        }
        return ret / (double)Parameters.getInstance().getNumberOfExecutions();
    }

}
