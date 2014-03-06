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
import com.velonuboso.made.core.common.Gender;
import com.velonuboso.made.core.common.LabelArchetype;
import com.velonuboso.made.core.interfaces.Archetype;
import com.velonuboso.made.core.interfaces.ArchetypeOccurrence;
import com.velonuboso.made.core.interfaces.MadeAgentInterface;
import com.velonuboso.made.core.rat.RatAgent;
import com.velonuboso.made.core.rat.RatState;
import com.velonuboso.made.core.setup.GlobalSetup;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.math3.analysis.function.Gaussian;
import org.jgap.IChromosome;

/**
 * Romeo And Juliet
 * @author raiben@gmail.com
 */
public class RomeoJuliet extends Archetype {

    public RomeoJuliet() {
        super();
    }

    public String getArchetypeName() {
        return "Romeo & Juliet";
    }

    public double evaluate(GlobalSetup gs, ArrayList<MadeAgentInterface> agents,
            ArchetypeOccurrence o) {

        int matches = 0;
        
        // TODO
        HashMap<Integer, Integer> ids = new HashMap<Integer, Integer>();

        int i = 0;
        for (MadeAgentInterface agent : agents) {
            ids.put(agent.getId(), i);
            i++;
        }

        // 1.- Buscar parejas
        Pattern pat1 = Pattern.compile("@" + RatState.PARTNER_FOUND + " ([0-9]+)\n");
        HashMap<Integer, Integer> partners = new HashMap<Integer, Integer>();

        i = 0;
        for (MadeAgentInterface agent : agents) {
            Matcher m = pat1.matcher(agent.getStringLog());
            if (m.find()) {
                int partnerIndex = ids.get(Integer.parseInt(m.group(1)));
                if (!partners.containsKey(partnerIndex)) {
                    partners.put(i, partnerIndex);
                }
            }
            i++;
        }

        // 2.- Buscar los padres de las parejas
        Pattern pat2 = Pattern.compile("@" + RatState.DESCENDANT + " ([0-9]+) ([0-9]+)\n");
        Pattern pat3 = Pattern.compile("@" + RatState.NUDGE_OK + " ([0-9]+)\n");
        for (Integer k : partners.keySet()) {
            
            RatAgent agent = (RatAgent) agents.get(k);
            RatAgent partner = (RatAgent) agents.get(partners.get(k));
            Matcher m2 = pat2.matcher(agent.getStringLog());
            if (m2.find()) {
                boolean found = false;
                RatAgent parent1 = (RatAgent) agents.get(ids.get(Integer.parseInt(m2.group(1))));
                Matcher m3 = pat3.matcher(parent1.getStringLog());
                if (m3.find()) {
                    found = true;
                }
                if (!found) {
                    RatAgent parent2 = (RatAgent) agents.get(ids.get(Integer.parseInt(m2.group(2))));
                    Matcher m4 = pat3.matcher(parent2.getStringLog());
                    if (m4.find()) {
                        found = true;
                    }
                }
                if (found) {

                    if (agent.getGender() == Gender.MALE) {
                        agent.addLabel(new LabelArchetype("Romeo (" + partner.getId() + ")"));
                        partner.addLabel(new LabelArchetype("Juliet (" + agent.getId() + ")"));
                    } else {
                        agent.addLabel(new LabelArchetype("Juliet (" + partner.getId() + ")"));
                        partner.addLabel(new LabelArchetype("Romeo (" + agent.getId() + ")"));
                    }
                    matches += 2;
                }
            }
        }

        return o.getValue(agents.size(), matches);
    }

    @Override
    public ArchetypeType getType() {
        return ArchetypeType.LITERARY;
    }

    @Override
    public String getDescription() {
        return "Two lovers whose families are in war";
    }

}
