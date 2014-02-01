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
 * @author raiben@gmail.com
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
