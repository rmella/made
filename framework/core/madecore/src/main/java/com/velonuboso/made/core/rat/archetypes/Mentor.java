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
public class Mentor extends Archetype {

    public Mentor() {
        dependencies.add(Villain.class);
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
    public double evaluate(GlobalSetup setup, ArrayList<MadeAgentInterface> agents, ArchetypeOccurrence o) {

        int mentors = 0;
                
        // a mentor is someone who is friend of a hero knows the hero
        Pattern patt1 = Pattern.compile("@" + RatState.KINDLY_DISPLACED + " ([0-9]+)\n");
        for (MadeAgentInterface agent1 : agents) {
            Matcher m1 = patt1.matcher(agent1.getStringLog());
            int counter1 = 0;
            String targets = "";
            while (m1.find()) {
                int target = Integer.parseInt(m1.group(1));
                if (agents.get(target).getLabels() != null) {
                    for (LabelArchetype la : agents.get(target).getLabels()) {
                        if (la.getArchetypeName().compareTo(Villain.class.getSimpleName()) == 0) {
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
                    for (LabelArchetype la : agent1.getLabels()) {
                        if (la.getArchetypeName().compareTo(Villain.class.getSimpleName()) == 0) {
                            isVillain = true;
                        }
                    }

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
