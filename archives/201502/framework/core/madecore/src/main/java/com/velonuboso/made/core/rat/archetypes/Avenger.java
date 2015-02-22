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
import com.velonuboso.made.core.common.LabelArchetype;
import com.velonuboso.made.core.interfaces.Archetype;
import com.velonuboso.made.core.interfaces.ArchetypeOccurrence;
import com.velonuboso.made.core.interfaces.MadeAgentInterface;
import com.velonuboso.made.core.setup.GlobalSetup;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * an avenger
 * @author raiben@gmail.com
 */
public class Avenger extends Archetype{
    
    public Avenger() {
        super();
    }

    
    public String getArchetypeName() {
        return "Avenger";
    }

    public double evaluate(GlobalSetup gs, ArrayList<MadeAgentInterface> agents, 
            ArchetypeOccurrence o) {
        
        Pattern patt = Pattern.compile("@NUDGED (\\w+)\\s[\\s\\S]*@NUDGE_OK \\1");
        
        int avengers = 0;
        for (MadeAgentInterface agent : agents) {
            Matcher m = patt.matcher(agent.getStringLog());
            if (m.find()){
                LabelArchetype la = new LabelArchetype("Avenger("+m.group(1)+")", null, null, "REVENGE TO THE AGENT "+m.group(1));
                agent.addLabel(la);
                avengers ++;
            }
        }
        return o.getValue(agents.size(), avengers);
    }

    @Override
    public ArchetypeType getType() {
        return ArchetypeType.PERSONAL;
    }

    @Override
    public String getDescription() {
        return "An character hurt by other one in the past, that revenge on him/her";
    }

}
