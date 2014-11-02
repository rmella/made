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
public class ThresholdGuardian extends Archetype {

    public ThresholdGuardian() {
        dependencies.add(Hero.class);
        dependencies.add(Mentor.class);
    }

    @Override
    public String getDescription() {
        return "The Threshold Guardian provides the obstacles to the hero "
                + "at transitional points in the story. To get past the "
                + "guardian the hero must fight them, answer riddles, solve "
                + "problems, give a gift, and so on.\n"
                + "The Threshold Guardian is often neutral, neither supporting "
                + "nor opposing the "
                + "hero, although they also may be allied to the antagonist or "
                + "even a potential ally.\n"
                + "Thresholds appear before the hero sets out on their "
                + "journey, before they "
                + "enter the final 'lion's den' and at critical scene changes. "
                + "Crossing thresholds symbolize change and points of growth "
                + "in the hero's character.\n"
                + "(Vogler)";
    }

    @Override
    public double evaluate(GlobalSetup setup, ArrayList<RatAgent> agents, ArchetypeOccurrence o) {
        int guardian = 0;

        Pattern patt = Pattern.compile("@" + RatState.NUDGED + " ([0-9]+)\n[\\s\\S.]*@" + RatState.NUDGED + " \\1\\n");

        for (RatAgent agent : agents) {
            Matcher m = patt.matcher(agent.getStringLog());
            int counter = 0;
            String targets = "";
            while (m.find()) {
                int target = Integer.parseInt(m.group(1));
                if (agents.get(target).getLabels() != null) {
                    for (LabelArchetype la : agents.get(target).getLabels()) {
                        if (la.getArchetypeName().compareTo(Hero.class.getSimpleName()) == 0) {
                            targets += target + " ";
                            counter++;
                        }
                    }
                }
            }
            if (counter >= 1) {

                // the guardian is not a hero, a mentor or a guardian
                boolean isHeroMentorOrVillain = false;
                /**
                for (LabelArchetype l : agent.getLabels()) {
                    if (l.getArchetypeName().compareTo(Hero.class.getSimpleName()) == 0
                            || l.getArchetypeName().compareTo(Mentor.class.getSimpleName()) == 0
                            || l.getArchetypeName().compareTo(Villain.class.getSimpleName()) == 0) {
                        isHeroMentorOrVillain = true;
                    }
                }
                */

                if (!isHeroMentorOrVillain){
                    LabelArchetype la = new LabelArchetype(this.getClass().getSimpleName(), null, null, targets);
                    agent.addLabel(la);
                    guardian++;
                }
            }
        }
        return o.getValue(agents.size(), guardian);
    }

}
