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


package com.velonuboso.made.core.rat;

import com.velonuboso.made.core.interfaces.ExecutionListenerInterface;
import com.velonuboso.made.core.interfaces.MadeEvaluatorInterface;
import com.velonuboso.made.core.interfaces.MadeFitnessFunctionInterface;
import com.velonuboso.made.core.interfaces.MadeFitnessFunctionInterface;
import com.velonuboso.made.core.setup.BaseAgentSetup;
import com.velonuboso.made.core.setup.GASetup;
import com.velonuboso.made.core.setup.GlobalSetup;
import java.util.Random;
import org.jgap.FitnessFunction;
import org.jgap.IChromosome;
import org.jgap.audit.Evaluator;

/**
 *
 * @author Ruben
 */
public class RatFitnessFunction extends FitnessFunction implements MadeFitnessFunctionInterface{

    private GlobalSetup globalSetup;
    private BaseAgentSetup baseAgentSetup;
    private GASetup gaSetup;
    private Random r;
    private ExecutionListenerInterface logger;
    private MadeEvaluatorInterface evaluator;
    
    public RatFitnessFunction(GlobalSetup globalSetup, 
            BaseAgentSetup baseAgentSetup, GASetup gaSetup, Random rand,
            ExecutionListenerInterface logger,
            MadeEvaluatorInterface eval) {
        this.gaSetup = gaSetup;
        this.globalSetup = globalSetup;
        this.baseAgentSetup = baseAgentSetup;
        this.r = rand;
        this.logger = logger;
        this.evaluator = eval;
    }

    
    public int getGeneNumber() {
        return globalSetup.getNumberOfProfiles()*RatAgent.NUMBER_OF_FEATURES;
    }
    
    @Override
    public double evaluate(IChromosome ic) {

        double ret = 0;
        for (int i=0; i<gaSetup.getTxtExecutionsAVG(); i++){
            RatEnvironment env = new RatEnvironment(ic, baseAgentSetup, globalSetup, r, logger, evaluator);
            ret += env.runEnvironment(false, false);
        }
        
        return ret / (double)gaSetup.getTxtExecutionsAVG();
    }

    /**
     * Only should be called manually
     * @param ic
     * @return
     */
    /*
    public double[] evaluateExecutions(IChromosome ic, int executions) {
        RatEvaluator e = RatEvaluator.getInstance();
        double results[] = new double[executions];
        double ret = 0;
        for (int i=0; i<executions; i++){
            RatEnvironment env = new RatEnvironment(ic);
            results[i] = env.runEnvironment(false, false);
        }
        return results;
    }*/

}
