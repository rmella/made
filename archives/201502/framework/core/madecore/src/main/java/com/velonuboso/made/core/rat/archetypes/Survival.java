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
import com.velonuboso.made.core.interfaces.ArchetypeOccurrence;
import com.velonuboso.made.core.interfaces.MadeAgentInterface;
import com.velonuboso.made.core.setup.GlobalSetup;
import java.util.ArrayList;
import org.apache.commons.math3.analysis.function.Gaussian;
import org.jgap.IChromosome;

/**
 *
 * @author raiben@gmail.com
 */
public class Survival extends Archetype{
    
    public String getArchetypeName() {
        return "Population stability";
    }

    public double evaluate(GlobalSetup gs, ArrayList<MadeAgentInterface> agents, 
            ArchetypeOccurrence o) {
        
        int aliveAgents = 0;
        for (MadeAgentInterface agent : agents) {
            if (agent.isAlive()){
                aliveAgents ++;
            }
        }
     
        return o.getValue(agents.size(), aliveAgents);
    }

    @Override
    public ArchetypeType getType() {
        return ArchetypeType.GLOBAL;
    }

    @Override
    public String getDescription() {
        return "Survival";
    }
}
