/*
 * Copyright 2014 Rubén Héctor García <raiben@gmail.com>.
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

package com.velonuboso.made2.core.rat.archetypes;

import com.velonuboso.made2.core.common.LabelArchetype;
import com.velonuboso.made2.core.interfaces.Archetype;
import com.velonuboso.made2.core.interfaces.ArchetypeOccurrence;
import com.velonuboso.made2.core.rat.RatAgent;
import com.velonuboso.made2.core.rat.RatState;
import com.velonuboso.made2.core.setup.GlobalSetup;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author rhgarcia
 */
public class Villain extends Archetype{


    @Override
    public String getDescription() {
        return "The \"villain\" archetype is a character who displays "
                + "characteristics of pure evil. Typical villains are "
                + "self-centered, power-hungry and interested only in "
                + "achieving their personal goals, usually at the cost of "
                + "others. An example of an archetypal villain would be a "
                + "power-hungry politician who has his political enemies "
                + "assassinated to ensure his victory in an upcoming election.";
    }

    @Override
    public double evaluate(GlobalSetup setup, ArrayList<RatAgent> agents, ArchetypeOccurrence o) {
        
        Pattern patt = Pattern.compile("@"+RatState.KILL+"\\s");
        
        int villains = 0;
        for (RatAgent agent : agents) {
            Matcher m = patt.matcher(agent.getStringLog());
            int counter = 0;
            while (m.find()) counter++;
            if (counter >= 1){
                LabelArchetype la = new LabelArchetype(this.getClass().getSimpleName(), null, null, "");
                agent.addLabel(la);
                villains ++;
            }
        }
        return o.getValue(agents.size(), villains);
    }
}
