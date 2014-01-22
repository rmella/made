/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.velonuboso.made.core.rat.archetypes;

import com.velonuboso.made.core.interfaces.Archetype;
import com.velonuboso.made.core.interfaces.MadeAgentInterface;
import com.velonuboso.made.core.setup.GlobalSetup;
import java.util.ArrayList;
import org.apache.commons.math3.analysis.function.Gaussian;
import org.jgap.IChromosome;

/**
 *
 * @author rhgarcia
 */
public class PopulationGrowthArchetype extends Archetype{

    public String getName() {
        return "Population Growth";
    }

    public double evaluate(GlobalSetup gs, ArrayList<MadeAgentInterface> agents, Float from, Float to) {
        
        int aliveAgents = 0;
        for (MadeAgentInterface agent : agents) {
            if (agent.isAlive()){
                aliveAgents ++;
            }
        }
        float ratio = aliveAgents / gs.getNumberOfInitialAgents();
     
        return getGaussian(ratio, from, to);
    }

}
