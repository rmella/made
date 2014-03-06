/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.velonuboso.made.core.rat.archetypes;

import com.velonuboso.made.core.common.ArchetypeType;
import com.velonuboso.made.core.common.LabelArchetype;
import com.velonuboso.made.core.interfaces.Archetype;
import com.velonuboso.made.core.interfaces.ArchetypeOccurrence;
import com.velonuboso.made.core.interfaces.MadeAgentInterface;
import com.velonuboso.made.core.rat.RatState;
import com.velonuboso.made.core.setup.GlobalSetup;
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
    public double evaluate(GlobalSetup setup, ArrayList<MadeAgentInterface> agents, ArchetypeOccurrence o) {
        
        Pattern patt = Pattern.compile("@"+RatState.KILL+"\\s");
        
        int villains = 0;
        for (MadeAgentInterface agent : agents) {
            Matcher m = patt.matcher(agent.getStringLog());
            int counter = 0;
            while (m.find()) counter++;
            if (counter >= 1){
                LabelArchetype la = new LabelArchetype("Villain", null, null, "");
                agent.addLabel(la);
                villains ++;
            }
        }
        return o.getValue(agents.size(), villains);
    
    }
    
}
