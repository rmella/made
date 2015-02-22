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
public class Hero extends Archetype {

    public Hero() {
        dependencies.add(Villain.class);
        dependencies.add(Shadow.class);
    }

    @Override
    public String getDescription() {
        return "In its simplest form, this character is the one ultimately who "
                + "may fulfill a necessary task and who will restore fertility, "
                + "harmony, and/or justice to a community. The hero character "
                + "is the one who typically experiences an initiation, who goes "
                + "the community’s ritual (s), et cetera. Often he or she will "
                + "embody characteristics of YOUNG PERSON FROM THE PROVINCES, "
                + "INITIATE, INNATE WISDOM, PUPIL, and SON.\n"
                + "The hero or heroine is the classic protagonist of the story "
                + "with whom we associate most. They embody our most "
                + "aspirational values and put higher duty and the welfare of "
                + "others before their own, even to extreme forms of "
                + "self-sacrifice. Achieving the goal of the story may thus "
                + "be achieved only at terrible personal cost, although the "
                + "hero may gain much personal learning and growth in the "
                + "transition.\n"
                + "Heroes can be willing or unwilling, deliberate or "
                + "accidental, solitary or leaders, already-recognized "
                + "as a hero or start out as an ordinary person.\n"
                + "(Vogler)";
    }

    @Override
    public double evaluate(GlobalSetup setup, ArrayList<RatAgent> agents, ArchetypeOccurrence o) {

        int heroes = 0;
        
        Pattern patt = Pattern.compile("@" + RatState.NUDGE_OK + " ([0-9]+)\n");

        for (RatAgent agent : agents) {
            Matcher m = patt.matcher(agent.getStringLog());
            int counter = 0;
            String targets = "";
            while (m.find()) {
                int target = Integer.parseInt(m.group(1));
                if (agents.get(target).getLabels() != null) {
                    for (LabelArchetype la : agents.get(target).getLabels()) {
                        if (la.getArchetypeName().compareTo(Villain.class.getSimpleName()) == 0) {
                            targets += target + " ";
                            counter++;
                        }
                    }
                }
            }
            if (counter >= 1) {
                LabelArchetype la = new LabelArchetype(this.getClass().getSimpleName(), null, null, targets);
                agent.addLabel(la);
                heroes++;
            }
        }
        return o.getValue(agents.size(), heroes);

    }

    @Override
    public ArchetypeType getType() {
        return ArchetypeType.LITERARY;
    }

}
