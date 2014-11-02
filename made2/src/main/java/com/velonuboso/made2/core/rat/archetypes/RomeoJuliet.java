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

import com.velonuboso.made2.core.common.ArchetypeType;
import com.velonuboso.made2.core.common.Gender;
import com.velonuboso.made2.core.common.LabelArchetype;
import com.velonuboso.made2.core.interfaces.Archetype;
import com.velonuboso.made2.core.interfaces.ArchetypeOccurrence;
import com.velonuboso.made2.core.rat.RatAgent;
import com.velonuboso.made2.core.rat.RatAgent;
import com.velonuboso.made2.core.rat.RatState;
import com.velonuboso.made2.core.setup.GlobalSetup;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Romeo And Juliet
 *
 * @author raiben@gmail.com
 */
public class RomeoJuliet extends Archetype {

    public RomeoJuliet() {
        super();
    }

    public String getArchetypeName() {
        return "Romeo & Juliet";
    }

    public double evaluate(GlobalSetup gs, ArrayList<RatAgent> agents,
            ArchetypeOccurrence o) {

        int matches = 0;

        // TODO
        // 1.- Buscar parejas
        Pattern pat1 = Pattern.compile("@" + RatState.PARTNER_FOUND + " ([0-9]+)\n");
        HashSet<RatAgent> partners = new HashSet<RatAgent>();

        int i = 0;
        for (RatAgent agent : agents) {
            if (!partners.contains(agent)) {
                boolean found = false;
                Matcher m = pat1.matcher(agent.getStringLog());
                if (m.find()) {
                    RatAgent partner = agents.get(Integer.parseInt(m.group(1)));

                    RatAgent parent11 = null;
                    RatAgent parent12 = null;
                    RatAgent parent21 = null;
                    RatAgent parent22 = null;

                    // 2.- Buscar los padres de las parejas
                    Pattern pat2 = Pattern.compile("@" + RatState.DESCENDANT + " ([0-9]+) ([0-9]+)\n");

                    Matcher m2 = pat2.matcher(agent.getStringLog());
                    if (m2.find()) {
                        parent11 = (RatAgent) agents.get(Integer.parseInt(m2.group(1)));
                        parent12 = (RatAgent) agents.get(Integer.parseInt(m2.group(2)));
                    }
                    m2 = pat2.matcher(partner.getStringLog());
                    if (m2.find()) {
                        parent21 = (RatAgent) agents.get(Integer.parseInt(m2.group(1)));
                        parent22 = (RatAgent) agents.get(Integer.parseInt(m2.group(2)));
                    }
                    
                    if (parent11 == null || parent12 == null || parent21 == null || parent22 == null){
                        // Eve and Adam
                    }else{
                        Pattern pat3 = Pattern.compile("@" + RatState.NUDGE_OK
                                + " (" + parent11.getId() + "|" + parent12.getId()
                                + "|" + parent21.getId() + "|" + parent22.getId()
                                + ")\n");

                        Matcher mpat = pat3.matcher(parent11.getStringLog());
                        if (mpat.find()) {
                            found = true;
                        } else {
                            mpat = pat3.matcher(parent12.getStringLog());
                            if (mpat.find()) {
                                found = true;
                            } else {
                                mpat = pat3.matcher(parent21.getStringLog());
                                if (mpat.find()) {
                                    found = true;
                                } else {
                                    mpat = pat3.matcher(parent22.getStringLog());
                                    if (mpat.find()) {
                                        found = true;
                                    }
                                }
                            }
                        }

                        if (found) {
                            if (((RatAgent) agent).getGender() == Gender.MALE) {
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
