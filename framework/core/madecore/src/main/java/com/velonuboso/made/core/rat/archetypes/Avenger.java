/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.velonuboso.made.core.rat.archetypes;

import com.velonuboso.made.core.common.ArchetypeType;
import com.velonuboso.made.core.interfaces.Archetype;
import com.velonuboso.made.core.interfaces.MadeAgentInterface;
import com.velonuboso.made.core.setup.GlobalSetup;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.math3.analysis.function.Gaussian;
import org.jgap.IChromosome;

/**
 *
 * @author rhgarcia
 */
public class Avenger extends Archetype{


    
    public Avenger() {
        super();
    }

    
    public String getName() {
        return "Population Growth";
    }

    public double evaluate(GlobalSetup gs, ArrayList<MadeAgentInterface> agents, Float from, Float to) {
        
        Pattern patt = Pattern.compile("@NUDGED (\\w+)\\s[\\s\\S]*@NUDGE_OK \\1");
        
        int avengers = 0;
        for (MadeAgentInterface agent : agents) {
            Matcher m = patt.matcher(agent.getStringLog());
            if (m.find()){
                agent.addLabel("Avenger("+m.group(1)+")");
                avengers ++;
            }
        }
        double ratio = (double) avengers / (double) agents.size();
     
        return ratio*(to-from);//getGaussian(ratio, from, to);
    }

    @Override
    public ArchetypeType getType() {
        return ArchetypeType.LITERARY;
    }

}
