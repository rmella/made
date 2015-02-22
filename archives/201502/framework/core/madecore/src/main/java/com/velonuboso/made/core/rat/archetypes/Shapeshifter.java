/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.velonuboso.made.core.rat.archetypes;

import com.velonuboso.made.core.common.ArchetypeType;
import com.velonuboso.made.core.interfaces.Archetype;
import com.velonuboso.made.core.interfaces.ArchetypeOccurrence;
import com.velonuboso.made.core.interfaces.MadeAgentInterface;
import com.velonuboso.made.core.setup.GlobalSetup;
import java.util.ArrayList;

/**
 *
 * @author rhgarcia
 */
public class Shapeshifter extends Archetype {

    public Shapeshifter() {
        dependencies.add(Hero.class);
    }

    

    @Override
    public String getDescription() {
        return "The Shapeshifter represents uncertainty and change, "
                + "reminding us that not all is as it seems. They may be a "
                + "character who keeps changing sides or whose allegiance "
                + "is uncertain. They act to keep the hero (and us) on his or "
                + "her toes and may thus catalyze critical action.\n" 
                + "A typical Shapeshifter is a person of the opposite sex "
                + "who provides the "
                + "love interest and whose affections vary across the story. "
                + "Other characters may also be shapeshifters, including "
                + "Mentors, Guardians and Tricksters.\n"
                + "(Vogler)";
    }

    @Override
    public double evaluate(GlobalSetup setup, ArrayList<MadeAgentInterface> agents, ArchetypeOccurrence o) {
        //TODO
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ArchetypeType getType() {
        return ArchetypeType.LITERARY;
    }


}
