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
public class Hero extends Archetype {

    public Hero() {
        dependencies.add(Villain.class);
    }

    
    
    @Override
    public String getArchetypeName() {
        return "Hero";
    }

    @Override
    public String getDescription() {
        return "In its simplest form, this character is the one ultimately who "
                + "may fulfill a necessary task and who will restore fertility, "
                + "harmony, and/or justice to a community. The hero character "
                + "is the one who typically experiences an initiation, who goes "
                + "the community’s ritual (s), et cetera. Often he or she will "
                + "embody characteristics of YOUNG PERSON FROM THE PROVINCES, "
                + "INITIATE, INNATE WISDOM, PUPIL, and SON.";
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
