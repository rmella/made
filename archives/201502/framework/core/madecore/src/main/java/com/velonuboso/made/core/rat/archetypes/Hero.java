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
    public double evaluate(GlobalSetup setup, ArrayList<MadeAgentInterface> agents, ArchetypeOccurrence o) {

        int heroes = 0;
        
        Pattern patt = Pattern.compile("@" + RatState.NUDGE_OK + " ([0-9]+)\n");

        for (MadeAgentInterface agent : agents) {
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
