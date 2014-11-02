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
public class Mentor extends Archetype {

    public Mentor() {
        dependencies.add(Hero.class);
    }

    @Override
    public String getDescription() {
        return "These individuals serve as teachers or counselors to the "
                + "initiates. Sometimes they work as role models and often "
                + "serve as father or mother figure. They teach by example "
                + "the skills necessary to survive the journey and quest.\n"
                + "The mentor helps the hero in some way, furnishing them "
                + "with important skills and advice. They may appear at "
                + "important moments to help the hero get over an obstacle, "
                + "then disappear (perhaps to mentor another unknown hero).\n"
                + "Typical mentors are old and wise. Although they may "
                + "also be younger, they "
                + "are still likely to be older than the hero as they offer "
                + "their superior knowledge and experience in support. "
                + "Perhaps once they were a young hero themselves.\n"
                + "The act of giving reminds us of the generosity to "
                + "which we must aspire. "
                + "The receiving of the gift may well be seen as reward "
                + "for courage and self-sacrifice.\n"
                + "(Vogler)";
    }

    @Override
    public double evaluate(GlobalSetup setup, ArrayList<RatAgent> agents, ArchetypeOccurrence o) {

        int mentors = 0;
                
        // a mentor is someone who is friend of a hero knows the hero
        Pattern patt1 = Pattern.compile("@" + RatState.KINDLY_DISPLACED + " ([0-9]+)\n");
        for (RatAgent agent1 : agents) {
            Matcher m1 = patt1.matcher(agent1.getStringLog());
            int counter1 = 0;
            String targets = "";
            while (m1.find()) {
                int target = Integer.parseInt(m1.group(1));
                if (agents.get(target).getLabels() != null) {
                    for (LabelArchetype la : agents.get(target).getLabels()) {
                        if (la.getArchetypeName().compareTo(Hero.class.getSimpleName()) == 0) {
                            targets += target + " ";
                            counter1++;
                        }
                    }
                }
            }
            if (counter1 >= 1) {

                // and also someone who had free time to improve his/her skills
                Pattern patt2 = Pattern.compile("@" + RatState.FREE_TIME + "\n");
                Matcher m = patt2.matcher(agent1.getStringLog());
                int counter = 0;
                while (m.find()) {
                    counter++;
                }

                if (counter > (agent1.getDays() * 2 / 3)) {

                    
                    boolean isVillain = false;
                    /*
                    for (LabelArchetype la : agent1.getLabels()) {
                        if (la.getArchetypeName().compareTo(Villain.class.getSimpleName()) == 0) {
                            isVillain = true;
                        }
                    }
                    */
                        // a mentor cannot be a villain
                    if (!isVillain) {
                        LabelArchetype la = new LabelArchetype(this.getClass().getSimpleName(), null, null, Integer.toString(counter));
                        agent1.addLabel(la);
                        mentors++;
                    }
                }
            }
        }
        return o.getValue(agents.size(), mentors);

    }

}
