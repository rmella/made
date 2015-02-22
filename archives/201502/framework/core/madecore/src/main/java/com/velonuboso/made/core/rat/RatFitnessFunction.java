/*
 * Copyright 2013 Rubén Héctor García <raiben@gmail.com>.
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
 * @author raiben@gmail.com
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
