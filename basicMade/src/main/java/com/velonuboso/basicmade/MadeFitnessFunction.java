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


package com.velonuboso.basicmade;

import org.jgap.FitnessFunction;
import org.jgap.IChromosome;

/**
 *
 * @author Ruben
 */
public class MadeFitnessFunction extends FitnessFunction{

    static int getGeneNumber() {
        MadeEvaluator e = MadeEvaluator.getInstance();
        return e.getProperty(e.NUMBER_OF_PROFILES)*MadeAgent.NUMBER_OF_FEATURES;
    }
    
    @Override
    protected double evaluate(IChromosome ic) {
        MadeEvaluator e = MadeEvaluator.getInstance();
        double ret = 0;
        for (int i=0; i<e.getProperty(e.AVERAGE); i++){
            MadeEnvironment env = new MadeEnvironment(ic);
            ret += env.runEnvironment(false);
        }
        return ret / (double)e.getProperty(e.AVERAGE);
    }
    
}
